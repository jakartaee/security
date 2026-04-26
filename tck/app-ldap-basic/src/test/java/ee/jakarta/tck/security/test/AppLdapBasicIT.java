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
public class AppLdapBasicIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck idstore/ldap/basic#testAnnotationLdapIDStore_Basic.
     *
     * Default attributes of {@code @LdapIdentityStoreDefinition} together with
     * an explicit ONE_LEVEL caller/group search scope. Three sub-scenarios:
     * <ol>
     *   <li>Valid user / valid password -> VALID with Administrator+Manager
     *       and ValidateCallerDN={@code uid=tom,ou=caller,dc=securityapi,dc=net}
     *   <li>Valid user / wrong password -> INVALID with no groups
     *   <li>Unknown user -> INVALID with no groups
     * </ol>
     */
    @Test
    public void testAnnotationLdapIDStore_Basic_valid() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tom&pwd=secret1");

        assertTrue("Expected VALID status.\n" + response,
                response.contains("ValidateResultStatus=VALID"));
        assertTrue("Expected Administrator group.\n" + response, response.contains("Administrator"));
        assertTrue("Expected Manager group.\n" + response, response.contains("Manager"));
        assertTrue("Expected caller DN uid=tom,ou=caller,dc=securityapi,dc=net.\n" + response,
                response.contains("ValidateCallerDN=uid=tom,ou=caller,dc=securityapi,dc=net"));
        assertTrue("Expected web username tom.\n" + response, response.contains("web username: tom"));
    }

    @Test
    public void testAnnotationLdapIDStore_Basic_pwdInvalid() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tom&pwd=invalid_pwd");

        assertTrue("Expected INVALID status.\n" + response,
                response.contains("ValidateResultStatus=INVALID"));
        assertTrue("Expected empty group set.\n" + response, response.contains("ValidateResultGroups=[]"));
    }

    @Test
    public void testAnnotationLdapIDStore_Basic_userInvalid() {
        String response = readFromServer("/ServletForLDAPIDStore?user=tom_invalid&pwd=invalid_pwd");

        assertTrue("Expected INVALID status.\n" + response,
                response.contains("ValidateResultStatus=INVALID"));
        assertTrue("Expected empty group set.\n" + response, response.contains("ValidateResultGroups=[]"));
    }

}
