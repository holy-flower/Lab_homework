package org.example.in;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionExample {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pass";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
            String createTableSQL = "CREATE TABLE pupils (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) UNIQUEQ NOT NULL";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            String insert1 = "INSERT INTO pupils (name) VALUES ('ALICE')";
            String insert2 = "INSERT INTO pupils (name) VALUES ('BOB')";

            statement.executeUpdate(insert1);
            statement.executeUpdate(insert2);
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                exception.getMessage();
            }
        }
    }
}
