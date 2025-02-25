/*
 * Copyright (c) 2024, 2025 Contributors to the Eclipse Foundation.
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

import static ee.jakarta.tck.security.test.Assert.assertDefaultAuthenticated;
import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.htmlunit.DefaultCredentialsProvider;
import org.htmlunit.WebResponse;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanismHandler;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * This test asserts that an application provided {@link HttpAuthenticationMechanismHandler} can override
 * the default one by allowing multiple authentication mechanisms to co-exists.
 *
 * <p>
 * In this test, three instances of the build-in Basic HTTP authentication mechanism are used. The custom
 * handler invokes each one depending on the request URI that was used.
 */
@RunWith(Arquillian.class)
public class AppCustomAuthenticationMechanismHandler2IT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Test that for path {@code /protectedServlet1} in effect {@link TestAuthenticationMechanism1} is used.
     */
    @Test
    public void testAuthenticated1() {
        WebResponse response = responseFromServer("/protectedServlet1");

        assertEquals(401, response.getStatusCode());

        assertTrue(
            "Response did not contain the \"WWW-Authenticate\" header, but should have",
            response.getResponseHeaderValue("WWW-Authenticate") != null);


        // Most important part of the test: check that we have the correct authentication mechanism instance used

        assertTrue(
            "Response did not contain \"realm1\" in the \"WWW-Authenticate\" header value, but should have",
            response.getResponseHeaderValue("WWW-Authenticate").contains("realm1"));


        // For completion, check that authentication mechanism also actually authenticates

        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials("reza", "secret1".toCharArray());

        getWebClient().setCredentialsProvider(credentialsProvider);

        assertDefaultAuthenticated(
            readFromServer("/protectedServlet1"));
    }

    /**
     * Test that for path {@code /protectedServlet2} in effect {@link TestAuthenticationMechanism2} is used.
     */
    @Test
    public void testAuthenticated2() {
        WebResponse response = responseFromServer("/protectedServlet2");

        assertEquals(401, response.getStatusCode());

        assertTrue(
            "Response did not contain the \"WWW-Authenticate\" header, but should have",
            response.getResponseHeaderValue("WWW-Authenticate") != null);


        // Most important part of the test: check that we have the correct authentication mechanism instance used

        assertTrue(
                "Response did not contain \"realm1\" in the \"WWW-Authenticate\" header value, but should have",
                response.getResponseHeaderValue("WWW-Authenticate").contains("realm2"));


        // For completion, check that authentication mechanism also actually authenticates

        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials("reza", "secret1".toCharArray());

        getWebClient().setCredentialsProvider(credentialsProvider);

        assertDefaultAuthenticated(
            readFromServer("/protectedServlet2"));
    }

    /**
     * Test that for path {@code /protectedServlet3} in effect {@link TestAuthenticationMechanism3} is used.
     */
    @Test
    public void testAuthenticated3() {
        WebResponse response = responseFromServer("/protectedServlet3");

        assertEquals(401, response.getStatusCode());

        assertTrue(
            "Response did not contain the \"WWW-Authenticate\" header, but should have",
            response.getResponseHeaderValue("WWW-Authenticate") != null);


        // Most important part of the test: check that we have the correct authentication mechanism instance used

        assertTrue(
                "Response did not contain \"realm3\" in the \"WWW-Authenticate\" header value, but should have",
                response.getResponseHeaderValue("WWW-Authenticate").contains("realm3"));


        // For completion, check that authentication mechanism also actually authenticates

        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials("reza", "secret1".toCharArray());

        getWebClient().setCredentialsProvider(credentialsProvider);

        assertDefaultAuthenticated(
            readFromServer("/protectedServlet3"));
    }

}
