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
public class AppHamRememberMeTest2IT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/rememberme/test2#testRememberMecookieHttpOnlyFalse:
     * @RememberMe(cookieHttpOnly = false) — Set-Cookie must NOT carry HttpOnly.
     */
    @Test
    public void testRememberMecookieHttpOnlyFalse() {
        WebResponse response = responseFromServer("/servlet?name=reza&password=secret1");
        assertExpectedAuthenticatedContent(response.getContentAsString());
        assertCookieHasAttribute(response, "JSR375COOKIENAME", "HttpOnly", false);
    }

    /**
     * Migrated from old-tck ham/rememberme/test2#testRememberMecookieSecureOnly:
     * @RememberMe(cookieSecureOnly = true) — Set-Cookie must carry Secure.
     */
    @Test
    public void testRememberMecookieSecureOnly() {
        WebResponse response = responseFromServer("/servlet?name=reza&password=secret1");
        assertExpectedAuthenticatedContent(response.getContentAsString());
        assertCookieHasAttribute(response, "JSR375COOKIENAME", "secure", true);
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
