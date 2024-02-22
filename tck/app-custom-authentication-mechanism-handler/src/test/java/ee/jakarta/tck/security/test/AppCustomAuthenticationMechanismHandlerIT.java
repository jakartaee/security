/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
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
import static ee.jakarta.tck.security.test.Assert.assertDefaultNoAccess;
import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;

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
 * In this test, a mechanism for path {@code /protectedServlet1} uses request parameters {@code username} and
 * {@code secret} to take the credentials from, while for all other paths {@code name} and
 * {@code password} is used.
 */
@RunWith(Arquillian.class)
public class AppCustomAuthenticationMechanismHandlerIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Test that for path {@code /protectedServlet1} in effect {@link TestAuthenticationMechanism1} is used.
     */
    @Test
    public void testAuthenticated1() {
        assertDefaultAuthenticated(
            readFromServer("/protectedServlet1?username=reza&secret=secret1"));
    }

    /**
     * Test that for path {@code /protectedServlet2} in effect {@link TestAuthenticationMechanism2} is used.
     */
    @Test
    public void testAuthenticated2() {
        assertDefaultAuthenticated(
            readFromServer("/protectedServlet2?name=reza&password=secret1"));
    }

    /**
     * Test that for path {@code /protectedServlet1} in effect {@link TestAuthenticationMechanism2} is not used.
     */
    @Test
    public void testNotAuthenticated1() {
        assertDefaultNoAccess(
            readFromServer("/protectedServlet1?name=reza&password=secret1"));
    }

    /**
     * Test that for path {@code /protectedServlet2} in effect {@link TestAuthenticationMechanism1} is not used.
     */
    @Test
    public void testNotAuthenticated2() {
        assertDefaultNoAccess(
            readFromServer("/protectedServlet2?username=reza&secret=secret1"));
    }

}
