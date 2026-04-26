/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppDbInvalidPriorityUseForExprIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/database/invalidpriorityuseforexpr#testAnnotationDBIDStore_invalidpriorityuseforexpr.
     *
     * The DB store's {@code priorityExpression} references an undefined EL
     * variable ({@code ${abc}}). Evaluating the expression must fail; the HAM
     * surfaces the failure as "Exception received." in the response body.
     */
    @Test
    public void testAnnotationDBIDStore_invalidpriorityuseforexpr_tom() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom&pwd=secret1");

        assertTrue("Expected 'Exception received.' due to invalid priorityExpression.\n" + response,
                response.contains("Exception received."));
    }

    @Test
    public void testAnnotationDBIDStore_invalidpriorityuseforexpr_emma() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=emma&pwd=secret2");

        assertTrue("Expected 'Exception received.' due to invalid priorityExpression.\n" + response,
                response.contains("Exception received."));
    }

}
