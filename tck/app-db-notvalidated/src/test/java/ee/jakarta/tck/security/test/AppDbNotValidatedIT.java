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
public class AppDbNotValidatedIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/database/notvalidated#testAnnotationDBIDStore_notValidated.
     *
     * The DatabaseIdentityStore is configured with useFor = {PROVIDE_GROUPS}.
     * Per spec, the store does not support validation in this configuration,
     * so calling IdentityStoreHandler.validate must yield NOT_VALIDATED.
     */
    @Test
    public void testAnnotationDBIDStore_notValidated() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom&pwd=secret1");

        assertTrue("Expected NOT_VALIDATED status (PROVIDE_GROUPS-only store).\n" + response,
                response.contains("ValidateResultStatus=NOT_VALIDATED"));
        assertTrue("Expected empty group set.\n" + response,
                response.contains("ValidateResultGroups=[]"));
    }

}
