/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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
public class AppDbHashAlgorithmParamIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/database/hashalgorithmparam#testAnnotationDBIDStore_HashAlgorithmParam.
     *
     * Three users have passwords pre-hashed with three different Pbkdf2 parameter
     * sets. The DB store sets non-default {@code hashAlgorithmParameters};
     * verification still succeeds for all three because the encoded hash is
     * self-describing and the configured parameters only affect generate-time.
     */
    @Test
    public void testHashAlgorithmParam_with_tom_hash256_saltsize32() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom_hash256_saltsize32&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom_hash256_saltsize32.\n" + response,
                response.contains("web username: tom_hash256_saltsize32"));
        assertTrue("Expected Administrator+Manager groups.\n" + response,
                response.contains("Administrator") && response.contains("Manager"));
    }

    @Test
    public void testHashAlgorithmParam_with_tom_hash512_saltsize16() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom_hash512_saltsize16&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom_hash512_saltsize16.\n" + response,
                response.contains("web username: tom_hash512_saltsize16"));
        assertTrue("Expected Administrator+Manager groups.\n" + response,
                response.contains("Administrator") && response.contains("Manager"));
    }

    @Test
    public void testHashAlgorithmParam_with_tom_hash512_saltsize32() {
        String response = readFromServer("/ServletForDatabaseIDStore?user=tom_hash512_saltsize32&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected web username tom_hash512_saltsize32.\n" + response,
                response.contains("web username: tom_hash512_saltsize32"));
        assertTrue("Expected Administrator+Manager groups.\n" + response,
                response.contains("Administrator") && response.contains("Manager"));
    }

}
