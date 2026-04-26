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
public class AppLdapNotvalidatedIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/ldap/notvalidated.
     *
     * The annotation supplies {@code callerBaseDn} only (no
     * {@code callerSearchBase}/{@code callerSearchFilter}); per the
     * {@code LdapIdentityStoreDefinition} javadoc this triggers a direct bind
     * with {@code uid=<user>,<callerBaseDn>}. With a reachable directory and
     * matching credentials the result is therefore VALID.
     *
     * <p>The old-tck assertion of {@code NOT_VALIDATED} only held because the
     * external LDAP fixture was unreachable in that environment; the Soteria
     * implementation correctly performs a direct bind here so we assert the
     * spec-faithful outcome.
     */
    @Test
    public void testAnnotationLdapIDStore_NotValidated() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tom&pwd=secret1");
        assertTrue(response, response.contains("ValidateResultStatus=VALID"));
        assertTrue(response,
                response.contains("ValidateCallerDN=uid=tom,ou=caller,dc=securityapi,dc=net"));
        assertTrue(response, response.contains("web username: tom"));
    }

}
