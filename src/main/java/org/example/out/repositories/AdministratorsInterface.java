package org.example.out.repositories;
import org.example.in.Cars;
import org.example.in.Main;
import org.example.in.Order;
import org.example.in.User;
import org.example.out.mappers.CarManagement;
import org.example.out.mappers.SearchCars;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AdministratorsInterface {
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {
    }

    public static void adminInterface(List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        System.out.println("Menu: ");
        System.out.println("1. User Management");
        System.out.println("2. Car management");
        System.out.println("3. Order Management");
        System.out.println("4. Managing managers");
        System.out.println("5. Log out of your account");

        //Scanner scanner = new Scanner(System.in);
        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            UsersManagement.admin(userList, carList, email, orderList);
        }
        else if (choice == 2) {
            CarManagement.carManagement(userList, orderList, email);
        }
        else if (choice == 3) {
            ManagersInterface.ordersManagementAdmin(userList, carList, orderList, email);
        }
        else if (choice == 4) {
            managersManagement(userList, carList, orderList, email);
        }
        else if (choice == 5) {
            Main.firstList(carList, orderList);
        }
        else {
            System.out.println("Select a number from the list");
            adminInterface(userList, carList, orderList, email);
        }
    }

    public static void managersManagement(List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        System.out.println("Menu: ");
        System.out.println("1. Viewing the list of managers");
        System.out.println("2. Change the information about the manager");
        System.out.println("3. Add information about the manager");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            List<User> managersList = userList.stream().filter(person -> person.getRole() == 2).collect(Collectors.toList());
            managersList.forEach(System.out::println);
            managersManagement(userList, carList, orderList, email);
        } else if (choice == 2) {
            System.out.println("Enter the full name of the manager whose information you want to change: ");
            String managerFio = Main.scanner.nextLine();
            changeManagerList(managerFio, userList, carList, orderList, email);
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
            managersManagement(userList, carList, orderList, email);

        } else if (choice == 4) {
            adminInterface(userList, carList, orderList, email);
        } else {
            System.out.println("Select a number from the list");
            managersManagement(userList, carList, orderList, email);
        }
    }

    public static void changeManagerList(String managerFio, List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        System.out.println("What information do you want to change: ");
        System.out.println("1. Full name");
        System.out.println("2. The role");
        System.out.println("3. Email");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();

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
                    managersManagement(userList, carList, orderList, email);
                } else {
                    System.out.println("Select a number from the list");
                    changeManagerList(managerFio, userList, carList, orderList, email);
                }
            }
        }
    }

    public static void setScanner(Scanner scanner) {
    }
}
