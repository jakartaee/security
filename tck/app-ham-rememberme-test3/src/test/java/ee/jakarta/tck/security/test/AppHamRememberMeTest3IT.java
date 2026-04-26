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
public class AppHamRememberMeTest3IT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/rememberme/test3#testRememberMeisRememberMe:
     * isRememberMe = true (constant) — every login persists across the
     * session via the cookie, until logout.
     */
    @Test
    public void testRememberMeisRememberMe() {
        assertEquals(401, responseFromServer("/servlet").getStatusCode());

        String authed = readFromServer("/servlet?name=reza&password=secret1&secureonly=false");
        assertExpectedAuthenticatedContent(authed);

        // Re-access -> still authenticated via cookie
        assertExpectedAuthenticatedContent(readFromServer("/servlet"));

        readFromServer("/servlet?logout=true");

        assertEquals(401, responseFromServer("/servlet").getStatusCode());
    }

    /**
     * Migrated from old-tck ham/rememberme/test3#testRememberMecookieMaxAgeSecondsExpression:
     * cookiemaxage=15 query param flows through cookieMaxAgeSecondsExpression,
     * overriding the static 86400 default; after 16s the cookie is expired
     * and the caller is no longer remembered.
     */
    @Test
    public void testRememberMecookieMaxAgeSecondsExpression() throws InterruptedException {
        assertEquals(401, responseFromServer("/servlet").getStatusCode());

        String authed = readFromServer("/servlet?name=reza&password=secret1&cookiemaxage=15&secureonly=false");
        assertExpectedAuthenticatedContent(authed);

        assertExpectedAuthenticatedContent(readFromServer("/servlet"));

        Thread.sleep(16000);

        assertEquals(401, responseFromServer("/servlet").getStatusCode());
    }

    /**
     * Migrated from old-tck ham/rememberme/test3#testRememberMecookieHttpOnlyExpression:
     * httponly=false flows through cookieHttpOnlyExpression — Set-Cookie must
     * NOT carry HttpOnly even though the static default is true.
     */
    @Test
    public void testRememberMecookieHttpOnlyExpression() {
        WebResponse response = responseFromServer("/servlet?name=reza&password=secret1&cookiemaxage=5&httponly=false");
        assertExpectedAuthenticatedContent(response.getContentAsString());
        assertCookieHasAttribute(response, "JSR375COOKIENAME", "HttpOnly", false);
    }

    /**
     * Migrated from old-tck ham/rememberme/test3#testRememberMecookieSecureOnlyExpression:
     * secureonly=false flows through cookieSecureOnlyExpression — Set-Cookie
     * must NOT carry Secure even though the static default is true.
     */
    @Test
    public void testRememberMecookieSecureOnlyExpression() {
        WebResponse response = responseFromServer("/servlet?name=reza&password=secret1&cookiemaxage=5&secureonly=false");
        assertExpectedAuthenticatedContent(response.getContentAsString());
        assertCookieHasAttribute(response, "JSR375COOKIENAME", "secure", false);
    }

    private void assertExpectedAuthenticatedContent(String response) {
        assertTrue("Expected user principal to be reza.\n" + response,
                response.contains("The user principal is: reza"));
        assertTrue(response.contains("isUserInRole(\"foo\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"bar\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"kaz\"): !false!"));
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
