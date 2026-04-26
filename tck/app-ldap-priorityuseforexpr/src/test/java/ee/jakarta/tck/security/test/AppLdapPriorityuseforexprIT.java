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
public class AppLdapPriorityuseforexprIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/ldap/priorityuseforexpr.
     *
     * The LDAP store has {@code priority=100} but {@code priorityExpression}
     * resolves to 300, so it outranks the in-memory IdentityStore1 (priority
     * 200). Both stores validate, the higher-priority result wins.
     *
     * <ul>
     *   <li>tom/secret1 matches both stores -&gt; LDAP wins, groups
     *       Administrator+Manager
     *   <li>emma/secret2 matches LDAP only -&gt; LDAP wins, groups
     *       Administrator+Employee
     * </ul>
     */
    @Test
    public void testIdentityStore_ldap_priorityuseforexpr_tom() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tom&pwd=secret1");
        assertTrue(response, response.contains("ValidateResultStatus=VALID"));
        assertTrue(response, response.contains("Administrator"));
        assertTrue(response, response.contains("Manager"));
        assertTrue(response, response.contains("web username: tom"));
    }

    @Test
    public void testIdentityStore_ldap_priorityuseforexpr_emma() {
        String response = readFromServer("/ServletForLDAPIDStore?user=emma&pwd=secret2");
        assertTrue(response, response.contains("ValidateResultStatus=VALID"));
        assertTrue(response, response.contains("Administrator"));
        assertTrue(response, response.contains("Employee"));
        assertTrue(response, response.contains("web username: emma"));
    }

}
