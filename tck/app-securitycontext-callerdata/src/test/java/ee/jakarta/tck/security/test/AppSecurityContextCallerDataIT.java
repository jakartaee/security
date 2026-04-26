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
import static org.junit.Assert.assertTrue;

import org.htmlunit.DefaultCredentialsProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppSecurityContextCallerDataIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck securitycontext/callerdata#testSecurityContextIsCallerInRole:
     * verifies that {@code SecurityContext.isCallerInRole} agrees with
     * {@code HttpServletRequest.isUserInRole} for every declared role.
     */
    @Test
    public void testSecurityContextIsCallerInRole() {
        authenticate("reza", "secret1");

        String response = readFromServer("/servlet");

        assertTrue(
            "Response should report context username reza.\n" + response,
            response.contains("context username: reza"));

        assertTrue(response.contains("context user has role \"foo\": true"));
        assertTrue(response.contains("context user has role \"bar\": true"));
        assertTrue(response.contains("context user has role \"kaz\": false"));

        assertTrue(response.contains("isCallerInRole(foo) result same with request.isUserInRole(foo) result : true"));
        assertTrue(response.contains("isCallerInRole(bar) result same with request.isUserInRole(bar) result : true"));
        assertTrue(response.contains("isCallerInRole(kaz) result same with request.isUserInRole(kaz) result : true"));
    }

    /**
     * Migrated from old-tck securitycontext/callerdata#testSecurityContextHasAccessToWebResource:
     * verifies {@code hasAccessToWebResource} reports the expected GET / POST
     * accessibility per caller role.
     *
     * /protectedServlet has a default constraint requiring "bar" with GET
     * unprotected, so:
     * <ul>
     *   <li>reza (foo, bar) — GET allowed, POST allowed
     *   <li>bob  (foo)      — GET allowed, POST denied
     * </ul>
     */
    @Test
    public void testSecurityContextHasAccessToWebResource_rezaHasAllAccess() {
        authenticate("reza", "secret1");

        String response = readFromServer("/servlet2");

        assertTrue(response.contains("context username: reza"));
        assertTrue(response.contains("has GET access to /protectedServlet: true"));
        assertTrue(response.contains("has POST access to /protectedServlet: true"));
    }

    @Test
    public void testSecurityContextHasAccessToWebResource_bobHasOnlyGetAccess() {
        authenticate("bob", "secret3");

        String response = readFromServer("/servlet2");

        assertTrue(response.contains("context username: bob"));
        assertTrue(response.contains("has GET access to /protectedServlet: true"));
        assertTrue(response.contains("has POST access to /protectedServlet: false"));
    }

    private void authenticate(String username, String password) {
        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials(username, password.toCharArray());
        getWebClient().setCredentialsProvider(credentialsProvider);
    }

}
