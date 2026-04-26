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

import jakarta.annotation.security.DeclareRoles;
import jakarta.security.enterprise.identitystore.IdentityStore.ValidationType;
import jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition.LdapSearchScope;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Test servlet that prints the authenticated caller name.
 */
@LdapIdentityStoreDefinition(
    url = "ldap://localhost:12401/",
    callerBaseDn = "ou=caller,dc=securityapi,dc=net",
    groupSearchBase = "ou=invalid,dc=securityapi,dc=net",
    groupSearchFilter = "(&(member=%s)(objectClass=groupOfNames))" 
)
@DeclareRoles({ "Administrator", "Manager", "Employee" })
@WebServlet("/ServletForLDAPIDStore")
public class ServletForLDAPIDStore extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String webName = null;
        if (request.getUserPrincipal() != null) {
            webName = request.getUserPrincipal().getName();
        }
        response.getWriter().write("web username: " + webName + "\n");
    }

}
