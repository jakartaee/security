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

import static ee.jakarta.tck.security.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AppHamWorkflowCleanSubjectIT extends ArquillianBase {

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    /**
     * Migrated from old-tck ham/workflow/cleansubject#testHAMCleanSubject:
     * the HAM sets a request attribute on validateRequest, removes it on
     * cleanSubject, and writes a marker to the response so we can confirm the
     * cleanSubject callback fired.
     */
    @Test
    public void testHAMCleanSubject() {
        String response = readFromServer("/servlet?name=reza&password=secret1");

        assertTrue("Expected attribute to be present before logout.\n" + response,
                response.contains("The attribute FlagforCleanSubject exist before logout : true"));
        assertTrue("Expected HAM.cleanSubject() to have fired.\n" + response,
                response.contains("This is in HAM cleanSubject mthod."));
        assertTrue("Expected attribute to be cleared after logout.\n" + response,
                response.contains("The attribute FlagforCleanSubject exist after logout : false"));
    }

}
