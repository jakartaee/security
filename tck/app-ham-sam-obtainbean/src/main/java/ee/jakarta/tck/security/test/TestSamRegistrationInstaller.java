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

import jakarta.security.auth.message.config.AuthConfigFactory;
import jakarta.security.auth.message.module.ServerAuthModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TestSamRegistrationInstaller implements ServletContextListener {

    private String registrationId;

    private static String getAppContextID(ServletContext context) {
        return context.getVirtualServerName() + " " + context.getContextPath();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServerAuthModule sam = new TestServerAuthModule();
        registrationId = AuthConfigFactory.getFactory().registerConfigProvider(
                new TestAuthConfigProvider(sam), "HttpServlet",
                getAppContextID(sce.getServletContext()),
                "Test single SAM authentication config provider");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        AuthConfigFactory.getFactory().removeRegistration(registrationId);
    }

}
