/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
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

import static ee.jakarta.tck.security.test.Assert.assertDefaultAccess;
import static ee.jakarta.tck.security.test.Assert.assertDefaultNoAccess;
import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class AppMemPolicy2IT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    // Test several general conditions to make sure security
    // works in the normal way

    /**
     * Normally authenticated for a request to the default path.
     * Should not have access, since not in role "kaz"
     */
    @Test
    public void testAuthenticated() {
        assertDefaultNoAccess(
            readFromServer("/protectedServlet?name=reza&password=secret1"));
    }

    /**
     * Not authenticated on the default path.
     * Should not have access, since not in the required role "kaz"
     */
    @Test
    public void testNotAuthenticated() {
        assertDefaultNoAccess(
            readFromServer("/protectedServlet"));
    }

    /**
     * Wrongly authenticated on the default path.
     * Should not have access, since not in the required role "kaz"
     */
    @Test
    public void testNotAuthenticatedWrongName() {
        assertDefaultNoAccess(
            readFromServer("/protectedServlet?name=romo&password=secret1"));
    }


    // Test on the special test path which a custom policy is observing

    /**
     * Should not have access, since the custom policy checks for any authenticated caller,
     * and we don't authenticate here.
     *
     */
    @Test
    public void testNotAuthenticatedSpecial() {
        String response = readFromServer("/protectedServlet/foo/test");

        assertDefaultNoAccess(response);
    }

    /**
     * Should have access, since the custom policy checks for any authenticated caller,
     * and we authenticate here.
     *
     */
    @Test
    public void testAuthenticatedSpecial() {
        String response = readFromServer("/protectedServlet/foo/test?name=reza&password=secret1");

        assertDefaultAccess(response);
    }

}
