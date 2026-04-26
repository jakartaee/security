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
public class AppIdStoreNoIdStoreIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/noidstore#testIdentityStoreValidate_noIDStore:
     * the application registers no IdentityStore at all. Per spec, the default
     * IdentityStoreHandler should fail to validate (NOT_VALIDATED or an
     * exception). The HAM in this app catches that and writes
     * "Exception received." to the response so the IT can assert it.
     */
    @Test
    public void testIdentityStoreValidate_noIDStore() {
        String response = readFromServer("/ServletForNoIDStore?user=reza&pwd=secret1");

        assertTrue("Expected an exception to be reported (no IdentityStore registered).\n" + response,
                response.contains("Exception received.")
                        || response.contains("ValidateResultStatus=NOT_VALIDATED"));
    }

}
