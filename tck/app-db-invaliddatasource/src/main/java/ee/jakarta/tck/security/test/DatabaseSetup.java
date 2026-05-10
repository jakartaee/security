/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import javax.sql.DataSource;

@DataSourceDefinition(
    name = "java:global/securityAPIDB-invaliddatasource",
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:~/SoteriaTestDB-db-invaliddatasource;DB_CLOSE_ON_EXIT=FALSE"
)
@Singleton
@Startup
public class DatabaseSetup {

    @Resource(lookup = "java:global/securityAPIDB-invaliddatasource")
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        executeUpdate(dataSource, "DROP TABLE IF EXISTS caller");
        executeUpdate(dataSource, "DROP TABLE IF EXISTS caller_groups");

        executeUpdate(dataSource, "CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(255))");
        executeUpdate(dataSource, "CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64))");

        executeUpdate(dataSource, "INSERT INTO caller VALUES('tom', 'secret1')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('emma', 'secret2')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('bob', 'secret3')");

        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('tom', 'Administrator')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('tom', 'Manager')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('emma', 'Administrator')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('emma', 'Employee')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('bob', 'Administrator')");
    }

    @PreDestroy
    public void destroy() {
        try {
            executeUpdate(dataSource, "DROP TABLE IF EXISTS caller");
            executeUpdate(dataSource, "DROP TABLE IF EXISTS caller_groups");
        } catch (Exception ignore) {
        }
    }

    private static void executeUpdate(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

}
