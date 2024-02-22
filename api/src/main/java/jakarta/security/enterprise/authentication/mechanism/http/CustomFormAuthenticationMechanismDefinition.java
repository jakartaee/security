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
import jakarta.security.enterprise.SecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation used to define a container authentication mechanism that implements
 * authentication resembling Servlet FORM authentication (Servlet spec 13.6.3).
 * <p>
 * Instead of posting back to a predefined action to continue the authentication dialog
 * (Servlet spec 13.6.3 step 3), this variant depends on the application calling
 * {@link SecurityContext#authenticate(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse, jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters)}
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface CustomFormAuthenticationMechanismDefinition {

    @Nonbinding
    LoginToContinue loginToContinue();

    /**
     * List of {@link Qualifier qualifier annotations}.
     *
     * <p>
     * An {@link HttpAuthenticationMechanism} injection point
     * with these qualifier annotations injects a bean that is
     * produced by this {@code CustomFormAuthenticationMechanismDefinition}.</p>
     *
     * <p>
     * The default value is {@code CustomFormAuthenticationMechanism}, indicating that
     * this {@code CustomFormAuthenticationMechanismDefinition} produces
     * bean instances of type {@link HttpAuthenticationMechanism} qualified by
     * {@code CustomFormAuthenticationMechanism}.
     *
     * @return list of qualifiers.
     * @since 4.0
     */
    Class<?>[] qualifiers() default { CustomFormAuthenticationMechanism.class };

    @Qualifier
    @Retention(RUNTIME)
    @Target({FIELD, METHOD, TYPE, PARAMETER})
    public static @interface CustomFormAuthenticationMechanism {

        /**
         * Supports inline instantiation of the {@link CustomFormAuthenticationMechanism} qualifier.
         *
         * @since 4.0
         */
        public static final class Literal extends AnnotationLiteral<CustomFormAuthenticationMechanism> implements CustomFormAuthenticationMechanism {
            private static final long serialVersionUID = 1L;

            /**
             * Instance of the {@link CustomFormAuthenticationMechanisms} qualifier.
             */
            public static final Literal INSTANCE = new Literal();
        }

    }

}
