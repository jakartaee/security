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

package ee.jakarta.tck.security.test;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

// The template pattern matches exactly that of ProtectedTemplateResource
// with just the regexp different, but matching our rest input "Reza".
// It's not defined which one is chosen, but we do specify the
// security system before the (servlet) chain is invoked and the
// actual invocation of the resource must be consistent
@Path("/protectedResource/{id}/users/{username: [a-zA-Z_0-9]*}")
@Produces(TEXT_PLAIN)
public class PublicTemplateResource {

    @Inject
    private HttpServletRequest request;

    @GET
    @Path("sayHi")
    public String sayHi() {
       return "saying hi! protected should be false, protected=" + request.getAttribute("PROTECTED");
    }

}
