package org.example.in;
import org.example.out.mappers.ClientsInterface;
import org.example.out.repositories.ManagersInterface;
import org.example.out.mappers.SearchCars;
import org.example.out.repositories.AdministratorsInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static List<User> userList = new ArrayList<>();
    private static List<Cars> carList;
    private static List<Order> orderList;
    public static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {
        firstList(carList, orderList);
    }

    public static void firstList(List<Cars> carList, List<Order> orderList) {
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

            User storedUser = findUserByEmail(email);
            boolean enty = false;

            while (enty == false) {
                if (storedUser != null && Arrays.equals(password, storedUser.getPassword())) {
                    System.out.println("The login was completed successfully");

                    enty = true;
                    int roleUser = storedUser.getRole();
                    if (roleUser == 1) {
                        logger.info ("Administrator " + email + "logged in to your account");
                        AdministratorsInterface.adminInterface(userList, carList, orderList, email);
                    } else if (roleUser == 2) {
                        logger.info("The manager " + email + " logged in to your account");
                        ManagersInterface.managerInterface(userList, carList, orderList, email);
                    } else if (roleUser == 3){
                        logger.info ("Client " + email + "logged in to your account");
                        ClientsInterface.clientInterface(email, userList, carList);
                    }
                } else {
                    System.out.println("Invalid email or password");
                    System.out.println("Enter email: ");
                    email = scanner.nextLine();
                    System.out.println("Enter password:");
                    password = scanner.nextLine().toCharArray();
                    storedUser = findUserByEmail(email);
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

                    AdministratorsInterface.adminInterface(userList, carList, orderList, email);
                }
                else if (role == 2) {
                    flag = false;
                    System.out.println("You have chosen the role of manager");
                    String email = registration(role);
                    logger.info("The manager " + email + " registered in the application");

                    ManagersInterface.managerInterface(userList, carList, orderList, email);
                }
                else if (role == 3) {
                    flag = false;
                    System.out.println("You have chosen the role of the client");
                    String email = registration(role);
                    logger.info ("Client " + email + "registered in the application");

                    ClientsInterface.clientInterface(email, userList, carList);
                }
                else if (role == 4) {
                    firstList(carList, orderList);
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
            firstList(carList, orderList);
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
        userList.add(newUser);
        System.out.println("The user has been successfully registered.");

        return email;
    }

    public static String checkingEmail() {
        String email = "";
        scanner.nextLine();

        while (true) {
            System.out.println("Enter email: ");
            email = scanner.nextLine();
            boolean isEmailUnique = true;
            boolean isEmailValid = email.endsWith("@mail.ru") || email.endsWith("@gmail.com");

            if (!isEmailValid) {
                System.out.println("Email must end with @mail.ru or @gmail.com . Please enter the email again.");
                continue;
            }

            for (User user : userList) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    isEmailUnique = false;
                    break;
                }
            }
            if (isEmailUnique) {
                break;
            } else {
                System.out.println("A user with this email already exists. Please enter a different email address.");
            }
        }
        return email;
    }

    public static String checkingName() {
        String name = "";
        scanner.nextLine();

        while (true) {
            System.out.println("Enter your full name: ");
            name = scanner.nextLine();
            boolean isNameUnique = true;
            boolean isNameValid = name.trim().split("\\s+").length == 3;

            if(!isNameValid) {
                System.out.println("Full name should consist of three words. Please enter your full name again.");                continue;
            }

            for (User user : userList) {
                if (user.getFio().equalsIgnoreCase(name)) {
                    isNameUnique = false;
                    break;
                }
            }
            if (isNameUnique) {
                break;
            } else {
                System.out.println("A user with this full name already exists. Are you already registered?");
            }
        }
        return name;
    }

    public static User findUserByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static void setScanner(Scanner scanner) {
        Main.scanner = scanner;
    }
}