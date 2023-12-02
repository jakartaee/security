/*
 * Copyright (c) 2022-2023 Contributors to the Eclipse Foundation
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
module jakarta.security {
    exports jakarta.security.enterprise;
    exports jakarta.security.enterprise.authentication.mechanism.http;
    exports jakarta.security.enterprise.authentication.mechanism.http.openid;
    exports jakarta.security.enterprise.credential;
    exports jakarta.security.enterprise.identitystore;
    exports jakarta.security.enterprise.identitystore.openid;

    requires transitive jakarta.servlet;
    requires transitive jakarta.security.auth.message;
    requires transitive jakarta.json;

    requires jakarta.cdi;
    requires jakarta.el;
}
