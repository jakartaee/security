/*
 * Copyright (c) 2018 Payara Services Limited. All rights reserved.
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
package javax.security.enterprise.authentication.mechanism.http;

import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is an implementation of the HttpAuthenticationMechanism interface that can be
 * subclassed by developers wishing to provide extra or different functionality.
 * <p>
 * All methods default to calling the wrapped object.
 *
 */
public class HttpAuthenticationMechanismWrapper implements HttpAuthenticationMechanism {

    private final HttpAuthenticationMechanism httpAuthenticationMechanism;

    public HttpAuthenticationMechanismWrapper(HttpAuthenticationMechanism httpAuthenticationMechanism) {
        this.httpAuthenticationMechanism = httpAuthenticationMechanism;
    }

    public HttpAuthenticationMechanism getWrapped() {
        return httpAuthenticationMechanism;
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
            HttpMessageContext httpMessageContext) throws AuthenticationException {
        return getWrapped().validateRequest(request, response, httpMessageContext);
    }

    @Override
    public AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response,
            HttpMessageContext httpMessageContext) throws AuthenticationException {
        return getWrapped().secureResponse(request, response, httpMessageContext);
    }

    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        getWrapped().cleanSubject(request, response, httpMessageContext);
    }

}