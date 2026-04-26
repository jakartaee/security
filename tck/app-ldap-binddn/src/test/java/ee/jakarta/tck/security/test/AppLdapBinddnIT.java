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
public class AppLdapBinddnIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/ldap/binddn.
     *
     * Verifies that the LDAP store binds with the configured bindDn /
     * bindDnPassword and uses the {@code callerx}/{@code groupx} subtree.
     */
    @Test
    public void testAnnotationLdapIDStore_Binddn_validTomx() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tomx&pwd=secret1");
        assertTrue(response, response.contains("ValidateResultStatus=VALID"));
        assertTrue(response, response.contains("Administratorx"));
        assertTrue(response, response.contains("Managerx"));
        assertTrue(response, response.contains("web username: tomx"));
    }

    @Test
    public void testAnnotationLdapIDStore_Binddn_invalidTom() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tom&pwd=secret1");
        assertTrue(response, response.contains("ValidateResultStatus=INVALID"));
        assertTrue(response, response.contains("ValidateResultGroups=[]"));
    }

    @Test
    public void testAnnotationLdapIDStore_Binddn_validBobx() {
        String response = readFromServer("/ServletForLDAPIDStore?user=bobx&pwd=secret3");
        assertTrue(response, response.contains("ValidateResultStatus=VALID"));
        assertTrue(response, response.contains("Administratorx"));
        assertTrue(response, response.contains("web username: bobx"));
    }

}
