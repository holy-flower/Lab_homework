package org.example.out.repositories;
import org.example.in.*;
import org.example.out.mappers.SearchCars;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ManagersInterface {
    //private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {

    }

    public static void managerInterface(String email) {
        System.out.println("Menu: ");
        System.out.println("1. User Management");
        System.out.println("2. Car management");
        System.out.println("3. Order Management");
        System.out.println("4. Log out of your account");

        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            UsersManagement.managers(email);
            managerInterface(email);
        }
        else if (choice == 2) {
            SearchCars.managersSearch(email);
            managerInterface(email);
        }
        else if (choice == 3) {
            ordersManagementAdmin(email);
            managerInterface(email);
        } else if (choice == 4) {
            Main.firstList();
        } else {
            System.out.println("Enter the number listed");
            managerInterface(email);
        }
    }

    public static void ordersManagement(String email, Runnable nextMethod) {
        System.out.println("Menu: ");
        System.out.println("1. List of all orders");
        System.out.println("2. Changing the order status");
        System.out.println("3. Back");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();

        if (choice == 1) {
            try (Connection connection = DatabaseManager.getConnection()) {
                ResultSet resultSet = TableOrders.retriveRecords(connection);
                TableOrders.printRecords(resultSet);
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }


            /*
            if (!orderList.isEmpty()){
                orderList.forEach(System.out::println);
            } else {
                System.out.println("No orders yet");
            }
             */
        } else if (choice == 2) {
            System.out.println("Enter the order ID you would like to change: ");
            int changeOrder = Main.scanner.nextInt();
            Main.scanner.nextLine();

            try (Connection connection = DatabaseManager.getConnection()) {
                String query = "SELECT * FROM orders WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, changeOrder);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Enter the new order status (reservation, awaiting payment, order, on the way, arrived at the warehouse): ");
                    String newStatus = Main.scanner.nextLine();

                    String updateQuery = "UPDATE orders SET status = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, newStatus);
                    updateStatement.setInt(2, changeOrder);
                    updateStatement.executeUpdate();

                    logger.info("The manager " + email + " has changed the status of the order №" + changeOrder);
                    System.out.println("Order status successfully updated");
                } else {
                    System.out.println("The order with the specified ID was not found.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }



            /*
            for (Order order : orderList) {
                if (changeOrder == order.getOrderId()) {
                    System.out.println("Enter the new order status (reservation, awaiting payment, order, on the way, arrived at the warehouse): ");
                    String newStatus = Main.scanner.nextLine();
                    order.setStatus(newStatus);

                    logger.info("The manager " + email + " has changed the status of the order №" + changeOrder);
                    System.out.println("Order status successfully updated");
                }
            }
             */
        } else if (choice == 3) {
            nextMethod.run();
        } else {
            System.out.println("Enter the number listed");
            ordersManagement(email, nextMethod);
        }
    }

    public static void ordersManagementAdmin(String email) {
        ordersManagement(email, () -> AdministratorsInterface.adminInterface(email));
    }

    public static void ordersManagementManager(String email) {
        ordersManagement(email, () -> managerInterface(email));
    }

    public static void setScanner(Scanner scanner) {
    }
}
