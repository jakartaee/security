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
public class AppIdStoreUseForGroupIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/useforgroup#testIdentityStore_validationType_useforgroup:
     * a VALIDATE-only store authenticates the caller; a separate
     * PROVIDE_GROUPS-only store supplies the groups via
     * getCallerGroups(). The IdentityStoreHandler must IGNORE the groups
     * returned from validate() and use only the ones from the dedicated
     * groups store.
     */
    @Test
    public void testIdentityStore_validationType_useforgroup() {
        String response = readFromServer("/ServletForIDStoreGroupOnly?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected the groups-only store's sentinel marker.\n" + response,
                response.contains("useforgroup:getCallerGroups"));
        assertTrue("Expected groups from the dedicated PROVIDE_GROUPS store.\n" + response,
                response.contains("Oracle") && response.contains("Oracle_HQ"));
        assertTrue("Expected the protected resource to identify the caller as tom.\n" + response,
                response.contains("web username: tom"));
    }

}
