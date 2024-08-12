package org.example.out.mappers;
import org.example.in.*;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class ClientsInterface {
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {
    }

    public static void clientInterface(String email) {
        System.out.println("Menu: ");
        System.out.println("1. Catalog view");
        System.out.println("2. Personal account");
        System.out.println("3. Order Management");
        System.out.println("4. Log out of your account");

        int change = Main.scanner.nextInt();
        if (change == 1) {
            try (Connection connection = DatabaseManager.getConnection()) {
                ResultSet resultSet = TableCars.retriveRecords(connection);
                TableCars.printRecords(resultSet);
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        else if (change == 2) {
            personalAccount(email);
            clientInterface(email);
        }
        else if (change == 3) {
            orders(email);
            clientInterface(email);
        } else if (change == 4) {
            Main.firstList();
        } else {
            System.out.println("Select a number from the list");
            clientInterface(email);
        }
    }

    public static void catalog(String email) {
        System.out.println("Car Catalog:");
        try (Connection connection = DatabaseManager.getConnection()) {
            ResultSet resultSet = TableOrders.retriveRecords(connection);
            TableOrders.printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Menu: ");
        System.out.println("1. Search for a car by criteria");
        System.out.println("2. Back");

        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            SearchCars.clientsSearch(email);
        } else if (choice == 2) {
            clientInterface(email);
        } else {
            System.out.println("Enter the number listed");
            catalog(email);
        }
    }

    public static void personalAccount(String email) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("fullName");
                int role = resultSet.getInt("role");
                int age = resultSet.getInt("age");
                String userEmail = resultSet.getString("email");

                System.out.println("name: " + name + ", role: " + role +
                        ", age: " + age + ", email: " + userEmail);
            } else {
                System.out.println("User not found with email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void orders(String email) {
        System.out.println("Menu: ");
        System.out.println("1. Create an order");
        System.out.println("2. Tracking my orders");
        System.out.println("3. Cancel the order");
        System.out.println("4. Apply for car maintenance");
        System.out.println("5. Back");

        int change = Main.scanner.nextInt();

        if (change == 1) {
            createOrder(email);
            orders(email);
        } else if (change == 2) {
            trackOrders(email);
            orders(email);
        } else if (change == 3) {
            cancelOrder(email);
            orders(email);
        } else if (change == 4) {
            serviceCar(email);
        } else if (change == 5) {
            clientInterface(email);
        } else {
            System.out.println("Select a number from the list");
            orders(email);
        }
    }

    public static void serviceCar(String email) {
        System.out.println("Enter the VIN of the car: ");
        Main.scanner.nextLine();
        String carVIN = Main.scanner.nextLine();
        String userFio = searchClient(email);

        try (Connection connection = DatabaseManager.getConnection()) {
            String carQuery = "SELECT model, price FROM cars WHERE vin = ?";
            PreparedStatement carStatement = connection.prepareStatement(carQuery);
            carStatement.setString(1, carVIN);
            ResultSet carResultSet = carStatement.executeQuery();

            if (carResultSet.next()) {
                String carModel = carResultSet.getString("model");
                int carPrice = carResultSet.getInt("price");

                String orderQuery = "SELECT id FROM orders WHERE VINCar = ?";
                PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
                orderStatement.setString(1, carVIN);

                ResultSet orderResultSet = orderStatement.executeQuery();
                if (orderResultSet.next()) {
                    Order order = new Order(userFio, email, carModel, carVIN, "Sent for maintenance", carPrice);
                    TableOrders.main(order);
                } else {
                    System.out.println("The order with the specified VIN and email was not found.");
                }
            } else {
                System.out.println("The car with this VIN was not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void createOrder(String email) {
        System.out.println("Enter the VIN of the car: ");
        Main.scanner.nextLine();
        String carVIN = Main.scanner.nextLine();
        String userFio = searchClient(email);

        PreparedStatement carStatement = null;
        PreparedStatement orderStatement = null;
        ResultSet carResultSet = null;
        ResultSet orderResultSet = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "orders", null);
            if (!tables.next()) {
                String query = "SELECT model, price FROM cars WHERE VIN = ?";
                carStatement = connection.prepareStatement(query);
                carStatement.setString(1, carVIN);
                carResultSet = carStatement.executeQuery();
                if (carResultSet.next()) {
                    String carModel = carResultSet.getString("model");
                    int carPrice = carResultSet.getInt("price");
                    Order order = new Order(userFio, email, carModel, carVIN, "Reservation", carPrice);
                    TableOrders.main(order);
                    return;
                }
            }

            String carQuery = "SELECT model, price FROM orders WHERE VINCar = ?";
            carStatement = connection.prepareStatement(carQuery);
            carStatement.setString(1, carVIN);
            carResultSet = carStatement.executeQuery();

            if (carResultSet.next()) {
                String carModel = carResultSet.getString("model");
                int carPrice = carResultSet.getInt("cost");

                String orderQuery = "SELECT VINCar FROM orders WHERE VINCar = ?";
                orderStatement = connection.prepareStatement(orderQuery);
                orderStatement.setString(1, carVIN);
                orderResultSet = orderStatement.executeQuery();

                if (orderResultSet.next()) {
                    System.out.println("This car has already been ordered.");
                } else {
                    Order order = new Order(userFio, email, carModel, carVIN, "Reservation", carPrice);
                    TableOrders.main(order);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



    public static String searchClient(String email) {
        String userName = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT fullName FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userName = resultSet.getString("fullName");
            } else {
                System.out.println("User not found with email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return userName;
    }

    private static void trackOrders(String email) {
        System.out.println("Your orders:");
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM orders WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            TableOrders.printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelOrder(String email) {
        try (Connection connection = DatabaseManager.getConnection()) {
            System.out.println("Enter the order ID to cancel it:");
            int orderId = Main.scanner.nextInt();
            boolean orderFound = false;

            String query = "SELECT id FROM orders WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String deleteOrder = "DELETE FROM orders WHERE id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteOrder);
                deleteStatement.setInt(1, orderId);
                deleteStatement.executeUpdate();

                System.out.println("Order cancelled: Order ID " + orderId);
                logger.info ("User " + email + " cancelled car order " + orderId);
                orderFound = true;
            }

            if (!orderFound) {
                System.out.println("The order with the specified ID was not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void setScanner(Scanner scanner) {
    }
}
