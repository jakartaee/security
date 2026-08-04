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

import org.htmlunit.HttpMethod;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for REST template resources of which the templates do not overlap; the protected resource
 * matches an upper case first character of the user name, the public one a lower case first
 * character. Every request therefore selects a single, known resource, and the tests can assert
 * the exact outcome.
 */
@RunWith(Arquillian.class)
public class AppRestRolesAllowedTemplateIT extends ArquillianBase {

    @ArquillianResource
    private URL base;

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

    /**
     * A HEAD request is served by the GET resource method when no explicit HEAD method is declared.
     * The security system therefore has to see the constraint of that GET method, and must not
     * conclude that no constraint applies just because no HEAD endpoint exists.
     */
    @Test
    public void testHeadNotAuthenticated() {
        WebResponse response = headResponseFromServer("/rest/protectedResource/21/users/Reza/sayHi");

        assertEquals(
            "Authentication mechanism should have seen the HEAD request for the REST template resource as protected",
            "true",
            response.getResponseHeaderValue("X-Protected"));
    }

    /**
     * The counterpart of the above; the HEAD request for an authenticated and authorized caller has
     * to be allowed through, and has to be seen as protected as well.
     */
    @Test
    public void testHeadAuthenticated() {
        WebResponse response = headResponseFromServer("/rest/protectedResource/21/users/Reza/sayHi?name=reza&password=secret1");

        assertEquals(
            "HEAD request should have been allowed, but was not",
            200,
            response.getStatusCode());

        assertEquals(
            "Authentication mechanism should have seen the HEAD request for the REST template resource as protected",
            "true",
            response.getResponseHeaderValue("X-Protected"));
    }

    /**
     * A HEAD request for the lower case template selects the public resource, which has no
     * constraint at all.
     */
    @Test
    public void testHeadLowercaseTemplatePathHitsPublicResource() {
        WebResponse response = headResponseFromServer("/rest/protectedResource/21/users/reza/sayHi");

        assertEquals(
            "Authentication mechanism should have seen the HEAD request for the REST template resource as public",
            "false",
            response.getResponseHeaderValue("X-Protected"));
    }

    @Test
    public void testCallerName() {
        String response = readFromServer("/rest/protectedResource/21/users/Reza/callerName?name=reza&password=secret1");

        assertTrue(
            "Should be authenticated as user reza but was not",
            response.contains("reza"));
    }

    @Test
    public void testNotCallerNameNotAuthenticated() {
        WebResponse response = responseFromServer("/rest/protectedResource/21/users/Reza/callerName");

        assertFalse(
            "Endpoint should not have been called, but was",
            response.getContentAsString().contains("reza"));

        assertEquals(
            "Authentication mechanism should have seen the REST template resource as protected",
            "true",
            response.getResponseHeaderValue("X-Protected"));
    }

    @Test
    public void testHasRoleFoo() {
        String response = readFromServer("/rest/protectedResource/21/users/Reza/hasRoleFoo?name=reza&password=secret1");

        assertTrue(
            "Should be in role foo, but was not",
            response.contains("true"));
    }

    @Test
    public void testNotHasRoleKaz() {
        // The class level constraint is role "foo", which reza has, so the endpoint is called.
        // Reza is not in role "kaz" though.
        String response = readFromServer("/rest/protectedResource/21/users/Reza/hasRoleKaz?name=reza&password=secret1");

        assertFalse(
            "Should not be in role kaz, but was",
            response.contains("true"));
    }

    /**
     * The public template resource has no constraints at all, so an unauthenticated caller reaches
     * the endpoint and simply has no caller principal.
     */
    @Test
    public void testLowercaseTemplateCallerNameNotAuthenticated() {
        String response = readFromServer("/rest/protectedResource/21/users/reza/callerName");

        assertFalse(
            "Should not be authenticated as user reza but was",
            response.contains("reza"));
    }


    // ### Private methods


    /**
     * Performs a HEAD request. {@link ArquillianBase#responseFromServer(String)} always does a GET,
     * and a HEAD request has to be exercised separately because it is served by the GET resource
     * method without such a method being declared.
     *
     * @param path The context relative path to request, for example {@code /rest/foo}.
     */
    private WebResponse headResponseFromServer(String path) {
        String requestPath = path;
        if (base.toString().endsWith("/") && requestPath.startsWith("/")) {
            requestPath = requestPath.substring(1);
        }

        try {
            return getWebClient().loadWebResponse(
                new WebRequest(URI.create(base + requestPath).toURL(), HttpMethod.HEAD));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}