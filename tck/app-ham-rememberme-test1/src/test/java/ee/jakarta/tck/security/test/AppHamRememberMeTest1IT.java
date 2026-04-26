/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.htmlunit.WebResponse;
import org.htmlunit.util.NameValuePair;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppHamRememberMeTest1IT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/rememberme/test1#testRememberMeCookieNameandisRememberMeExpression:
     * verifies isRememberMeExpression overrides the static isRememberMe = false.
     * Without rememberme=true the HAM is invoked every request; with
     * rememberme=true the cookie is set, and the next request in the same
     * session is served from the RememberMe store without invoking the HAM.
     */
    @Test
    public void testRememberMeCookieNameandisRememberMeExpression() {
        // 1. Unauthenticated -> 401
        assertEquals(401, responseFromServer("/servlet").getStatusCode());

        // 2. Authenticate without rememberme -> HAM is invoked
        String authedNoRemember = readFromServer("/servlet?name=reza&password=secret1");
        assertExpectedAuthenticatedContent(authedNoRemember);
        assertTrue("Expected HAM to have been invoked.\n" + authedNoRemember,
                authedNoRemember.contains("HAM authentication mechanism called: true"));

        // 3. Without remember-me cookie, anonymous request -> 401
        assertEquals(401, responseFromServer("/servlet").getStatusCode());

        // 4. Authenticate with rememberme=true -> cookie set, expected name
        WebResponse rememberResponse = responseFromServer("/servlet?name=reza&password=secret1&rememberme=true");
        String rememberContent = rememberResponse.getContentAsString();
        assertExpectedAuthenticatedContent(rememberContent);
        assertCookieIsSetWithName(rememberResponse, "JSR375COOKIENAME");
        assertTrue("Expected HAM to have been invoked on the rememberme login.\n" + rememberContent,
                rememberContent.contains("HAM authentication mechanism called: true"));

        // 5. Subsequent request with no credentials -> served by RememberMe, HAM not invoked
        String reaccess = readFromServer("/servlet");
        assertExpectedAuthenticatedContent(reaccess);
        assertTrue("Expected HAM to NOT have been invoked when remember-me cookie is honoured.\n" + reaccess,
                reaccess.contains("HAM authentication mechanism called: false"));

        // 6. Logout
        readFromServer("/servlet?logout=true");

        // 7. Unauthenticated -> 401
        assertEquals(401, responseFromServer("/servlet").getStatusCode());
    }

    /**
     * Migrated from old-tck ham/rememberme/test1#testRememberMecookieMaxAgeSeconds:
     * the cookie is configured with cookieMaxAgeSeconds = 15; after that, the
     * caller is no longer remembered.
     *
     * Note: this test sleeps 16s by design, mirroring the original.
     */
    @Test
    public void testRememberMecookieMaxAgeSeconds() throws InterruptedException {
        // 1. Unauthenticated -> 401
        assertEquals(401, responseFromServer("/servlet").getStatusCode());

        // 2. Authenticate with rememberme
        String authed = readFromServer("/servlet?name=reza&password=secret1&rememberme=true");
        assertExpectedAuthenticatedContent(authed);

        // 3. Subsequent request -> still authenticated via cookie
        assertExpectedAuthenticatedContent(readFromServer("/servlet"));

        // 4. Wait past cookie expiry
        Thread.sleep(16000);

        // 5. Cookie expired -> 401
        assertEquals(401, responseFromServer("/servlet").getStatusCode());
    }

    /**
     * Migrated from old-tck ham/rememberme/test1#testRememberMecookieHttpOnly:
     * the @RememberMe defaults cookieHttpOnly = true, so the Set-Cookie header
     * must include HttpOnly.
     */
    @Test
    public void testRememberMecookieHttpOnly() {
        WebResponse response = responseFromServer("/servlet?name=reza&password=secret1&rememberme=true");
        assertExpectedAuthenticatedContent(response.getContentAsString());

        assertCookieHasAttribute(response, "JSR375COOKIENAME", "HttpOnly", true);
    }

    private void assertExpectedAuthenticatedContent(String response) {
        assertTrue("Expected user principal to be reza.\n" + response,
                response.contains("The user principal is: reza"));
        assertTrue(response.contains("isUserInRole(\"foo\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"bar\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"kaz\"): !false!"));
    }

    private static void assertCookieIsSetWithName(WebResponse response, String cookieName) {
        boolean found = false;
        for (NameValuePair header : response.getResponseHeaders()) {
            if ("Set-Cookie".equalsIgnoreCase(header.getName())
                    && header.getValue().contains(cookieName)) {
                found = true;
                break;
            }
        }
        assertTrue("Expected Set-Cookie header for " + cookieName, found);
    }

    private static void assertCookieHasAttribute(WebResponse response, String cookieName, String attribute, boolean expectPresent) {
        boolean found = false;
        for (NameValuePair header : response.getResponseHeaders()) {
            if ("Set-Cookie".equalsIgnoreCase(header.getName())
                    && header.getValue().toLowerCase().contains(cookieName.toLowerCase())
                    && header.getValue().toLowerCase().contains(attribute.toLowerCase())) {
                found = true;
                break;
            }
        }
        if (expectPresent) {
            assertTrue("Expected " + cookieName + " cookie to carry " + attribute + " attribute.", found);
        } else {
            assertFalse("Did not expect " + cookieName + " cookie to carry " + attribute + " attribute.", found);
        }
    }

}
