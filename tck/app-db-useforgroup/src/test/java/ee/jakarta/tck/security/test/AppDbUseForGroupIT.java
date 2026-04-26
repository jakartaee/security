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

import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppDbUseForGroupIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/database/useforgroup#testAnnotationDBIDStore_useforgroup.
     *
     * Three stores: DB (PROVIDE_GROUPS only, prio 100), IdentityStore1 (both
     * validation types, prio 1000) and IdentityStore2 (PROVIDE_GROUPS only,
     * prio 200). On a successful validation by IdentityStore1, all three
     * group-providing stores must contribute to the resulting group set.
     */
    @Test
    public void testAnnotationDBIDStore_useforgroup_tom() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom.\n" + response,
                response.contains("web username: tom"));
        assertTrue("Expected groups from IdentityStore1.\n" + response,
                response.contains("Administrator1") && response.contains("Manager1"));
        assertTrue("Expected groups from DB store.\n" + response,
                response.contains("Administrator") && response.contains("Manager"));
        assertTrue("Expected groups from IdentityStore2.\n" + response,
                response.contains("Administrator2") && response.contains("Manager2"));
    }

    @Test
    public void testAnnotationDBIDStore_useforgroup_emma() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=emma&pwd=secret12");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username emma.\n" + response,
                response.contains("web username: emma"));
        assertTrue("Expected groups from IdentityStore1.\n" + response,
                response.contains("Administrator1") && response.contains("Employee1"));
        assertTrue("Expected groups from IdentityStore2.\n" + response,
                response.contains("Administrator2") && response.contains("Employee2"));
    }

}
