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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.IdentityStore.ValidationType;

/**
 * EL config bean used by the {@code useForExpression} / {@code priorityExpression}
 * attributes of {@code @DatabaseIdentityStoreDefinition} in this module.
 */
@RequestScoped
@Named
public class ConfigBean {

    public int getPriority300() {
        return 300;
    }

    public ValidationType[] getUseforBoth() {
        return new ValidationType[] { ValidationType.VALIDATE, ValidationType.PROVIDE_GROUPS };
    }

    public ValidationType[] getUseforProvideGroup() {
        return new ValidationType[] { ValidationType.PROVIDE_GROUPS };
    }

}
