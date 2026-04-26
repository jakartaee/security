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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppIdStoreBasicIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/basic#testIdentityStoreInstall:
     * with a single registered IdentityStore the IdentityStoreHandler must
     * delegate validation, return the groups from validate() — and must NOT
     * call getCallerGroups() (so the synthetic "getCallerGroups" marker the
     * IdentityStore would add via that method is absent).
     */
    @Test
    public void testIdentityStoreInstall() {
        String response = readFromServer("/ServletForIDStoreBasic?user=reza&pwd=secret1");

        assertTrue("Expected ValidateResultStatus=VALID.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected ValidateCallerDN=null (no DN for username/password store).\n" + response,
                response.contains("ValidateCallerDN=null"));
        assertTrue("Expected the validated groups to contain foo and bar.\n" + response,
                response.contains("foo") && response.contains("bar"));
        assertFalse("Expected getCallerGroups() NOT to have been invoked (single-store install).\n" + response,
                response.contains("getCallerGroups"));
        assertTrue("Expected web username reza on the protected resource.\n" + response,
                response.contains("web username: reza"));
    }

}
