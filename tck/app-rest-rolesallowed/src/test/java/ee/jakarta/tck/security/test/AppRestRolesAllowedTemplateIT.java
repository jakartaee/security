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

import org.htmlunit.WebResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(Arquillian.class)
public class AppRestRolesAllowedTemplateIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        try {
            return mavenWar();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void testSayHi() {
        String response = readFromServer("/rest/protectedResource/21/users/Reza/sayHi?name=reza&password=secret1");

        assertTrue(
            "Endpoint should have been called, but was not",
            response.contains("saying hi!"));

        // Test whether the authentication mechanism saw the resource as protected
        assertTrue(
            "Authentication mechanism should have seen the REST template resource as protected",
            response.contains("protected=true"));
    }

    @Test
    public void testNotSayHiNotAuthenticated() {
        WebResponse response = responseFromServer("/rest/protectedResource/21/users/Reza/sayHi");

        assertFalse(
            "Endpoint should not have been called, but was",
            response.getContentAsString().contains("saying hi!"));

        // Test whether the authentication mechanism saw the resource as protected
        assertEquals(
            "Authentication mechanism should have seen the REST template resource as protected",
            "true",
            response.getResponseHeaderValue("X-Protected"));
    }

    @Test
    public void testNotSayHiAuthenticatedNotAuthorized() {
        WebResponse response = responseFromServer("/rest/protectedResource/21/users/Reza/sayHiRoleKaz?name=reza&password=secret1");

         // User reza is authenticated and has role "foo", but the target resource method overrides
         // the class-level role with "kaz", which reza does not have.
         //
         // This also verifies that the security template matching for /users/Reza
         // does not accidentally fall through to the public lowercase /users/reza
         // template and mark the request as non-mandatory.
        assertFalse(
            "Endpoint should not have been called, but was",
            response.getContentAsString().contains("saying hi!"));

        // Test whether the authentication mechanism saw the resource as protected
        assertEquals(
            "Authentication mechanism should have seen the REST template resource as protected",
            "true",
            response.getResponseHeaderValue("X-Protected"));
    }

    @Test
    public void testLowercaseTemplatePathHitsPublicResource() {
        String response = readFromServer("/rest/protectedResource/21/users/reza/sayHi?name=reza&password=secret1");

        assertTrue(
            "Endpoint should have been called, but was not",
            response.contains("saying ho!")); // check "ho" to really make sure we're hitting the public resource

        // Test whether the authentication mechanism saw the resource as public
        assertTrue(
            "Authentication mechanism should have seen the REST template resource as public",
            response.contains("protected=false"));
    }

}
