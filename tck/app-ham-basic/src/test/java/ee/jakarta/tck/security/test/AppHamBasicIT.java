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

import org.htmlunit.DefaultCredentialsProvider;
import org.htmlunit.WebResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppHamBasicIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/basic#testBasicHAMValidateRequest:
     * BASIC auth with the correct credentials results in a successful caller
     * principal and the expected role membership.
     */
    @Test
    public void testBasicHAMValidateRequest() {
        authenticate("reza", "secret1");

        String response = readFromServer("/servlet");

        assertTrue(response.contains("web username: reza"));
        assertTrue(response.contains("web user has role \"foo\": true"));
        assertTrue(response.contains("web user has role \"bar\": true"));
        assertTrue(response.contains("web user has role \"kaz\": false"));
    }

    /**
     * Migrated from old-tck ham/basic#testBasicHAMValidateRequest_wrongPassword:
     * BASIC auth with the wrong password is rejected with a 401 status.
     */
    @Test
    public void testBasicHAMValidateRequest_wrongPassword() {
        authenticate("reza", "wrongpwd");

        WebResponse response = responseFromServer("/servlet");

        assertEquals(401, response.getStatusCode());
    }

    /**
     * Migrated from old-tck ham/basic#testBasicHAMHasCorrectQualifier:
     * the {@code @BasicAuthenticationMechanismDefinition} annotation must
     * register an {@code HttpAuthenticationMechanism} bean qualified by
     * {@code @BasicAuthenticationMechanism} with {@code @ApplicationScoped}.
     */
    @Test
    public void testBasicHAMHasCorrectQualifier() {
        authenticate("reza", "secret1");

        String response = readFromServer("/servlet2");

        assertTrue(
            "Response should report the BASIC HAM bean has @BasicAuthenticationMechanism qualifier.\n" + response,
            response.contains("Have qualifier @BasicAuthenticationMechanism: true"));

        assertTrue(
            "Response should report the BASIC HAM bean has @ApplicationScoped scope.\n" + response,
            response.contains("Have scope @ApplicationScoped: true"));
    }

    private void authenticate(String username, String password) {
        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials(username, password.toCharArray());
        getWebClient().setCredentialsProvider(credentialsProvider);
    }

}
