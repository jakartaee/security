/*
 * Copyright (c) 2023, 2024 Contributors to Eclipse Foundation.
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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
package jakarta.security.enterprise.identitystore;

import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.security.enterprise.identitystore.IdentityStore.ValidationType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation used to define a container provided {@link IdentityStore} that stores
 * caller credentials and identity attributes (together caller identities) in an
 * in-memory store, and make that implementation available as an enabled CDI bean.
 *
 * <p>
 * The data in this store is set at definition time only via the {@link #value()} attribute
 * of this annotation.
 *
 * <p>
 * The following shows an example:
 *
 * <pre>
 * <code>
 * {@literal @}InMemoryIdentityStoreDefinition({
 *  {@literal @}Credentials(callerName = "peter", password = "secret1", groups = { "foo", "bar" }),
 *  {@literal @}Credentials(callerName = "john", password = "secret2", groups = { "foo", "kaz" }),
 *  {@literal @}Credentials(callerName = "carla", password = "secret3", groups = { "foo" }) })
 * </code>
 * </pre>
 *
 * @since 4.0
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface InMemoryIdentityStoreDefinition {

    /**
     * Defines the caller identities stored in the in-memory identity store
     *
     * @return caller identities stored in the in-memory identity store
     */
    Credentials[] value() default {};

    /**
     * Determines the order in case multiple IdentityStores are found.
     * @return the priority.
     */
    int priority() default 90;

    /**
     * Allow <code>priority</code> to be specified as a Jakarta Expression Language expression.
     * If set, overrides any value set with <code>priority</code>.
     *
     * @return the <code>priority</code> Jakarta Expression Language expression
     */
    String priorityExpression() default "";

    /**
     * Determines what the identity store is used for
     *
     * @return the type the identity store is used for
     */
    ValidationType[] useFor() default {VALIDATE, PROVIDE_GROUPS};

    /**
     * Allow <code>useFor</code> to be specified as an Jakarta Expression Language expression.
     * If set, overrides any value set with useFor.
     *
     * @return the <code>useFor</code> Jakarta Expression Language expression
     */
    String useForExpression() default "";

    /**
     * <code>Credentials</code> define a single caller identity for
     * use with the {@link InMemoryIdentityStoreDefinition} annotation.
     *
     */
    @Retention(RUNTIME)
    @Target({ TYPE, METHOD, FIELD, PARAMETER })
    public @interface Credentials {

        /**
         * Name of caller. This is the name a caller uses to authenticate with.
         *
         * @return Name of caller
         */
        String callerName();

        /**
         * A text-based password used by the caller to authenticate.
         *
         * @return A text-based password
         */
        String password();

        /**
         * The optional list of groups that the specified caller is in.
         *
         * @return optional list of groups
         */
        String[] groups() default {};
    }

}
