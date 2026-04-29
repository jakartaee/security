/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
 *
 */
package jakarta.security.enterprise.authentication.mechanism.http;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * DRAFT API! SUBJECT TO CHANGE!
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Repeatable(JwtAuthenticationMechanismDefinition.List.class)
public @interface JwtAuthenticationMechanismDefinition {

    String configJwtTokenHeader() default "Authorization";
    String configJwtTokenCookie() default "Bearer";

    String acceptedIssuer();
    String[] allowedAudience() default {};
    String publicKey() default "";
    String publicKeyLocation() default "/publicKey.pem";
    String decryptKeyLocation() default "/privateKey.pem";
    String keyAlgorithm() default "RSA-OAEP-256";
    long tokenAge() default Long.MAX_VALUE;
    long clockSkew() default 0;
    long keyCacheTTL() default 300; // 5 minutes

    boolean enableNamespace() default false;
    String customNamespace() default "";
    boolean disableTypeVerification() default false;

    /**
     * List of {@link Qualifier qualifier annotations}.
     *
     * <p>
     * An {@link JwtAuthenticationMechanism} injection point
     * with these qualifier annotations injects a bean that is
     * produced by this {@code JwtAuthenticationMechanismDefinition}.</p>
     *
     * <p>
     * The default value is {@code JwtAuthenticationMechanism}, indicating that
     * this {@code JwtAuthenticationMechanismDefinition} produces
     * bean instances of type {@link HttpAuthenticationMechanism} qualified by
     * {@code JwtAuthenticationMechanism}.
     *
     * @return list of qualifiers.
     * @since 5.0
     */
    Class<?>[] qualifiers() default { JwtAuthenticationMechanism.class };

    /**
     * Enables multiple <code>JwtAuthenticationMechanism</code>
     * annotations on the same type.
     */
    @Retention(RUNTIME)
    @Target(TYPE)
    public @interface List {
        JwtAuthenticationMechanismDefinition[] value();
    }

    @Qualifier
    @Retention(RUNTIME)
    @Target({FIELD, METHOD, TYPE, PARAMETER})
    public static @interface JwtAuthenticationMechanism {

        /**
         * Supports inline instantiation of the {@link JwtAuthenticationMechanism} qualifier.
         *
         * @since 4.0
         */
        public static final class Literal extends AnnotationLiteral<JwtAuthenticationMechanism> implements JwtAuthenticationMechanism {
            private static final long serialVersionUID = 1L;

            /**
             * Instance of the {@link JwtAuthenticationMechanism} qualifier.
             */
            public static final Literal INSTANCE = new Literal();
        }

    }

}
