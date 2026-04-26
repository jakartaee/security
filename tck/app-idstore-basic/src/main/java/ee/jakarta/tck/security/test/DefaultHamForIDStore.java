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

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

import java.io.IOException;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HAM that validates the user/pwd query parameters against the
 * {@link IdentityStoreHandler} and emits the resulting status, group set and
 * caller DN to the response so the IT can assert the wiring of the default
 * IdentityStoreHandler / IdentityStore.
 */
@ApplicationScoped
public class DefaultHamForIDStore implements HttpAuthenticationMechanism {

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {

        IdentityStoreHandler identityStoreHandler = CDI.current().select(IdentityStoreHandler.class).get();

        String username = request.getParameter("user");
        String password = request.getParameter("pwd");

        try {
            CredentialValidationResult result = identityStoreHandler.validate(
                    new UsernamePasswordCredential(username, new Password(password)));

            Set<String> groups = result.getCallerGroups();
            String callerDn = result.getCallerDn();

            response.getWriter().append("ValidateResultStatus=" + result.getStatus().toString() + "\n");
            response.getWriter().append("ValidateResultGroups=" + groups + "\n");
            response.getWriter().append("ValidateCallerDN=" + callerDn + "\n");

            // Always 200 so the IT can read the response body even on failure.
            response.setStatus(200);

            if (result.getStatus() == VALID) {
                return httpMessageContext.notifyContainerAboutLogin(result);
            }
            return httpMessageContext.doNothing();

        } catch (Exception ex) {
            try {
                response.getWriter().append("Exception received.\n");
                response.getWriter().append("Exception message: " + ex.getMessage() + "\n");
            } catch (IOException ignore) {
            }
        }

        return httpMessageContext.doNothing();
    }

}
