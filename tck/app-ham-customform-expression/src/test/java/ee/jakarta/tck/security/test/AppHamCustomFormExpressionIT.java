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

import org.htmlunit.html.HtmlPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppHamCustomFormExpressionIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/customform/expression#testLoginToContinueuseForwardToLoginExpression.
     *
     * The annotation declares {@code useForwardToLogin = true} and
     * {@code useForwardToLoginExpression = "${false}"}; the EL expression
     * must take precedence over the static value, so the unauthenticated
     * caller is redirected (URL changes) to /login.jsf rather than forwarded.
     */
    @Test
    public void testLoginToContinueUseForwardToLoginExpression() {
        HtmlPage loginPage = pageFromServer("/servlet");

        assertTrue("Expected the login page content.\n" + loginPage.getWebResponse().getContentAsString(),
                loginPage.getWebResponse().getContentAsString().contains("Login"));

        assertTrue("Expected the URL to reflect a redirect to /login.jsf (EL expression must override the static useForwardToLogin = true).\n"
                + loginPage.getUrl().toString(),
                loginPage.getUrl().toString().contains("/login.jsf"));
    }

}
