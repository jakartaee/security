/*
 * Copyright (c) 2018 Payara Services and/or its affiliates and others.
 * All rights reserved.
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
package org.glassfish.security;

import static org.junit.Assert.assertEquals;

import javax.enterprise.util.AnnotationLiteral;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;

import org.junit.Test;

public class AnnotationLiteralTest {


    @Test
    public void testAutoApplySession() {
        AutoApplySession literal = AutoApplySession.Literal.INSTANCE;

        assertEquals(new AnnotationLiteral<AutoApplySession>() {
            private static final long serialVersionUID = 1L;
        },  literal);

    }

    @Test
    public void testRememberMeDefault() {
        RememberMe literal = RememberMe.Literal.of().build();

        assertEquals(86400, literal.cookieMaxAgeSeconds());
        assertEquals("JREMEMBERMEID", literal.cookieName());
    }

    @Test
    public void testRememberMe() {
        RememberMe literal = RememberMe.Literal.of()
                                       .cookieMaxAgeSeconds(100)
                                       .cookieSecureOnly(false)
                                       .cookieHttpOnly(false)
                                       .build();

        assertEquals(100, literal.cookieMaxAgeSeconds());
        assertEquals(false, literal.cookieSecureOnly());
        assertEquals(false, literal.cookieHttpOnly());
        assertEquals("JREMEMBERMEID", literal.cookieName());
    }

    @Test
    public void testLoginToContinueDefault() {
        LoginToContinue literal = LoginToContinue.Literal.of().build();

        assertEquals("/login", literal.loginPage());
        assertEquals("/login-error", literal.errorPage());
    }

    @Test
    public void testLoginToContinue() {
        LoginToContinue literal = LoginToContinue.Literal.of()
                                                 .loginPage("/mylogin")
                                                 .useForwardToLogin(false)
                                                 .useForwardToLoginExpression("#{bar.foo}")
                                                 .build();

        assertEquals("/mylogin", literal.loginPage());
        assertEquals(false, literal.useForwardToLogin());
        assertEquals("#{bar.foo}", literal.useForwardToLoginExpression());
        assertEquals("/login-error", literal.errorPage());
    }




}
