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
public class AppIdStoreCustomHandlerIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/customhandler#testIdentityStore_customHandler.
     *
     * The application supplies its own IdentityStoreHandler via an
     * @Alternative @Priority bean, replacing the built-in implementation.
     * The custom handler iterates VALIDATE-typed stores, accepts the first
     * non-INVALID result, then merges groups from PROVIDE_GROUPS-typed stores
     * into the final {@link CredentialValidationResult} and tags the result
     * with a sentinel marker the IT can assert.
     *
     * Note: the old test additionally relied on a live LDAP server at
     * localhost:11389, but the original code had a comment flagging the LDAP
     * groups were not in fact returned ("// The groups in Ldap server is not
     * returned due to bug# https://github.com/javaee-security-spec/soteria/issues/78").
     * The migrated test exercises the custom-handler wiring without LDAP.
     */
    @Test
    public void testIdentityStore_customHandler_validCaller() {
        String response = readFromServer("/ServletForIDStoreCustomhandler?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected the custom handler's sentinel marker.\n" + response,
                response.contains("customIdentiyStoreHandler"));
        assertTrue("Expected the in-mem store's getCallerGroups sentinel.\n" + response,
                response.contains("getCallerGroups"));
        assertTrue("Expected validated groups to contain Administrator1.\n" + response,
                response.contains("Administrator1"));
        assertTrue("Expected validated groups to contain Manager1.\n" + response,
                response.contains("Manager1"));
        assertTrue("Expected web username tom.\n" + response,
                response.contains("web username: tom"));
    }

    /**
     * Migrated from idstore/customhandler#testIdentityStore_customHandler:
     * with mismatched password the validate must short-circuit on INVALID
     * with no groups in the result.
     */
    @Test
    public void testIdentityStore_customHandler_invalidCaller() {
        String response = readFromServer("/ServletForIDStoreCustomhandler?user=emma&pwd=secret2");

        assertTrue("Expected INVALID status when validation fails.\n" + response,
                response.contains("ValidateResultStatus=INVALID"));
        assertTrue("Expected no groups in the INVALID result.\n" + response,
                response.contains("ValidateResultGroups=[]"));
    }

}
