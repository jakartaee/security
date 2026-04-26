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

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static java.util.Arrays.asList;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

/**
 * In-memory IdentityStore (priority 200, PROVIDE_GROUPS only) used by the
 * useforgroup test to verify that multiple group-providing stores are merged.
 */
@RequestScoped
public class IdentityStore2 implements IdentityStore {

    private Map<String, String> userPwd;
    private Map<String, Set<String>> userGroup;

    @PostConstruct
    public void init() {
        userPwd = new HashMap<>();
        userPwd.put("tom", "secret1");
        userPwd.put("emma", "secret12");
        userPwd.put("bob", "secret13");

        userGroup = new HashMap<>();
        userGroup.put("tom", new HashSet<>(asList("Administrator2", "Manager2")));
        userGroup.put("emma", new HashSet<>(asList("Administrator2", "Employee2")));
        userGroup.put("bob", new HashSet<>(asList("Administrator2")));
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (!(credential instanceof UsernamePasswordCredential)) {
            return NOT_VALIDATED_RESULT;
        }

        UsernamePasswordCredential up = (UsernamePasswordCredential) credential;
        String caller = up.getCaller();
        String expectedPW = userPwd.get(caller);

        if (expectedPW != null && expectedPW.equals(up.getPasswordAsString())) {
            return new CredentialValidationResult(caller, userGroup.get(caller));
        }
        return INVALID_RESULT;
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        Set<String> groups = new HashSet<>();
        groups.add("IDStore2:getCallerGroups");
        Set<String> u = userGroup.get(validationResult.getCallerPrincipal().getName());
        if (u != null) {
            groups.addAll(u);
        }
        return groups;
    }

    @Override
    public int priority() {
        return 200;
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(PROVIDE_GROUPS);
    }
}
