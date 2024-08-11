package org.example.out.mappers;
import org.example.in.*;
import org.example.out.mappers.ClientsInterface;
import org.example.out.repositories.ManagersInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SearchCars {
    //private static Scanner scanner = new Scanner(System.in);
    public static void searchCars(String email, Runnable nextMethod){
        System.out.println("Select a car search option: ");
        System.out.println("1. By year of manufacture");
        System.out.println("2. According to the client");
        System.out.println("3. By order status");
        System.out.println("4. According to the model");
        System.out.println("5. Back");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();
        String query;
        PreparedStatement preparedStatement;

        try (Connection connection = DatabaseManager.getConnection()) {
            if (choice == 1) {
                System.out.println("Enter the year you are interested in: ");
                int yearCar = Main.scanner.nextInt();

                query = "SELECT * FROM cars WHERE year = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, yearCar);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    do {
                        int id = resultSet.getInt("id");
                        String brand = resultSet.getString("brand");
                        String model = resultSet.getString("model");
                        int year = resultSet.getInt("year");
                        String VIN = resultSet.getString("VIN");
                        int price = resultSet.getInt("price");
                        System.out.println("ID: " + id + ", brand: " + brand + ", model: " + model +
                                ", year: " + year + ", VIN: " + VIN + ", price: " + price);
                    } while (resultSet.next());
                } else {
                    System.out.println("No cars with this year of manufacture were found.");
                }
                searchCars(email, nextMethod);
            } else if (choice == 2) {
                System.out.println("Enter the full name of the client you are interested in: ");
                String client = Main.scanner.nextLine();

                query = "SELECT * FROM orders WHERE fioClient = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, client);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    do {
                        int id = resultSet.getInt("id");
                        String brand = resultSet.getString("brand");
                        String model = resultSet.getString("model");
                        int year = resultSet.getInt("year");
                        String VIN = resultSet.getString("VIN");
                        int price = resultSet.getInt("price");
                        System.out.println("ID: " + id + ", brand: " + brand + ", model: " + model +
                                ", year: " + year + ", VIN: " + VIN + ", price: " + price);
                    } while (resultSet.next());
                } else {
                    System.out.println("No client with this full name was found.");
                }
                searchCars(email, nextMethod);
            } else if (choice == 3) {
                System.out.println("Enter the status of the order you are interested in (reservation, awaiting payment, order, on the way, arrived at the warehouse): ");
                String statusOrder = Main.scanner.nextLine();

                query = "SELECT * FROM orders WHERE status = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, statusOrder);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    do {
                        int id = resultSet.getInt("id");
                        String brand = resultSet.getString("brand");
                        String model = resultSet.getString("model");
                        int year = resultSet.getInt("year");
                        String VIN = resultSet.getString("VIN");
                        int price = resultSet.getInt("price");
                        System.out.println("ID: " + id + ", brand: " + brand + ", model: " + model +
                                ", year: " + year + ", VIN: " + VIN + ", price: " + price);
                    } while (resultSet.next());
                } else {
                    System.out.println("No order with this status was found.");
                }
                searchCars(email, nextMethod);
            } else if (choice == 4) {
                System.out.println("Enter the model you are interested in: ");
                String modelCar = Main.scanner.nextLine();

                query = "SELECT * FROM cars WHERE model = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, modelCar);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    do {
                        int id = resultSet.getInt("id");
                        String brand = resultSet.getString("brand");
                        String model = resultSet.getString("model");
                        int year = resultSet.getInt("year");
                        String VIN = resultSet.getString("VIN");
                        int price = resultSet.getInt("price");
                        System.out.println("ID: " + id + ", brand: " + brand + ", model: " + model +
                                ", year: " + year + ", VIN: " + VIN + ", price: " + price);
                    } while (resultSet.next());
                } else {
                    System.out.println("No cars of this model were found.");
                }
                searchCars(email, nextMethod);
            } else if (choice == 5) {
                nextMethod.run();
            } else {
                System.out.println("Enter the number listed");
                searchCars(email, nextMethod);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }








        /*
        if (choice == 1) {
            System.out.println("Enter the year you are interested in: ");
            int yearCar = Main.scanner.nextInt();
            List<Cars> filteredByYear = carList.stream()
                    .filter(person -> person.getYear() == yearCar).collect(Collectors.toList());
            if (!filteredByYear.isEmpty()) {
                filteredByYear.forEach(System.out::println);
            } else {
                System.out.println("No cars with this year of manufacture were found.");
            }
            searchCars(email, nextMethod);

        } else if (choice == 2) {
            System.out.println("Enter the full name of the client you are interested in: ");
            String client = Main.scanner.nextLine();
            List<Order> filteredByClient = orderList.stream().filter(person -> client.equals(person.getFioClient())).collect(Collectors.toList());
            if (!filteredByClient.isEmpty()) {
                filteredByClient.forEach(System.out::println);
            } else {
                System.out.println("No client with this full name was found.");
            }
            searchCars(email, nextMethod);

        } else if (choice == 3) {
            System.out.println("Enter the status of the order you are interested in (reservation, awaiting payment, order, on the way, arrived at the warehouse): ");
            String statusOrder = Main.scanner.nextLine();
            List<Order> filteredByStatus = orderList.stream().filter(person -> statusOrder.equals(person.getStatus())).collect(Collectors.toList());
            if (!filteredByStatus.isEmpty()) {
                filteredByStatus.forEach(System.out::println);
            } else {
                System.out.println("No order with this status was found.");
            }
            searchCars(email, nextMethod);

        } else if (choice == 4) {
            System.out.println("Enter the model you are interested in: ");
            String modelCar = Main.scanner.nextLine();
            List<Cars> filteredByModel = carList.stream().filter(person -> modelCar.equals(person.getModel())).collect(Collectors.toList());
            if (!filteredByModel.isEmpty()) {
                filteredByModel.forEach(System.out::println);
            } else {
                System.out.println("No cars of this model were found.");
            }
            searchCars(email, nextMethod);

        } else if (choice == 5) {
            nextMethod.run();
        } else {
            System.out.println("Enter the number listed");
            searchCars(email, nextMethod);
        }
         */
    }

    public static void clientsSearch(String email) {
        searchCars(email, () -> ClientsInterface.catalog(email));
    }

    public static void managersSearch(String email) {
        searchCars(email, () -> ManagersInterface.managerInterface(email));
    }

    public static void setScanner(Scanner scanner) {
    }
}
