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
public class AppIdStoreUseForValidationIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/useforvalidation#testIdentityStore_validationType_useforvalidation:
     * a single VALIDATE-only IdentityStore returns groups from validate(), but
     * the IdentityStoreHandler must IGNORE those groups (no PROVIDE_GROUPS
     * store is registered to provide them). Result is VALID with empty groups.
     */
    @Test
    public void testIdentityStore_validationType_useforvalidation() {
        String response = readFromServer("/ServletForIDStoreUseForValidation?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected an empty group set (validate-only store, no PROVIDE_GROUPS store).\n" + response,
                response.contains("ValidateResultGroups=[]"));
        assertTrue("Expected the protected resource to identify the caller as tom.\n" + response,
                response.contains("web username: tom"));
    }

}
