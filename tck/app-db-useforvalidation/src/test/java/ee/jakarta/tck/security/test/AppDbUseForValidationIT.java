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
public class AppDbUseForValidationIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/database/useforvalidation#testAnnotationDBIDStore_useforvalidation.
     *
     * Two stores: a DatabaseIdentityStore configured with useFor = {VALIDATE},
     * priority 200; and an in-mem IdentityStore1 with both validation types,
     * priority 100. The lower-priority store sees validation requests first.
     * tom/secret1 validates VALID in the in-mem store, with no groups from the
     * DB store (it's VALIDATE-only). emma/secret2 only validates against the
     * DB; the DB returns VALID with no groups (since useFor=VALIDATE only).
     */
    @Test
    public void testValid_inMem_higherPriority() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom.\n" + response,
                response.contains("web username: tom"));
        assertTrue("Expected at least one group from the in-mem store.\n" + response,
                response.contains("Administrator1") || response.contains("Manager1"));
    }

    @Test
    public void testValid_db_validateOnly() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=emma&pwd=secret2");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected empty group set (DB store is VALIDATE-only and in-mem rejects).\n" + response,
                response.contains("ValidateResultGroups=[]"));
        assertTrue("Expected web username emma.\n" + response,
                response.contains("web username: emma"));
    }

    @Test
    public void testInvalid_unknownUser() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=invalid_user&pwd=secret2");

        assertTrue("Expected INVALID status.\n" + response,
                response.contains("ValidateResultStatus=INVALID"));
        assertTrue("Expected empty group set.\n" + response,
                response.contains("ValidateResultGroups=[]"));
    }

}
