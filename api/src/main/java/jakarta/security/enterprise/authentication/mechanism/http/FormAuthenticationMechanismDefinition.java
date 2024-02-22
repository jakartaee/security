/*
 * Copyright (c) 2024 Contributors to Eclipse Foundation.
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

package jakarta.security.enterprise.authentication.mechanism.http;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation used to define a container authentication mechanism that implements
 * FORM authentication as defined by the Servlet spec (13.6.3) and make that
 * implementation available as an enabled CDI bean.
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface FormAuthenticationMechanismDefinition {

    @Nonbinding
    LoginToContinue loginToContinue();

    /**
     * List of {@link Qualifier qualifier annotations}.
     *
     * <p>
     * An {@link HttpAuthenticationMechanism} injection point
     * with these qualifier annotations injects a bean that is
     * produced by this {@code FormAuthenticationMechanismDefinition}.</p>
     *
     * <p>
     * The default value is {@code FormAuthenticationMechanism}, indicating that
     * this {@code FormAuthenticationMechanismDefinition} produces
     * bean instances of type {@link HttpAuthenticationMechanism} qualified by
     * {@code FormAuthenticationMechanism}.
     *
     * @return list of qualifiers.
     * @since 4.0
     */
    Class<?>[] qualifiers() default { FormAuthenticationMechanism.class };

    @Qualifier
    @Retention(RUNTIME)
    @Target({FIELD, METHOD, TYPE, PARAMETER})
    public static @interface FormAuthenticationMechanism {

        /**
         * Supports inline instantiation of the {@link FormAuthenticationMechanism} qualifier.
         *
         * @since 4.0
         */
        public static final class Literal extends AnnotationLiteral<FormAuthenticationMechanism> implements FormAuthenticationMechanism {
            private static final long serialVersionUID = 1L;

            /**
             * Instance of the {@link FormAuthenticationMechanism} qualifier.
             */
            public static final Literal INSTANCE = new Literal();
        }

    }

}
