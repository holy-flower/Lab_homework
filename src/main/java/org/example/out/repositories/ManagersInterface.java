package org.example.out.repositories;
import org.example.in.Cars;
import org.example.in.Main;
import org.example.in.Order;
import org.example.in.User;
import org.example.out.mappers.SearchCars;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ManagersInterface {
    //private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {

    }

    public static void managerInterface(List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        System.out.println("Menu: ");
        System.out.println("1. User Management");
        System.out.println("2. Car management");
        System.out.println("3. Order Management");
        System.out.println("4. Log out of your account");

        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            UsersManagement.managers(userList, carList, orderList, email);
            managerInterface(userList, carList, orderList, email);
        }
        else if (choice == 2) {
            SearchCars.managersSearch(userList, carList, orderList, email);
            managerInterface(userList, carList, orderList, email);
        }
        else if (choice == 3) {
            ordersManagementAdmin(userList, carList, orderList, email);
            managerInterface(userList, carList, orderList, email);
        } else if (choice == 4) {
            Main.firstList(carList, orderList);
        } else {
            System.out.println("Enter the number listed");
            managerInterface(userList, carList, orderList, email);
        }
    }

    public static void ordersManagement(List<User> userList, List<Cars> carList, List<Order> orderList, String email, Runnable nextMethod) {
        System.out.println("Menu: ");
        System.out.println("1. List of all orders");
        System.out.println("2. Changing the order status");
        System.out.println("3. Back");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();

        if (choice == 1) {
            if (!orderList.isEmpty()){
                orderList.forEach(System.out::println);
            } else {
                System.out.println("No orders yet");
            }
        } else if (choice == 2) {
            System.out.println("Enter the order ID you would like to change: ");
            int changeOrder = Main.scanner.nextInt();
            Main.scanner.nextLine();

            for (Order order : orderList) {
                if (changeOrder == order.getOrderId()) {
                    System.out.println("Enter the new order status (reservation, awaiting payment, order, on the way, arrived at the warehouse): ");
                    String newStatus = Main.scanner.nextLine();
                    order.setStatus(newStatus);

                    logger.info("The manager " + email + " has changed the status of the order №" + changeOrder);
                    System.out.println("Order status successfully updated");
                }
            }
        } else if (choice == 3) {
            nextMethod.run();
        } else {
            System.out.println("Enter the number listed");
            ordersManagement(userList, carList, orderList, email, nextMethod);
        }
    }

    public static void ordersManagementAdmin(List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        ordersManagement(userList, carList, orderList, email, () -> AdministratorsInterface.adminInterface(userList, carList, orderList, email));
    }

    public static void ordersManagementManager(List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        ordersManagement(userList, carList, orderList, email, () -> managerInterface(userList, carList, orderList, email));
    }

    public static void setScanner(Scanner scanner) {
    }
}
