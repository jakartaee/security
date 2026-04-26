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
import static org.junit.Assert.assertTrue;

import org.htmlunit.WebResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppHamAutoApplySessionIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/autoapplysession#testAutoApplySession.
     *
     * Verifies the {@code @AutoApplySession} interceptor on the HAM:
     * <ol>
     *   <li>Unauthenticated request to a protected URL yields HTTP 401.
     *   <li>Authenticating once with {@code ?name=&password=} succeeds and
     *       leaves the caller principal + roles populated.
     *   <li>A subsequent request in the same HTTP session is served as the
     *       same caller without re-supplying credentials.
     *   <li>{@code ?logout=true} clears the session.
     *   <li>Following the logout, an unauthenticated request again yields 401.
     * </ol>
     */
    @Test
    public void testAutoApplySession() {
        // 1. Unauthenticated -> 401
        WebResponse first = responseFromServer("/servlet");
        assertEquals(401, first.getStatusCode());

        // 2. Authenticate
        String authedResponse = readFromServer("/servlet?name=reza&password=secret1");
        assertExpectedAuthenticatedContent(authedResponse);

        // 3. Same session, no credentials -> still authenticated thanks to AutoApplySession
        String reaccess = readFromServer("/servlet");
        assertExpectedAuthenticatedContent(reaccess);

        // 4. Logout
        readFromServer("/servlet?logout=true");

        // 5. Unauthenticated again -> 401
        WebResponse afterLogout = responseFromServer("/servlet");
        assertEquals(401, afterLogout.getStatusCode());
    }

    private void assertExpectedAuthenticatedContent(String response) {
        assertTrue("Expected user principal to be reza.\n" + response,
                response.contains("The user principal is: reza"));
        assertTrue(response.contains("isUserInRole(\"foo\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"bar\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"kaz\"): !false!"));
    }

}
