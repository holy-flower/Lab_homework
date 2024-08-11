package org.example.in;

import java.sql.*;

public class TableOrders {
    public static void main(Order order) {
        try (Connection connection = DatabaseManager.getConnection()) {
            createOrderTable(connection);
            insertRecords(connection, order);
            ResultSet resultSet = retriveRecords(connection);
            printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void printRecords(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("fioClient");
            String email = resultSet.getString("email");
            String carModel = resultSet.getString("carModel");
            String VINCar = resultSet.getString("VINCar");
            String status = resultSet.getString("status");
            int cost = resultSet.getInt("cost");
            System.out.println("ID: " + id + ", name: " + name + ", email: " + email +
                    ", carModel: " + carModel + ", VIN: " + VINCar + ", status: " + status + ", cost: " + cost);
        }
    }

    public static ResultSet retriveRecords(Connection connection) throws SQLException {
        String retrieveOrdersSql = "SELECT * FROM orders";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(retrieveOrdersSql);
        return resultSet;
    }

    private static void insertRecords(Connection connection, Order order) throws SQLException {
        String insertDataSql = "INSERT INTO orders (fioClient, email, carModel, VINCar, status, cost) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, order.getFioClient());
        preparedStatement.setString(2, order.getEmail());
        preparedStatement.setString(3, order.getCarModel());
        preparedStatement.setString(4, order.getVINCar());
        preparedStatement.setString(5, order.getStatus());
        preparedStatement.setInt(6, order.getCost());
        preparedStatement.executeUpdate();
    }

    private static void createOrderTable(Connection connection) throws SQLException {
        String createSequenceSql = "CREATE SEQUENCE IF NOT EXISTS orders_seq START WITH 1 INCREMENT BY 1";
        String tableOrdersSql = "CREATE TABLE IF NOT EXISTS orders (" +
                "id SERIAL PRIMARY KEY, " +
                "fioClient VARCHAR(255), " +
                "email VARCHAR(255), " +
                "carModel VARCHAR(255), " +
                "VINCar VARCHAR(255), " +
                "status VARCHAR(255), " +
                "cost INT)";
        Statement statement = connection.createStatement();
        statement.execute(createSequenceSql);
        statement.execute(tableOrdersSql);
    }
}
