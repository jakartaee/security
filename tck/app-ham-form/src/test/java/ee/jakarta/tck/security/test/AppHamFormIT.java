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

import java.io.IOException;

import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppHamFormIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/form#testFormHAMValidateRequest.
     */
    @Test
    public void testFormHAMValidateRequest() throws IOException {
        HtmlPage loginPage = pageFromServer("/servlet");

        HtmlForm form = loginPage.getForms().get(0);
        form.getInputByName("j_username").setValueAttribute("reza");
        form.getInputByName("j_password").setValueAttribute("secret1");

        HtmlPage page = form.getInputByValue("Submit").click();
        String response = page.getWebResponse().getContentAsString();

        assertTrue("Expected user principal to be reza.\n" + response,
                response.contains("The user principal is: reza"));
        assertTrue("Expected getRemoteUser() to return reza.\n" + response,
                response.contains("getRemoteUser(): reza"));
        assertTrue(response.contains("isUserInRole(\"foo\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"bar\"): !true!"));
        assertTrue(response.contains("isUserInRole(\"kaz\"): !false!"));

        // Re-access the protected resource — the FORM session remembers the caller.
        String reaccess = readFromServer("/servlet");
        assertTrue("Expected to remain authenticated as reza on subsequent request.\n" + reaccess,
                reaccess.contains("The user principal is: reza"));
    }

    /**
     * Migrated from old-tck ham/form#testLoginToContinueLoginPage:
     * accessing the protected resource forwards to the configured login page.
     */
    @Test
    public void testLoginToContinueLoginPage() {
        String fromProtected = readFromServer("/servlet");
        String fromLogin = readFromServer("/form-login-servlet");

        assertTrue("Login page rendered on protected access should match the login page rendered directly.\n"
                + "from /servlet:\n" + fromProtected + "\n\nfrom /form-login-servlet:\n" + fromLogin,
                fromProtected.equals(fromLogin));
    }

    /**
     * Migrated from old-tck ham/form#testLoginToContinueerrorPage:
     * submitting a wrong password forwards to the configured error page and
     * leaves the caller unauthenticated.
     */
    @Test
    public void testLoginToContinueErrorPage() throws IOException {
        HtmlPage loginPage = pageFromServer("/servlet");

        HtmlForm form = loginPage.getForms().get(0);
        form.getInputByName("j_username").setValueAttribute("reza");
        form.getInputByName("j_password").setValueAttribute("wrongpassword");

        HtmlPage page = form.getInputByValue("Submit").click();
        String response = page.getWebResponse().getContentAsString();

        assertTrue("Expected the user principal to be null on the error page.\n" + response,
                response.contains("The user principal is: null"));
    }

    /**
     * Migrated from old-tck ham/form#testFormHAMHasCorrectQualifier:
     * the {@code @FormAuthenticationMechanismDefinition} annotation must
     * register an {@code HttpAuthenticationMechanism} bean qualified by
     * {@code @FormAuthenticationMechanism} with {@code @ApplicationScoped}.
     */
    @Test
    public void testFormHAMHasCorrectQualifier() throws IOException {
        HtmlPage loginPage = pageFromServer("/servlet2");

        HtmlForm form = loginPage.getForms().get(0);
        form.getInputByName("j_username").setValueAttribute("reza");
        form.getInputByName("j_password").setValueAttribute("secret1");

        HtmlPage page = form.getInputByValue("Submit").click();
        String response = page.getWebResponse().getContentAsString();

        assertTrue("Expected the FORM HAM bean to carry @FormAuthenticationMechanism.\n" + response,
                response.contains("Have qualifier @FormAuthenticationMechanism: true"));
        assertTrue("Expected the FORM HAM bean to be @ApplicationScoped.\n" + response,
                response.contains("Have scope @ApplicationScoped: true"));
    }

}
