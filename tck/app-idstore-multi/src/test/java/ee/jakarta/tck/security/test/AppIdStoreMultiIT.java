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
public class AppIdStoreMultiIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/multi#testIdentityStoreValidate_multiIDStore.
     *
     * Three IdentityStores in priority order (1: 100, 2: 200, 3: 300). Each
     * validate() that succeeds tags the result with a sentinel group like
     * "IDStore1:validate". The IdentityStoreHandler must invoke them in
     * priority order and stop on the first VALID, returning that store's
     * groups directly. Subsequent stores must NOT have validate() called.
     */
    @Test
    public void testIdentityStoreValidate_multiIDStore() {
        // tom/secret2 -> IDStore2 validates first VALID match
        String r1 = readFromServer("/ServletForMultiIDStore?user=tom&pwd=secret2");
        assertTrue("tom/secret2 should result in VALID.\n" + r1, r1.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom.\n" + r1, r1.contains("web username: tom"));

        // tom/secret1 -> IDStore1 validates first
        String r2 = readFromServer("/ServletForMultiIDStore?user=tom&pwd=secret1");
        assertTrue("tom/secret1 should result in VALID.\n" + r2, r2.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected the IDStore1 sentinel group.\n" + r2, r2.contains("IDStore1:validate"));
        assertTrue(r2.contains("web username: tom"));

        // emma/secret2 -> IDStore1 validates first
        String r3 = readFromServer("/ServletForMultiIDStore?user=emma&pwd=secret2");
        assertTrue("emma/secret2 should result in VALID.\n" + r3, r3.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected the IDStore1 sentinel group.\n" + r3, r3.contains("IDStore1:validate"));
        assertTrue(r3.contains("web username: emma"));
    }

    /**
     * Migrated from idstore/multi#testIdentityStoreValidate_multiIDStore_INVALID:
     * if every store returns INVALID, the final result is INVALID.
     */
    @Test
    public void testIdentityStoreValidate_multiIDStore_INVALID() {
        String response = readFromServer("/ServletForMultiIDStore?user=tom&pwd=secret_invalid");
        assertTrue("Expected INVALID with no groups.\n" + response,
                response.contains("ValidateResultStatus=INVALID")
                        && response.contains("ValidateResultGroups=[]"));
    }

    /**
     * Migrated from idstore/multi#testIdentityStoreValidate_multiIDStore_INVALIDWithNOTVALIDATED:
     * a single INVALID anywhere in the chain yields INVALID overall, even
     * when other stores return NOT_VALIDATED.
     */
    @Test
    public void testIdentityStoreValidate_multiIDStore_INVALIDWithNOTVALIDATED() {
        for (String caller : new String[] { "notValidated_invalid1", "notValidated_invalid2", "notValidated_invalid3" }) {
            String response = readFromServer("/ServletForMultiIDStore?user=" + caller + "&pwd=secret11");
            assertTrue("Expected INVALID for " + caller + ".\n" + response,
                    response.contains("ValidateResultStatus=INVALID"));
        }
    }

    /**
     * Migrated from idstore/multi#testIdentityStoreValidate_multiIDStore_NOTVALIDATED:
     * if every store returns NOT_VALIDATED, the final result is NOT_VALIDATED.
     */
    @Test
    public void testIdentityStoreValidate_multiIDStore_NOTVALIDATED() {
        String response = readFromServer("/ServletForMultiIDStore?user=notValidated&pwd=secret4");
        assertTrue("Expected NOT_VALIDATED.\n" + response,
                response.contains("ValidateResultStatus=NOT_VALIDATED"));
    }

}
