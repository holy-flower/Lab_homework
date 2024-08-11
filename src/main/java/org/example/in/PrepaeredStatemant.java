package org.example.in;

import java.sql.*;

public class PrepaeredStatemant {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pass";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createTable(connection);
            insertRecords(connection);
            ResultSet resultSet = retriveRecords(connection);
            printRecords(resultSet);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static void printRecords(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
        }
    }

    private static ResultSet retriveRecords(Connection connection) throws SQLException {
        String retriveStudentSql = "SELECT * FROM students";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(retriveStudentSql);
        return resultSet;
    }

    private static void insertRecords(Connection connection) throws SQLException {
        String insertDataSql = "INSERT INTO students (name, age) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, "Ivan");
        preparedStatement.setInt(2, 20);
        preparedStatement.executeUpdate();
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "age INT)";
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);
    }
}
