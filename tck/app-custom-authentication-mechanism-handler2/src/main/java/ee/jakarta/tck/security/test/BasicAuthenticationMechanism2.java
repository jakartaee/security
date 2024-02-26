/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Qualifier
@Retention(RUNTIME)
@Target({ FIELD, METHOD, TYPE, PARAMETER })
public @interface BasicAuthenticationMechanism2 {

    /**
     * Supports inline instantiation of the {@link BasicAuthenticationMechanism2} qualifier.
     *
     * @since 4.0
     */
    public static final class Literal extends AnnotationLiteral<BasicAuthenticationMechanism2> implements BasicAuthenticationMechanism2 {
        private static final long serialVersionUID = 1L;

        /**
         * Instance of the {@link BasicAuthenticationMechanism2} qualifier.
         */
        public static final Literal INSTANCE = new Literal();
    }

}