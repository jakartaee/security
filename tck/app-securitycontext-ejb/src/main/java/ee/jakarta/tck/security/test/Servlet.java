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

import static ee.jakarta.tck.security.test.Utils.notNull;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Test Servlet that programmatically authenticates from within an EJB and then
 * queries SecurityContext (via the EJB) for the caller name and role membership.
 */
@DeclareRoles({ "foo", "bar", "kaz" })
@WebServlet("/servlet")
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private TestEJB testEJB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter writer = response.getWriter();

        String username = request.getParameter("name");
        String password = request.getParameter("password");

        if (notNull(username, password)) {

            AuthenticationStatus status = testEJB.authenticate(
                    request, response,
                    withParams().credential(new UsernamePasswordCredential(username, new Password(password))));

            writer.write("Authenticated with status: " + status.name() + "\n");

            if (status == AuthenticationStatus.SUCCESS) {
                writer.write("Authentication successed\n");
            } else if (status == AuthenticationStatus.SEND_FAILURE) {
                writer.write("Authentication failed\n");
                return;
            }
        }

        String contextName = null;
        if (testEJB.getCallerPrincipal() != null) {
            contextName = testEJB.getCallerPrincipal().getName();
        }

        writer.write("context username: " + contextName + "\n");

        writer.write("context user has role \"foo\": " + testEJB.isCallerInRole("foo") + "\n");
        writer.write("context user has role \"bar\": " + testEJB.isCallerInRole("bar") + "\n");
        writer.write("context user has role \"kaz\": " + testEJB.isCallerInRole("kaz") + "\n");
    }

}
