/*
 * Copyright (c) 2018, 2022 Payara Foundation and/or its affiliates and others.
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
package ee.jakarta.tck.security.test;

import java.io.IOException;
import java.io.Serializable;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContextWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This is a CDI decorator that decorates the authentication mechanism (in this test
 * the one that is installed via the annotation on the {@link Servlet} class.
 *
 * @author Arjan Tijms
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
@Decorator
@Priority(100)
public abstract class AuthenticationMechanismDecorator implements HttpAuthenticationMechanism, Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @Delegate
    private HttpAuthenticationMechanism delagate;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        // Wrap the context, so we can set a header if the request is unauthorized.
        HttpMessageContext contextWrapper = new MessageContextWrapper(response, httpMessageContext);
        // Invoke the original authentication mechanism
        return delagate.validateRequest(request, response, contextWrapper);
    }

    private static class MessageContextWrapper extends HttpMessageContextWrapper {

        private final HttpServletResponse response;

        public MessageContextWrapper(HttpServletResponse response, HttpMessageContext httpMessageContext) {
            super(httpMessageContext);
            this.response = response;
        }

        @Override
        public AuthenticationStatus responseUnauthorized() {
            response.addHeader("foo", "bar");
            return super.responseUnauthorized();
        }

    }

}
