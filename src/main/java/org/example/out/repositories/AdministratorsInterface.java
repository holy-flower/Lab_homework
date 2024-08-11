package org.example.out.repositories;
import org.example.in.*;
import org.example.out.mappers.CarManagement;
import org.example.out.mappers.SearchCars;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AdministratorsInterface {
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {
    }

    public static void adminInterface(String email) {
        System.out.println("Menu: ");
        System.out.println("1. User Management");
        System.out.println("2. Car management");
        System.out.println("3. Order Management");
        System.out.println("4. Managing managers");
        System.out.println("5. Log out of your account");

        //Scanner scanner = new Scanner(System.in);
        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            UsersManagement.admin(email);
        }
        else if (choice == 2) {
            CarManagement.carManagement(email);
        }
        else if (choice == 3) {
            ManagersInterface.ordersManagementAdmin(email);
        }
        else if (choice == 4) {
            managersManagement(email);
        }
        else if (choice == 5) {
            Main.firstList();
        }
        else {
            System.out.println("Select a number from the list");
            adminInterface(email);
        }
    }

    public static void managersManagement(String email) {
        System.out.println("Menu: ");
        System.out.println("1. Viewing the list of managers");
        System.out.println("2. Change the information about the manager");
        System.out.println("3. Add information about the manager");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();
        String query;
        PreparedStatement preparedStatement;

        try (Connection connection = DatabaseManager.getConnection()) {
            switch (choice) {
                case 1:
                    query = "SELECT * FROM users WHERE role = 2";
                    preparedStatement = connection.prepareStatement(query);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String name = resultSet.getString("fullName");
                        int role = resultSet.getInt("role");
                        int age = resultSet.getInt("age");
                        String userEmail = resultSet.getString("email");

                        System.out.println("name: " + name + ", role: " + role +
                                ", age: " + age + ", email: " + userEmail);
                    }
                    managersManagement(email);
                    break;
                case 2:
                    System.out.println("Enter the full name of the manager whose information you want to change: ");
                    String managerFio = Main.scanner.nextLine();
                    changeManagerList(managerFio, email);
                    System.out.println("Manager information has been successfully changed");
                    break;
                case 3:
                    Main.scanner.nextLine();

                    System.out.println("Enter the full name of the manager you want to add information about: ");
                    String managerName = Main.scanner.nextLine();

                    System.out.println("Enter additional information about the manager: ");
                    String additionalInfo = Main.scanner.nextLine();

                    query = "UPDATE users SET additionalInfo = ? WHERE fullName = ? AND role = 2";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, additionalInfo);
                    preparedStatement.setString(2, managerName);

                    try {
                        int rowAffected = preparedStatement.executeUpdate();
                        if (rowAffected > 0) {
                            System.out.println("Additional information about the manager has been successfully added");
                        } else {
                            System.out.println("The manager with this name was not found.");
                        }
                    } catch (SQLException e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    managersManagement(email);
                    break;
                case 4:
                    adminInterface(email);
                    break;
                default:
                    System.out.println("Select a number from the list");
                    managersManagement(email);
                    return;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }



        /*
        if (choice == 1) {
            List<User> managersList = userList.stream().filter(person -> person.getRole() == 2).collect(Collectors.toList());
            managersList.forEach(System.out::println);
            managersManagement(userList, carList, orderList, email);
        } else if (choice == 2) {
            System.out.println("Enter the full name of the manager whose information you want to change: ");
            String managerFio = Main.scanner.nextLine();
            changeManagerList(managerFio, carList, orderList, email);
            System.out.println("Manager information has been successfully changed");
        } else if (choice == 3) {
            System.out.println("Enter the full name of the manager you want to add information about: ");
            String managerName = Main.scanner.nextLine();

            System.out.println("Enter additional information about the manager: ");
            String additionalInfo = Main.scanner.nextLine();

            boolean updated = false;
            for (User user : userList) {
                if (managerName.equals(user.getFio()) && user.getRole() == 2) {
                    user.setAdditionalInfo(additionalInfo);

                    System.out.println("Additional information about the manager has been successfully added");
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                System.out.println("The manager with this name was not found.");
            }
            managersManagement(carList, orderList, email);

        } else if (choice == 4) {
            adminInterface(carList, orderList, email);
        } else {
            System.out.println("Select a number from the list");
            managersManagement(carList, orderList, email);
        }
         */
    }

    public static void changeManagerList(String managerFio, String email) {
        System.out.println("What information do you want to change: ");
        System.out.println("1. Full name");
        System.out.println("2. The role");
        System.out.println("3. Email");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();

        try (Connection connection = DatabaseManager.getConnection()) {
            String query;
            PreparedStatement preparedStatement;

            switch (choice) {
                case 1:
                    System.out.println("Enter a new full name: ");
                    String newName = Main.scanner.nextLine();
                    query = "UPDATE users SET fullName = ? WHERE fullName = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newName);
                    preparedStatement.setString(2, managerFio);
                    preparedStatement.executeUpdate();

                    logger.info ("Administrator " + email + " changed the manager's full name (earlier) " + managerFio + " to " + newName);
                    break;
                case 2:
                    System.out.println("Enter a new role (1. Administrator; 2. Manager; 3. Client): ");
                    int newRole = Main.scanner.nextInt();
                    query = "UPDATE users SET role = ? WHERE fullName = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, newRole);
                    preparedStatement.setString(2, managerFio);
                    preparedStatement.executeUpdate();

                    logger.info ("Administrator " + email + " changed the manager role to " + newRole);
                    break;
                case 3:
                    System.out.println("Enter a new email: ");
                    String newEmail = Main.scanner.nextLine();
                    query = "UPDATE users SET email = ? WHERE fullName = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, managerFio);
                    preparedStatement.executeUpdate();

                    logger.info ("Administrator " + email + " changed manager's email " + managerFio + " to " + newEmail);
                    break;
                case 4:
                    managersManagement(email);
                    break;
                default:
                    System.out.println("Select a number from the list");
                    changeManagerList(managerFio, email);
                    return;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }



        /*
        for (User user : userList) {
            if (managerFio.equals(user.getFio())) {
                if (choice == 1) {
                    System.out.println("Enter a new full name: ");
                    String newName = Main.scanner.nextLine();
                    user.setFio(newName);

                    logger.info ("Administrator " + email + " changed the manager's full name (earlier) " + managerFio + " to " + newName);
                } else if (choice == 2) {
                    System.out.println("Enter a new role (1. Administrator; 2. Manager; 3. Client): ");
                    int newRole = Main.scanner.nextInt();
                    user.setRole(newRole);

                    logger.info ("Administrator " + email + " changed the manager role to " + newRole);

                } else if (choice == 3) {
                    System.out.println("Enter a new email: ");
                    String newEmail = Main.scanner.nextLine();
                    user.setEmail(newEmail);

                    logger.info ("Administrator " + email + " changed manager's email " + managerFio + " to " + newEmail);

                } else if (choice == 4) {
                    managersManagement(carList, orderList, email);
                } else {
                    System.out.println("Select a number from the list");
                    changeManagerList(managerFio, carList, orderList, email);
                }
            }
        }
         */
    }

    public static void setScanner(Scanner scanner) {
    }
}
