/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import jakarta.security.jacc.Policy;
import jakarta.security.jacc.WebResourcePermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.logging.Logger;
import javax.security.auth.Subject;

/**
 * Policy implementation that uses a custom permission check
 * to grant access to {@code /protectedServlet/[*]/test} to
 * the unauthenticated caller.
 */
public class TestPolicy implements Policy {

    private static final Logger LOGGER = Logger.getLogger(TestPolicy.class.getName());

    private final Policy originalPolicy;

    public TestPolicy(Policy policy) {
        this.originalPolicy = policy;
    }

    public boolean implies(Permission permissionToBeChecked, Subject subject) {
        LOGGER.info(permissionToBeChecked.toString());
        LOGGER.info(subject.toString());

        // First try our custom permission checking
        if (impliesCustom(permissionToBeChecked)) {
           return true;
        }

        // If custom doesn't grant access, try the original policy so we
        // keep all normal checks in place.
        return originalPolicy.implies(permissionToBeChecked, subject);
    }

    public PermissionCollection getPermissionCollection(Subject subject) {
        return originalPolicy.getPermissionCollection(subject);
    }

    private boolean impliesCustom(Permission permissionToBeChecked) {
        return
            permissionToBeChecked instanceof WebResourcePermission &&
            permissionToBeChecked.getName().startsWith("/protectedServlet/") &&
            permissionToBeChecked.getName().endsWith("/test");
    }

}
