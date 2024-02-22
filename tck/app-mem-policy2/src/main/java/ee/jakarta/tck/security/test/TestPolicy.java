/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
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

import static jakarta.security.jacc.PolicyContext.PRINCIPAL_MAPPER;

import jakarta.security.jacc.Policy;
import jakarta.security.jacc.PolicyContext;
import jakarta.security.jacc.PolicyContextException;
import jakarta.security.jacc.PrincipalMapper;
import jakarta.security.jacc.WebResourcePermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Principal;
import java.util.logging.Logger;
import javax.security.auth.Subject;

/**
 * Policy implementation that uses a custom permission check
 * to grant access to {@code /protectedServlet/[*]/test} to
 * the *authenticated* caller.
 */
public class TestPolicy implements Policy {

    private static final Logger LOGGER = Logger.getLogger(TestPolicy.class.getName());

    private final Policy originalPolicy;

    public TestPolicy(Policy policy) {
        this.originalPolicy = policy;
    }

    @Override
    public boolean implies(Permission permissionToBeChecked, Subject subject) {
        LOGGER.info(permissionToBeChecked.toString());
        LOGGER.info(subject.toString());

        Principal callerPrincipal = getCallerPrincipal(subject);

        LOGGER.info(callerPrincipal == null? "null" : callerPrincipal.toString() + " " + callerPrincipal.getName());

        // First try our custom permission checking, but only for authenticated callers
        if (isAuthenticated(callerPrincipal) && impliesCustom(permissionToBeChecked)) {
           return true;
        }

        // If custom doesn't grant access, try the original policy so we
        // keep all normal checks in place.
        return originalPolicy.implies(permissionToBeChecked, subject);
    }

    @Override
    public PermissionCollection getPermissionCollection(Subject subject) {
        return originalPolicy.getPermissionCollection(subject);
    }

    private Principal getCallerPrincipal(Subject subject) {
        try {
            // Use the PrincipalMapper to retrieve the caller principal
            // that should be somewhere in the Subject

            PrincipalMapper principalMapper = PolicyContext.getContext(PRINCIPAL_MAPPER);

            return principalMapper.getCallerPrincipal(subject);

        } catch (PolicyContextException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isAuthenticated(Principal callerPrincipal) {
        return callerPrincipal != null && callerPrincipal.getName() != null;
    }

    private boolean impliesCustom(Permission permissionToBeChecked) {
        return
            permissionToBeChecked instanceof WebResourcePermission &&
            permissionToBeChecked.getName().startsWith("/protectedServlet/") &&
            permissionToBeChecked.getName().endsWith("/test");
    }

}
