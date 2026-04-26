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

import jakarta.enterprise.context.RequestScoped;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

@RequestScoped
public class TestIdentityStore implements IdentityStore {

    private final Map<String, String> callerToPassword = new HashMap<>();
    private final Map<String, Set<String>> callerToGroups = new HashMap<>();

    public TestIdentityStore() {
        callerToPassword.put("reza", "secret1");
        callerToPassword.put("emma", "secret2");
        callerToPassword.put("bob", "secret3");

        callerToGroups.put("reza", new HashSet<>(asList("foo", "bar")));
        callerToGroups.put("emma", new HashSet<>(asList("foo", "kaz")));
        callerToGroups.put("bob", new HashSet<>(asList("foo")));
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            return validate((UsernamePasswordCredential) credential);
        }

        return NOT_VALIDATED_RESULT;
    }

    private CredentialValidationResult validate(UsernamePasswordCredential usernamePasswordCredential) {
        String user = usernamePasswordCredential.getCaller();
        String password = callerToPassword.get(user);

        if (password != null && usernamePasswordCredential.getPassword().compareTo(password)) {
            return new CredentialValidationResult(user, callerToGroups.get(user));
        }

        return INVALID_RESULT;
    }

}
