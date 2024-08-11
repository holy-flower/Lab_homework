package org.example.in;

import java.sql.*;

public class TableUsers {
    public static void main(User user) {
        try (Connection connection = DatabaseManager.getConnection()) {
            createUserTable(connection);
            insertRecords(connection, user);
            ResultSet resultSet = retriveRecords(connection);
            printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void printRecords(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String name = resultSet.getString("fullName");
            int role = resultSet.getInt("role");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            System.out.println("name: " + name + ", role: " + role +
                    ", age: " + age + ", email: " + email + ", password: " + password);
        }
    }

    static ResultSet retriveRecords(Connection connection) throws SQLException {
        String retrieveUsersSql = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(retrieveUsersSql);
        return resultSet;
    }

    static boolean insertRecords(Connection connection, User user) throws  SQLException {
        String checkExistSql = "SELECT * FROM users WHERE email = ? OR fullName = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkExistSql)) {
            checkStatement.setString(1, user.getEmail());
            checkStatement.setString(2, user.getFio());

            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Error: User with this email or full name already exists");
                return false;
            }
        }

        String insertDataSql = "INSERT INTO users (fullName, role, age, email, password) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, user.getFio());
        preparedStatement.setInt(2, user.getRole());
        preparedStatement.setInt(3, user.getAge());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, new String(user.getPassword()));
        preparedStatement.executeUpdate();
        return true;
    }

    static void createUserTable(Connection connection) throws SQLException {
        String createSequenceSql = "CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1";
        String tableUsersSql = "CREATE TABLE IF NOT EXISTS users (" +
                "fullName VARCHAR(255), " +
                "role INT, " +
                "age INT, " +
                "email VARCHAR(255), " +
                "password VARCHAR(255), " +
                "additionalInfo VARCHAR(255))";
        Statement statement = connection.createStatement();
        statement.execute(createSequenceSql);
        statement.execute(tableUsersSql);
    }

    public static boolean validateUserCredentials(String email, char[] password) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String validateSql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(validateSql);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, new String(password));

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static int getUserRoleByEmail(String email) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT role FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("role");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }
}
