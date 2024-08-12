package org.example.in;

import java.sql.*;

public class TableCars {
    public static void main(Cars cars) {
        try (Connection connection = DatabaseManager.getConnection()) {
            createCarTable(connection);
            insertRecords(connection, cars);
            ResultSet resultSet = retriveRecords(connection);
            printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }
    public static void printRecords(ResultSet resultSet) throws SQLException {
        System.out.println("Updated list of cars: ");
        while (resultSet.next()) {
            String brand = resultSet.getString("brand");
            String model = resultSet.getString("model");
            int year = resultSet.getInt("year");
            String VIN = resultSet.getString("VIN");
            int price = resultSet.getInt("price");
            System.out.println("brand: " + brand + ", model: " + model +
                    ", year: " + year + ", VIN: " + VIN + ", price: " + price);
        }
    }

    public static ResultSet retriveRecords(Connection connection) throws SQLException {
        String retrieveCarsSql = "SELECT * FROM cars";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(retrieveCarsSql);
        return resultSet;
    }

    private static void insertRecords(Connection connection, Cars cars) throws SQLException {
        String insertDataSql = "INSERT INTO cars (brand, model, year, VIN, price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSql);
        preparedStatement.setString(1, cars.getBrand());
        preparedStatement.setString(2, cars.getModel());
        preparedStatement.setInt(3, cars.getYear());
        preparedStatement.setString(4, cars.getVIN());
        preparedStatement.setInt(5, cars.getPrice());
        preparedStatement.executeUpdate();
    }

    private static void createCarTable(Connection connection) throws SQLException {
        String createSequenceSql = "CREATE SEQUENCE IF NOT EXISTS private.cars_seq START WITH 1 INCREMENT BY 1";
        String tableCarsSql = "CREATE TABLE IF NOT EXISTS private.cars (" +
                "brand VARCHAR(255), " +
                "model VARCHAR(255), " +
                "year INT, " +
                "VIN VARCHAR(255), " +
                "price INT)";
        Statement statement = connection.createStatement();
        statement.execute(createSequenceSql);
        statement.execute(tableCarsSql);
    }
}
