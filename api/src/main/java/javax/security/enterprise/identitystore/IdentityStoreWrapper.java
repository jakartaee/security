/*
 * Copyright (c) 2018 Payara Services Limited. All rights reserved.
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
package javax.security.enterprise.identitystore;

import java.util.Set;

import javax.security.enterprise.credential.Credential;

/**
 * This class is an implementation of the IdentityStore interface that can be
 * subclassed by developers wishing to provide extra or different functionality.
 * <p>
 * All methods default to calling the wrapped object.
 *
 */
public class IdentityStoreWrapper implements IdentityStore {

    private final IdentityStore identityStore;

    public IdentityStoreWrapper(IdentityStore identityStore) {
        this.identityStore = identityStore;
    }

    public IdentityStore getWrapped() {
        return identityStore;
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        return getWrapped().validate(credential);
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return getWrapped().getCallerGroups(validationResult);
    }

    @Override
    public int priority() {
        return getWrapped().priority();
    }

    @Override
    public Set<ValidationType> validationTypes() {
        return getWrapped().validationTypes();
    }

}