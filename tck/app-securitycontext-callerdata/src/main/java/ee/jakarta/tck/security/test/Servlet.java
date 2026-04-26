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

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Authenticated servlet that asserts {@code SecurityContext.isCallerInRole}
 * agrees with {@code HttpServletRequest.isUserInRole} for the declared roles.
 */
@BasicAuthenticationMechanismDefinition(realmName = "test realm")
@DeclareRoles({ "foo", "bar", "kaz" })
@WebServlet("/servlet")
@ServletSecurity(@HttpConstraint(rolesAllowed = "foo"))
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private SecurityContext securityContext;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter writer = response.getWriter();

        writer.write("This is a servlet \n");

        String contextName = null;
        if (securityContext.getCallerPrincipal() != null) {
            contextName = securityContext.getCallerPrincipal().getName();
        }

        writer.write("context username: " + contextName + "\n");

        writer.write("context user has role \"foo\": " + securityContext.isCallerInRole("foo") + "\n");
        writer.write("context user has role \"bar\": " + securityContext.isCallerInRole("bar") + "\n");
        writer.write("context user has role \"kaz\": " + securityContext.isCallerInRole("kaz") + "\n");

        writer.write("isCallerInRole(foo) result same with request.isUserInRole(foo) result : "
                + (securityContext.isCallerInRole("foo") == request.isUserInRole("foo")) + "\n");
        writer.write("isCallerInRole(bar) result same with request.isUserInRole(bar) result : "
                + (securityContext.isCallerInRole("bar") == request.isUserInRole("bar")) + "\n");
        writer.write("isCallerInRole(kaz) result same with request.isUserInRole(kaz) result : "
                + (securityContext.isCallerInRole("kaz") == request.isUserInRole("kaz")) + "\n");
    }

}
