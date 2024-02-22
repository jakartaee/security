/*
 * Copyright (c) 2024 Contributors to Eclipse Foundation.
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.security.enterprise.authentication.mechanism.http;

import static jakarta.security.enterprise.AuthenticationStatus.SUCCESS;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * <code>HttpAuthenticationMechanismHandler</code> is a mechanism for obtaining a caller's credentials in some way,
 * using the HTTP protocol where necessary, by consulting a set of one or more {@link HttpAuthenticationMechanism}s.
 *
 * <p>
 * This is a special variant of an {@link HttpAuthenticationMechanism} intended for coordination
 * between multiple {@link HttpAuthenticationMechanism}s. Implementations are therefore expected and
 * encouraged to delegate actually obtaining the caller's credential to an actual {@link HttpAuthenticationMechanism}.
 * This is however <b>not</b> required and implementations can do as they choose.
 *
 * <p>
 * Implementations of Jakarta Security <b>must</b> supply a default implementation of the
 * {@code HttpAuthenticationMechanismHandler}. This implementation must be {@link ApplicationScoped} and this implementation
 * <b>must</b> behave as described below:
 *
 * <ol>
 *   <li> Before servicing any calls as defined by this interface, the implementation must check if there is more than
 *        one enabled bean of type {@code HttpAuthenticationMechanism} available.
 *   </li>
 *   <li> If there is more than one enabled bean of type {@code HttpAuthenticationMechanism} available, the implementation
 *        must throw an {@link IllegalStateException}.
 *   </li>
 *   <li>
 *       If there is one enabled bean of type {@code HttpAuthenticationMechanism} available, the implementation
 *       must remember this one enabled bean.
 *   </li>
 *   <li>
 *       When servicing any calls as defined by this interface, the implementation must call the method in the
 *       {@code HttpAuthenticationMechanism} bean with the same name and arguments, and where applicable return
 *       the result from that call.
 *   </li>
 *  </ol>
 *
 * <p>
 * Applications do not need to supply an {@code HttpAuthenticationMechanismHandler} unless application-specific
 * behavior is desired.
 */
public interface HttpAuthenticationMechanismHandler {

    /**
     * Authenticate an HTTP request.
     *
     * <p>
     * This method is called in response to an HTTP client request for a resource, and is always invoked
     * <strong>before</strong> any {@link Filter} or {@link HttpServlet}. Additionally this method is called
     * in response to {@link HttpServletRequest#authenticate(HttpServletResponse)}
     *
     * <p>
     * Note that by default this method is <strong>always</strong> called for every request, independent of whether
     * the request is to a protected or non-protected resource, or whether a caller was successfully authenticated
     * before within the same HTTP session or not.
     *
     * <p>
     * A CDI/Interceptor spec interceptor can be used to prevent calls to this method if needed.
     * See {@link AutoApplySession} and {@link RememberMe} for two examples.
     *
     * @param request contains the request the client has made
     * @param response contains the response that will be send to the client
     * @param httpMessageContext context for interacting with the container
     * @return the completion status of the processing performed by this method
     * @throws AuthenticationException when the processing failed
     */
    AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException;

    /**
     * Secure the response, optionally.
     *
     * <p>
     * This method is called to allow for any post processing to be done on the request, and is always invoked
     * <strong>after</strong> any {@link Filter} or {@link HttpServlet}.
     *
     * <p>
     * Note that this method is only called when a (Servlet) resource has indeed been invoked, i.e. if a previous call
     * to <code>validateRequest</code> that was invoked before any {@link Filter} or {@link HttpServlet} returned SUCCESS.
     *
     * @param request contains the request the client has made
     * @param response contains the response that will be send to the client
     * @param httpMessageContext context for interacting with the container
     * @return the completion status of the processing performed by this method
     * @throws AuthenticationException when the processing failed
     */
    default AuthenticationStatus secureResponse(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        return SUCCESS;
    }

    /**
     * Remove mechanism specific principals and credentials from the subject and any other state the mechanism
     * might have used.
     *
     * <p>
     * This method is called in response to {@link HttpServletRequest#logout()} and gives the authentication mechanism
     * the option to remove any state associated with an earlier established authenticated identity. For example, an
     * authentication mechanism that stores state within a cookie can send remove that cookie here.
     *
     * @param request contains the request the client has made
     * @param response contains the response that will be send to the client
     * @param httpMessageContext context for interacting with the container
     */
    default void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        httpMessageContext.cleanClientSubject();
    }

}
