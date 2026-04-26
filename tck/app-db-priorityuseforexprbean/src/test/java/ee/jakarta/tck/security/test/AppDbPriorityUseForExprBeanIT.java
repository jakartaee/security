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
public class AppDbPriorityUseForExprBeanIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/database/priorityuseforexprbean#testAnnotationDBIDStore_priorityuseforexprbean.
     *
     * Both {@code priorityExpression} (-> 300) and {@code useForExpression}
     * ({@code VALIDATE+PROVIDE_GROUPS}) override the literal annotation values
     * via EL bean expressions. With effective DB priority 300 and IdentityStore1
     * at priority 200, the DB store both validates and provides groups; the
     * in-mem store still contributes its own groups via PROVIDE_GROUPS.
     */
    @Test
    public void testAnnotationDBIDStore_priorityuseforexprbean_tom() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom.\n" + response,
                response.contains("web username: tom"));
        assertTrue("Expected groups from IdentityStore1.\n" + response,
                response.contains("Administrator1") && response.contains("Manager1"));
    }

    @Test
    public void testAnnotationDBIDStore_priorityuseforexprbean_emma() {
        // emma/secret2 only matches the DB store. With the EL-overridden useFor
        // including VALIDATE, the DB store can validate her credentials.
        String response = readFromServer("/ServletForDatabaseIDStore?user=emma&pwd=secret2");

        assertTrue("Expected VALID status from DB store.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username emma.\n" + response,
                response.contains("web username: emma"));
        assertTrue("Expected groups from DB store.\n" + response,
                response.contains("Administrator") && response.contains("Employee"));
    }

}
