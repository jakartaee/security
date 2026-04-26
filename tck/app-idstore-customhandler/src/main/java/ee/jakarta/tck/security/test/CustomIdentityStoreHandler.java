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

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static jakarta.interceptor.Interceptor.Priority.APPLICATION;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static jakarta.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;

/**
 * Application-supplied IdentityStoreHandler. The default IdentityStoreHandler
 * is replaced by this @Alternative @Priority bean. It iterates the
 * IdentityStore beans (sorted by priority), short-circuits on INVALID,
 * accepts the first VALID, and merges groups from every PROVIDE_GROUPS
 * store, plus a sentinel "customIdentiyStoreHandler" marker so the IT can
 * verify the wiring.
 */
@Alternative
@Priority(APPLICATION)
@ApplicationScoped
public class CustomIdentityStoreHandler implements IdentityStoreHandler {

    private List<IdentityStore> validatingIdentityStores;
    private List<IdentityStore> groupProvidingIdentityStores;

    @PostConstruct
    public void init() {
        List<IdentityStore> identityStores = getBeanReferencesByType(IdentityStore.class);

        validatingIdentityStores = identityStores.stream()
                .filter(i -> i.validationTypes().contains(VALIDATE))
                .sorted(comparing(IdentityStore::priority))
                .collect(toList());

        groupProvidingIdentityStores = identityStores.stream()
                .filter(i -> i.validationTypes().contains(PROVIDE_GROUPS))
                .sorted(comparing(IdentityStore::priority))
                .collect(toList());
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        CredentialValidationResult validationResult = null;

        for (IdentityStore store : validatingIdentityStores) {
            CredentialValidationResult temp = store.validate(credential);
            switch (temp.getStatus()) {
                case NOT_VALIDATED:
                    break;
                case INVALID:
                case VALID:
                    validationResult = temp;
                    break;
                default:
                    throw new IllegalArgumentException("Value not supported " + temp.getStatus());
            }
            if (validationResult != null && validationResult.getStatus() == CredentialValidationResult.Status.INVALID) {
                break;
            }
        }

        if (validationResult == null) {
            return INVALID_RESULT;
        }

        if (validationResult.getStatus() != VALID) {
            return validationResult;
        }

        CallerPrincipal callerPrincipal = validationResult.getCallerPrincipal();
        Set<String> groups = new HashSet<>();
        groups.add("customIdentiyStoreHandler");

        for (IdentityStore store : groupProvidingIdentityStores) {
            groups.addAll(store.getCallerGroups(validationResult));
        }

        return new CredentialValidationResult(callerPrincipal, groups);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> getBeanReferencesByType(Class<T> type) {
        BeanManager beanManager = CDI.current().getBeanManager();
        Set<Bean<?>> beans = beanManager.getBeans(type);
        List<T> result = new ArrayList<>(beans.size());

        for (Bean<?> bean : beans) {
            Bean<?> resolved = beanManager.resolve(Collections.singleton(bean));
            if (resolved != null) {
                result.add((T) beanManager.getReference(resolved, type, beanManager.createCreationalContext(resolved)));
            }
        }

        return result;
    }

}
