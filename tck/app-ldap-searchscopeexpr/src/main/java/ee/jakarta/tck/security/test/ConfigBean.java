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

import static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition.LdapSearchScope.ONE_LEVEL;
import static jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition.LdapSearchScope.SUBTREE;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.IdentityStore.ValidationType;
import jakarta.security.enterprise.identitystore.LdapIdentityStoreDefinition.LdapSearchScope;

@RequestScoped
@Named
public class ConfigBean {

    public int getPriority300() {
        return 300;
    }

    public int getPriority100() {
        return 100;
    }

    public ValidationType[] getUseforBoth() {
        return new ValidationType[] { ValidationType.VALIDATE, ValidationType.PROVIDE_GROUPS };
    }

    public ValidationType[] getUseforValidate() {
        return new ValidationType[] { ValidationType.VALIDATE };
    }

    public ValidationType[] getUseforProvideGroup() {
        return new ValidationType[] { ValidationType.PROVIDE_GROUPS };
    }

    public LdapSearchScope getSearchScopeOneLevel() {
        return ONE_LEVEL;
    }

    public LdapSearchScope getSearchScopeSubTree() {
        return SUBTREE;
    }
}
