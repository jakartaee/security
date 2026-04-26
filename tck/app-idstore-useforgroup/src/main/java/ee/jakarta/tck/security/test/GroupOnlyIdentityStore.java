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

import static java.util.Arrays.asList;
import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

@RequestScoped
public class GroupOnlyIdentityStore implements IdentityStore {

    private Map<String, Set<String>> userGroup;

    @PostConstruct
    public void init() {
        userGroup = new HashMap<>();
        userGroup.put("tom", new TreeSet<>(asList("Oracle", "Oracle_HQ")));
        userGroup.put("emma", new TreeSet<>(asList("Oracle", "Oracle_CDC")));
        userGroup.put("bob", new TreeSet<>(asList("Oracle")));
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        Set<String> groups = new TreeSet<>();
        groups.add("useforgroup:getCallerGroups");
        groups.addAll(userGroup.get(validationResult.getCallerPrincipal().getName()));
        return groups;
    }

    @Override
    public int priority() {
        return 90;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(PROVIDE_GROUPS);
    }

}
