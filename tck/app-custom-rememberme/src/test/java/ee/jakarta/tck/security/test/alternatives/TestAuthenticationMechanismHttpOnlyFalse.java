/*
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

package ee.jakarta.tck.security.test.alternatives;


import ee.jakarta.tck.security.test.TestAuthenticationMechanism;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.security.enterprise.authentication.mechanism.http.RememberMe;

@RememberMe(
    cookieMaxAgeSeconds = 3600,
    cookieHttpOnly = false,
    cookieSecureOnly = false,
    isRememberMeExpression ="#{self.isRememberMe(httpMessageContext)}"
)
@RequestScoped
@Priority(1000)
@Alternative
public class TestAuthenticationMechanismHttpOnlyFalse extends TestAuthenticationMechanism {
    
}
