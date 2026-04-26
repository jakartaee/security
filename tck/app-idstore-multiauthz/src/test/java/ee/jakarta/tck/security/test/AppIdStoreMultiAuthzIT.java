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
public class AppIdStoreMultiAuthzIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/multiauthz#testIdentityStore_getGroups_multiGroupStore_highPriority_valid.
     *
     * Four IdentityStores: IDStore1 (priority 400, BOTH), IDStore2
     * (priority 300, VALIDATE-only), IDStoreAuthz1 (priority 200,
     * PROVIDE_GROUPS-only), IDStoreAuthz2 (priority 100, PROVIDE_GROUPS-only).
     *
     * tom/secret1 validates VALID in IDStore2 first (lower priority number =
     * tried later, but first non-INVALID wins on the validation chain). The
     * IdentityStoreHandler must then merge groups from BOTH PROVIDE_GROUPS
     * stores into the result.
     */
    @Test
    public void testIdentityStore_getGroups_multiGroupStore_highPriority_valid() {
        String response = readFromServer("/ServletForMultiAuthzIDStore?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue(response.contains("web username: tom"));

        // Validation comes from one of IDStore1 or IDStore2 depending on priority order.
        assertTrue("Expected validation sentinel from one of the validating stores.\n" + response,
                response.contains("IDStore1:validate") || response.contains("IDStore2:validate"));

        // Both PROVIDE_GROUPS stores must contribute their sentinel + groups.
        assertTrue(response.contains("IDStoreAuthz1:getCallerGroups"));
        assertTrue(response.contains("AdminAuthz1"));
        assertTrue(response.contains("ManagerAuthz1"));
        assertTrue(response.contains("IDStoreAuthz2:getCallerGroups"));
        assertTrue(response.contains("AdminAuthz2"));
        assertTrue(response.contains("ManagerAuthz2"));
    }

    /**
     * Migrated from old-tck idstore/multiauthz#testIdentityStore_getGroups_multiGroupStore_lowerPriority_valid.
     *
     * emma/secret2 — IDStore2 has emma/secret3 (mismatch -> INVALID), but
     * IDStore1 has emma/secret2 (VALID). Result is VALID with the IDStore1
     * sentinel plus both PROVIDE_GROUPS stores' groups for emma.
     */
    @Test
    public void testIdentityStore_getGroups_multiGroupStore_lowerPriority_valid() {
        String response = readFromServer("/ServletForMultiAuthzIDStore?user=emma&pwd=secret2");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue(response.contains("web username: emma"));

        // IDStore1 is the only validating store that accepts emma/secret2.
        assertTrue("Expected validation to fall through to IDStore1.\n" + response,
                response.contains("IDStore1:validate"));

        // Both PROVIDE_GROUPS stores must contribute groups for emma.
        assertTrue(response.contains("IDStoreAuthz1:getCallerGroups"));
        assertTrue(response.contains("AdminAuthz1"));
        assertTrue(response.contains("EmployeeAuthz1"));
        assertTrue(response.contains("IDStoreAuthz2:getCallerGroups"));
        assertTrue(response.contains("AdminAuthz2"));
        assertTrue(response.contains("EmployeeAuthz2"));
    }

}
