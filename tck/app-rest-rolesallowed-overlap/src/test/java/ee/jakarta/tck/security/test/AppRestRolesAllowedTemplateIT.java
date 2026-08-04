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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for REST template resources of which the templates overlap; both the protected and the
 * public resource match the request path used here. Which one is selected is not defined, so these
 * tests do not assert which resource is invoked, but assert that the security system and the actual
 * invocation are consistent with each other.
 */
@RunWith(Arquillian.class)
public class AppRestRolesAllowedTemplateIT extends ArquillianBase {

    private static final String OVERLAPPING_PATH = "/rest/protectedResource/21/users/Reza/sayHi";
    private static final String CREDENTIALS = "?name=reza&password=secret1";

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
        String response = readFromServer(OVERLAPPING_PATH + CREDENTIALS);

        assertTrue(
            "Endpoint should have been called, but was not",
            response.contains("saying hi!"));

        // Test whether what the authentication mechanism saw and what is actually invoked as resource is
        // consistent
        assertTrue(
            "Either public or protected resource may be called, but has to be consistent with what authentication mechanism sees.",
            response.contains("protected should be true, protected=true") ||
            response.contains("protected should be false, protected=false"));
    }

    /**
     * Without credentials the outcome depends on which of the two overlapping templates is selected;
     * the protected one denies the request, the public one serves it. Either is allowed, as long as
     * the authentication mechanism saw the same thing.
     */
    @Test
    public void testSayHiNotAuthenticated() {
        WebResponse response = responseFromServer(OVERLAPPING_PATH);
        String content = response.getContentAsString();
        String protectedHeader = response.getResponseHeaderValue("X-Protected");

        assertNotNull(
            "Authentication mechanism should have run for the request, but did not",
            protectedHeader);

        assertTrue(
            "Either public or protected resource may be selected, but the outcome has to be consistent "
                + "with what the authentication mechanism sees.",
            ("true".equals(protectedHeader) && !content.contains("saying hi!")) ||
            ("false".equals(protectedHeader) && content.contains("protected should be false, protected=false")));
    }

    /**
     * A HEAD request is served by the GET resource method when no explicit HEAD method is declared,
     * so it selects the very same resource as the GET request does. The security system therefore
     * has to see a HEAD request exactly like it sees the GET request; a HEAD request must not be
     * treated as unconstrained just because no HEAD endpoint exists.
     *
     * <p>
     * This holds no matter which of the two overlapping templates is selected, which is why the GET
     * outcome is used as the expected value rather than a fixed one.
     */
    @Test
    public void testHeadIsSeenTheSameAsGet() {
        WebResponse getResponse = responseFromServer(OVERLAPPING_PATH + CREDENTIALS);
        WebResponse headResponse = headResponseFromServer(OVERLAPPING_PATH + CREDENTIALS);

        assertNotNull(
            "Authentication mechanism should have run for the GET request, but did not",
            getResponse.getResponseHeaderValue("X-Protected"));

        assertEquals(
            "A HEAD request is served by the GET resource method, so the authentication mechanism has to "
                + "see it the same way as it sees the GET request",
            getResponse.getResponseHeaderValue("X-Protected"),
            headResponse.getResponseHeaderValue("X-Protected"));
    }

    @Test
    public void testHeadIsSeenTheSameAsGetNotAuthenticated() {
        WebResponse getResponse = responseFromServer(OVERLAPPING_PATH);
        WebResponse headResponse = headResponseFromServer(OVERLAPPING_PATH);

        assertNotNull(
            "Authentication mechanism should have run for the GET request, but did not",
            getResponse.getResponseHeaderValue("X-Protected"));

        assertEquals(
            "A HEAD request is served by the GET resource method, so the authentication mechanism has to "
                + "see it the same way as it sees the GET request",
            getResponse.getResponseHeaderValue("X-Protected"),
            headResponse.getResponseHeaderValue("X-Protected"));

        assertEquals(
            "A HEAD request has to be authorized the same way as the GET request",
            getResponse.getStatusCode(),
            headResponse.getStatusCode());
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