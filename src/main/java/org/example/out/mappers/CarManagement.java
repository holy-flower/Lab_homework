package org.example.out.mappers;
import org.example.in.*;
import org.example.out.repositories.AdministratorsInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CarManagement {
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {

    }

    public static void carManagement(String email) {
        System.out.println("Menu:");
        System.out.println("1. Add a car");
        System.out.println("2. Change the information about the car");
        System.out.println("3. Remove the car");
        System.out.println("4. Back");
        int change = Main.scanner.nextInt();

        if (change == 1) {
            addCars(email);
            carManagement(email);
        }
        else if (change == 2) {
            modificationCar(email);
            carManagement(email);
        }
        else if (change == 3) {
            deleteCar(email);
            carManagement(email);
        }
        else if (change == 4) {
            AdministratorsInterface.adminInterface(email);
        } else {
            System.out.println("Select a number from the list");
            carManagement(email);
        }
    }

    public static void addCars(String email) {
        Main.scanner.nextLine();
        System.out.println("Enter vehicle information: ");

        System.out.println("Brand: ");
        String brand = Main.scanner.nextLine();

        System.out.print("Model: ");
        String model = Main.scanner.nextLine();

        System.out.print("Year of manufacture: ");
        int year = Main.scanner.nextInt();

        System.out.print("Identified Number (VIN): ");
        Main.scanner.nextLine();
        String VIN = "";
        while (VIN.length() != 17 || !isValidVIN(VIN)) {
            VIN = Main.scanner.nextLine();
            if (VIN.length() != 17 || !isValidVIN(VIN)) {
                System.out.println("Invalid VIN. Please enter again.");
            }
        }

        System.out.print("Price: ");
        int price = Main.scanner.nextInt();

        Cars car = new Cars(brand, model, year, VIN, price);
        TableCars.main(car);

        logger.info("The manager " + email + " added the car " + VIN + " to the catalog");
    }

    // Метод для проверки корректности VIN
    private static boolean isValidVIN(String VIN) {
        if (VIN.length() != 17) {
            return false;
        }
        for (char c : VIN.toCharArray()) {
            if (!Character.isDigit(c) && !(c >= 'A' && c <= 'Z')) {
                return false;
            }
            if (c == 'I' || c == 'O' || c == 'Q') {
                return false;
            }
        }
        return true;
    }

    private static void modificationCar(String email) {
        System.out.print("Enter the VIN of the car you want to change: ");
        Main.scanner.nextLine();
        String vinToUpdate = Main.scanner.nextLine();
        Main.scanner.nextLine();

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM cars WHERE VIN = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vinToUpdate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Enter new data for the car:");

                System.out.print("Brand: ");
                String newBrand = Main.scanner.nextLine();

                System.out.print("Model: ");
                String newModel = Main.scanner.nextLine();

                System.out.print("Year of manufacture: ");
                int newYear = Main.scanner.nextInt();
                Main.scanner.nextLine();

                System.out.print("Price: ");
                int newPrice = Main.scanner.nextInt();
                Main.scanner.nextLine();

                String updateQuery = "UPDATE cars SET brand = ?, model = ?, year = ?, price = ? WHERE VIN = ?";
                PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
                updatePstmt.setString(1, newBrand);
                updatePstmt.setString(2, newModel);
                updatePstmt.setInt(3, newYear);
                updatePstmt.setInt(4, newPrice);
                updatePstmt.setString(5, vinToUpdate);
                updatePstmt.executeUpdate();

                logger.info("The manager " + email + " changed the information about the car " + vinToUpdate);

                System.out.println("Vehicle information has been successfully updated.");
            } else {
            System.out.println("The car with the VIN " + vinToUpdate + " was not found.");
            }
            TableCars.printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void deleteCar(String email) {
        System.out.print("Enter the VIN of the car you want to delete: ");
        Main.scanner.nextLine();
        String vinToDelete = Main.scanner.nextLine();
        Main.scanner.nextLine();

        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM cars WHERE VIN = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vinToDelete);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String deleteQuery = "DELETE FROM cars WHERE VIN = ?";
                PreparedStatement deletePstmt = connection.prepareStatement(deleteQuery);
                deletePstmt.setString(1, vinToDelete);
                deletePstmt.executeUpdate();

                logger.info("The manager " + email + " deleted the car " + vinToDelete + " from the catalog");
                System.out.println("Car with VIN " + vinToDelete + "successfully deleted.");
            } else {
                System.out.println("Car with VIN " + vinToDelete + "not found.");
            }

            TableCars.printRecords(resultSet);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
