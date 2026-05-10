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
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.sql.DataSource;

/**
 * Pre-populates three callers whose passwords are stored as Pbkdf2 hashes
 * generated with three different parameter sets. The DB IdentityStore's own
 * {@code hashAlgorithmParameters} are only used at generate-time; verify-time
 * reads parameters back from the encoded hash.
 */
@DataSourceDefinition(
    name = "java:global/securityAPIDB-hashalgorithmparam",
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:~/SoteriaTestDB-db-hashalgorithmparam;DB_CLOSE_ON_EXIT=FALSE"
)
@Singleton
@Startup
public class DatabaseSetup {

    @Resource(lookup = "java:global/securityAPIDB-hashalgorithmparam")
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        executeUpdate(dataSource, "DROP TABLE IF EXISTS caller");
        executeUpdate(dataSource, "DROP TABLE IF EXISTS caller_groups");

        executeUpdate(dataSource, "CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(255))");
        executeUpdate(dataSource, "CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64))");

        executeUpdate(dataSource, "INSERT INTO caller VALUES('tom_hash256_saltsize32', '"
                + hash("PBKDF2WithHmacSHA256", 2048, 32, 32, "secret1") + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('tom_hash512_saltsize16', '"
                + hash("PBKDF2WithHmacSHA512", 1024, 16, 16, "secret1") + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('tom_hash512_saltsize32', '"
                + hash("PBKDF2WithHmacSHA512", 2048, 32, 16, "secret1") + "')");

        for (String name : new String[] { "tom_hash256_saltsize32", "tom_hash512_saltsize16", "tom_hash512_saltsize32" }) {
            executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('" + name + "', 'Administrator')");
            executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('" + name + "', 'Manager')");
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            executeUpdate(dataSource, "DROP TABLE IF EXISTS caller");
            executeUpdate(dataSource, "DROP TABLE IF EXISTS caller_groups");
        } catch (Exception ignore) {
        }
    }

    private static String hash(String algorithm, int iterations, int saltSizeBytes, int keySizeBytes, String password) {
        Pbkdf2PasswordHash pbkdf2 = CDI.current().select(Pbkdf2PasswordHash.class).get();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Algorithm", algorithm);
        parameters.put("Pbkdf2PasswordHash.Iterations", Integer.toString(iterations));
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", Integer.toString(saltSizeBytes));
        parameters.put("Pbkdf2PasswordHash.KeySizeBytes", Integer.toString(keySizeBytes));
        pbkdf2.initialize(parameters);
        return pbkdf2.generate(password.toCharArray());
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
