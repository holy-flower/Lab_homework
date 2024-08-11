package org.example.in;
import org.example.out.mappers.ClientsInterface;
import org.example.out.repositories.ManagersInterface;
import org.example.out.mappers.SearchCars;
import org.example.out.repositories.AdministratorsInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    //public static List<User> userList = new ArrayList<>();
    //private static List<Cars> carList;
    //private static List<Order> orderList;
    public static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {
        firstList();
    }

    public static void firstList() {
        System.out.println("Do you want to log in or register?");
        System.out.println("1. Log in");
        System.out.println("2. Register");
        System.out.println("3. Close the application");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Enter your email address: ");
            String email = scanner.nextLine();
            System.out.println("Enter the password: ");
            char[] password = scanner.nextLine().toCharArray();
            boolean enty = false;

            while (enty == false) {
                if (TableUsers.validateUserCredentials(email, password)) {
                    System.out.println("The login was completed successfully");
                    int roleUser = TableUsers.getUserRoleByEmail(email);
                    enty = true;

                    if (roleUser == 1) {
                        logger.info("Administrator " + email + "logged in to your account");
                        AdministratorsInterface.adminInterface(email);
                    } else if (roleUser == 2) {
                        logger.info("The manager " + email + " logged in to your account");
                        ManagersInterface.managerInterface(email);
                    } else if (roleUser == 3) {
                        logger.info("Client " + email + "logged in to your account");
                        ClientsInterface.clientInterface(email);
                    } else {
                        System.out.println("the role was not found");
                    }
                } else {
                    System.out.println("Invalid email or password");
                    System.out.println("Enter email: ");
                    email = scanner.nextLine();
                    System.out.println("Enter password:");
                    password = scanner.nextLine().toCharArray();
                }
            }
        }
        else if (choice == 2) {
            boolean flag = true;

            while (flag == true){
                System.out.println("Select a role: ");
                System.out.println("1. The Administrator");
                System.out.println("2. The Manager");
                System.out.println("3. The Client");
                System.out.println("4. Go back");
                int role = scanner.nextInt();

                if (role == 1) {
                    flag = false;
                    System.out.println("You have chosen the administrator role");
                    String email = registration(role);
                    logger.info ("Administrator " + email + "registered in the application");

                    AdministratorsInterface.adminInterface(email);
                }
                else if (role == 2) {
                    flag = false;
                    System.out.println("You have chosen the role of manager");
                    String email = registration(role);
                    logger.info("The manager " + email + " registered in the application");

                    ManagersInterface.managerInterface(email);
                }
                else if (role == 3) {
                    flag = false;
                    System.out.println("You have chosen the role of the client");
                    String email = registration(role);
                    logger.info ("Client " + email + "registered in the application");

                    ClientsInterface.clientInterface(email);
                }
                else if (role == 4) {
                    firstList();
                    flag = false;
                } else {
                    System.out.println("Enter the number listed");
                    flag = true;
                }
            }
        } else if (choice == 3) {
            System.exit(0);
        } else {
            System.out.println("Enter the number listed");
            firstList();
        }
    }

    public static String registration(int role) {
        String name = checkingName();

        System.out.println("Enter age: ");
        int age = scanner.nextInt();
        String email = checkingEmail();

        System.out.println("Enter password:");
        char[] password = scanner.nextLine().toCharArray();

        User newUser = new User(name, role, age, email, password);

        try (Connection connection = DatabaseManager.getConnection()) {
            TableUsers.createUserTable(connection);

            if (TableUsers.insertRecords(connection, newUser)) {
                ResultSet resultSet = TableUsers.retriveRecords(connection);
                TableUsers.printRecords(resultSet);

                System.out.println("The user has been successfully registered.");
            } else {
                registration(role);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return email;
    }

    public static String checkingEmail() {
        String email = "";
        scanner.nextLine();

        while (true) {
            System.out.println("Enter email: ");
            email = scanner.nextLine();
            boolean isEmailValid = email.endsWith("@mail.ru") || email.endsWith("@gmail.com");

            if (!isEmailValid) {
                System.out.println("Email must end with @mail.ru or @gmail.com . Please enter the email again.");
            } else {
                return email;
            }

        }
    }

    public static String checkingName() {
        String name = "";
        scanner.nextLine();

        while (true) {
            System.out.println("Enter your full name: ");
            name = scanner.nextLine();
            boolean isNameValid = name.trim().split("\\s+").length == 3;

            if(!isNameValid) {
                System.out.println("Full name should consist of three words. Please enter your full name again.");
            } else {
                return name;
            }
        }
    }

    public static void setScanner(Scanner scanner) {
        Main.scanner = scanner;
    }
}