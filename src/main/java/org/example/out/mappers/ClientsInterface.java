package org.example.out.mappers;
import org.example.in.Cars;
import org.example.in.Main;
import org.example.in.Order;
import org.example.in.User;

import java.util.*;
import java.util.logging.Logger;

public class ClientsInterface {
    private static List<Order> orderList = new ArrayList<>();
    //private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {
    }

    public static void clientInterface(String email, List<User> userList, List<Cars> carList) {
        System.out.println("Menu: ");
        System.out.println("1. Catalog view");
        System.out.println("2. Personal account");
        System.out.println("3. Order Management");
        System.out.println("4. Log out of your account");

        int change = Main.scanner.nextInt();
        if (change == 1) {
            if (carList != null && !carList.isEmpty()) {
                catalog(email, userList, carList);
            } else {
                System.out.println("No cars yet");
                clientInterface(email, userList, carList);
            }
        }
        else if (change == 2) {
            personalAccount(email, userList);
            clientInterface(email, userList, carList);
        }
        else if (change == 3) {
            orders(email, userList, carList);
            clientInterface(email, userList, carList);
        } else if (change == 4) {
            Main.firstList(carList, orderList);
        } else {
            System.out.println("Select a number from the list");
            clientInterface(email, userList, carList);
        }
    }

    public static void catalog(String email, List<User> userList, List<Cars> carList) {
        System.out.println("Car Catalog:");
        for (Cars car : carList) {
            System.out.println("Brand: " + car.getBrand() +
                    ", Model: " + car.getModel() +
                    ", Year of manufacture: " + car.getYear() +
                    ", VIN: " + car.getVIN() + ", Price: " + car.getPrice());
        }

        System.out.println("Menu: ");
        System.out.println("1. Search for a car by criteria");
        System.out.println("2. Back");

        int choice = Main.scanner.nextInt();

        if (choice == 1) {
            SearchCars.clientsSearch(userList, carList, orderList, email);
        } else if (choice == 2) {
            clientInterface(email, userList, carList);
        } else {
            System.out.println("Enter the number listed");
            catalog(email, userList, carList);
        }
    }

    public static void personalAccount(String email, List<User> userList) {
        System.out.println("My data: ");
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                System.out.println("Full name: " + user.getFio() +
                        " , Age: " + user.getAge() +
                        " , Email: " + user.getEmail());
            }
        }
    }

    public static void orders(String email, List<User> userList, List<Cars> carList) {
        System.out.println("Menu: ");
        System.out.println("1. Create an order");
        System.out.println("2. Tracking my orders");
        System.out.println("3. Cancel the order");
        System.out.println("4. Apply for car maintenance");
        System.out.println("5. Back");

        int change = Main.scanner.nextInt();

        if (change == 1) {
            createOrder(email, userList, carList);
            orders(email, userList, carList);
        } else if (change == 2) {
            trackOrders(email);
            orders(email, userList, carList);
        } else if (change == 3) {
            cancelOrder(email);
            orders(email, userList, carList);
        } else if (change == 4) {
            serviceCar(email, userList, carList);
        } else if (change == 5) {
            clientInterface(email, userList, carList);
        } else {
            System.out.println("Select a number from the list");
            orders(email, userList, carList);
        }
    }

    public static void serviceCar(String email, List<User> userList, List<Cars> carList) {
        System.out.println("Enter the VIN of the car: ");
        Main.scanner.nextLine();
        String carVIN = Main.scanner.nextLine();
        String userFio = searchClient(email, userList);

        if (!carList.isEmpty()) {
            boolean carFound = false;
            for (Order order : orderList) {
                if (order.getEmail().equals(email)) {
                    for (Cars car : carList) {
                        if (carVIN.equals(car.getVIN())) {
                            Order serviceOrder = new Order(userFio, email, car.getModel(), carVIN, "Отправлен на обслуживание", car.getPrice());
                            orderList.add(serviceOrder);
                            logger.info ("User " + email + "sent the car " + carVIN + " for maintenance");
                            carFound = true;
                            break;
                        }
                    }
                }
                if (carFound) {
                    break;
                }
            }
            if (!carFound) {
                System.out.println("The car with this VIN was not found.");
            }
        } else {
            System.out.println("No cars available");
        }
    }

    public static void createOrder(String email, List<User> userList, List<Cars> carList) {
        System.out.println("Enter the VIN of the car: ");
        Main.scanner.nextLine();
        String carVIN = Main.scanner.nextLine();
        String userFio = searchClient(email, userList);

        if (!carList.isEmpty()) {
            boolean carFound = false;
            for (Cars car : carList) {
                if (carVIN.equals(car.getVIN())) {
                    if (isCarOrdered(carVIN)) {
                        System.out.println("This car has already been ordered.");
                    } else {
                        Order order = new Order(userFio, email, car.getModel(), carVIN, "Reservation", car.getPrice());
                        orderList.add(order);
                        logger.info ("User " + email + " ordered a car " + carVIN);
                        carFound = true;
                    }
                    break;
                }
            }
            if (!carFound) {
                System.out.println("The car with this VIN was not found.");
            }
        } else {
            System.out.println("No cars available");
        }
    }

    private static boolean isCarOrdered(String carVIN) {
        for (Order order : orderList) {
            if (order.getVINCar().equals(carVIN)) {
                return true;
            }
        }
        return false;
    }

    public static String searchClient(String email, List<User> userList) {
        for (User user : userList) {
            if (email.equals(user.getEmail())){
                return user.getFio();
            }
        }
        return "";
    }

    private static void trackOrders(String email) {
        System.out.println("Your orders:");
        if (!orderList.isEmpty()) {
            for (Order order : orderList) {
                if (order.getEmail().equals(email)) {
                    System.out.println(order);
                }
            }
        } else {
            System.out.println("You don't have any orders yet.");
        }
    }

    private static void cancelOrder(String email) {
        if (!orderList.isEmpty()) {
            System.out.println("Enter the order ID to cancel it:");
            int orderId = Main.scanner.nextInt();
            boolean orderFound = false;

            Iterator<Order> iterator = orderList.iterator();
            while (iterator.hasNext()) {
                Order order = iterator.next();
                if (order.getEmail().equals(email) && order.getOrderId() == orderId) {
                    iterator.remove();
                    System.out.println("Order cancelled: " + order);
                    logger.info ("User " + email + " cancelled car order " + order.getVINCar());
                    orderFound = true;
                    break;
                }
            }
            if (!orderFound) {
                System.out.println("The order with the specified ID was not found.");
            }
        } else {
            System.out.println("You don't have any orders yet.");
        }
    }

    public static void setScanner(Scanner scanner) {
    }
}
