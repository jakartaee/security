/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.security.test;

import static ee.jakarta.tck.security.test.Assert.assertDefaultAuthenticated;
import static ee.jakarta.tck.security.test.Assert.assertDefaultNotAuthenticated;
import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class AppCustomSessionIT extends ArquillianBase {
    
    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    @Test
    public void testAuthenticated() {
        assertDefaultAuthenticated(
            readFromServer("/servlet?name=reza&password=secret1"));
    }
    
    @Test
    public void testNotAuthenticated() {
        assertDefaultNotAuthenticated(
            readFromServer("/servlet"));
    }
    
    @Test
    public void testNotAuthenticatedWrongName() {
        assertDefaultNotAuthenticated(
            readFromServer("/servlet?name=romo&password=secret1"));
    }
    
    @Test
    public void testNotAuthenticatedWrongPassword() {
        assertDefaultNotAuthenticated(
            readFromServer("/servlet?name=reza&password=wrongpassword"));
    }
    
    @Test
    public void testAuthenticatedSession() {
        
        // 1. Initially request page when we're not authenticated
        
        assertDefaultNotAuthenticated(
            readFromServer("/servlet"));
        
        
        // 2. Authenticate
        
        String response = readFromServer("/servlet?name=reza&password=secret1");
        
        assertDefaultAuthenticated(
            response);
        
        // For the initial authentication, the mechanism should be called
        
        assertTrue(
            "Authentication mechanism should have been called, but wasn't", 
            response.contains("authentication mechanism called: true"));
        
        
        // 3. Request same page again within same http session, should still
        //    be authenticated
        
        response = readFromServer("/servlet");
        
        assertDefaultAuthenticated(
            response);
        
        // For the subsequent authentication, the mechanism should NOT be called
        // (the session interceptor takes care of authentication now)
        
        assertTrue(
            "Authentication mechanism should have been called, but wasn't", 
            response.contains("authentication mechanism called: false"));
        
        
        // 4. Logout. Should not be authenticated anymore
        
        assertDefaultNotAuthenticated(
            readFromServer("/servlet?logout=true"));
        
        
        // 5. Request same page again, should still not be authenticated
        
        assertDefaultNotAuthenticated(
            readFromServer("/servlet"));
       
    }

}
