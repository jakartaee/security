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
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;

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
 * In-mem IdentityStore covering "tomx" with {@code Administratorx, Managerx}.
 * Priority 100 (consulted before the DB store at priority 200).
 */
@RequestScoped
public class IdentityStore1 implements IdentityStore {

    private Map<String, String> userPwd;
    private Map<String, Set<String>> userGroup;

    @PostConstruct
    public void init() {
        userPwd = new HashMap<>();
        userPwd.put("tomx", "secret1");

        userGroup = new HashMap<>();
        userGroup.put("tomx", new HashSet<>(asList("Administratorx", "Managerx")));
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (!(credential instanceof UsernamePasswordCredential)) {
            return NOT_VALIDATED_RESULT;
        }
        UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
        String caller = usernamePassword.getCaller();
        String expectedPW = userPwd.get(caller);
        if (expectedPW != null && expectedPW.equals(usernamePassword.getPasswordAsString())) {
            return new CredentialValidationResult(caller, userGroup.get(caller));
        }
        return INVALID_RESULT;
    }

    @Override
    public int priority() {
        return 100;
    }

}
