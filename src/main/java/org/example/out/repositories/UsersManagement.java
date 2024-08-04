package org.example.out.repositories;
import org.example.in.*;
import org.example.out.repositories.AdministratorsInterface;
import org.example.out.repositories.ManagersInterface;

import java.util.*;
import java.util.stream.Collectors;

public class UsersManagement {
    //private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

    }

    public static void userManagement(List<User> userList, List<Cars> carList, List<Order> orderList, Runnable nextMethod, String email) {
        System.out.println("Menu: ");
        System.out.println("1. Viewing clients");
        System.out.println("2. Customer filtering");
        System.out.println("3. Sorting clients");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();
        int k = 0;

        if (choice == 1) {
            if (userList.size() != 0) {
                for (User user : userList) {
                    if (user.getRole() == 3){
                        k++;
                        System.out.println("Full name: " + user.getFio() +
                                " , age: " + user.getAge() +
                                " , email: " + user.getEmail());
                    }
                }
                if (k == 0) {
                    System.out.println("No clients");
                }
            } else {
                System.out.println("No users");
            }
            userManagement(userList, carList, orderList, nextMethod, email);
        }
        else if (choice == 2) {
            filteredUsers(userList, carList, orderList, email, nextMethod);
            userManagement(userList, carList, orderList, nextMethod, email);
        }
        else if (choice == 3) {
            sortedUsers(userList, carList, orderList, email, nextMethod);
            userManagement(userList, carList, orderList, nextMethod, email);
        }
        else if (choice == 4) {
            nextMethod.run();
        } else {
            System.out.println("Enter the number listed");
            userManagement(userList, carList, orderList, nextMethod, email);
        }
    }

    public static void managers(List<User> userList, List<Cars> carList, List<Order> orderList, String email) {
        userManagement(userList, carList, orderList, () -> ManagersInterface.managerInterface(userList, carList, orderList, email), email);
    }

    public static void admin(List<User> userList, List<Cars> carList, String email, List<Order> orderList) {
        userManagement(userList, carList, orderList, () -> AdministratorsInterface.adminInterface(userList, carList, orderList, email), email);
    }


    public static void filteredUsers(List<User> userList, List<Cars> carList, List<Order> orderList, String email, Runnable nextMethod) {
        System.out.println("Select the user filtering criteria: ");
        System.out.println("1. By full name");
        System.out.println("2. By age");
        System.out.println("3. By the number of purchases");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();

        if (choice == 1) {
            System.out.println("Enter the name you are interested in: ");
            String fioClient = Main.scanner.nextLine();
            List<User> filteredByFio = userList.stream().filter(person -> fioClient.equals(person.getFio()) && person.getRole() == 3).collect(Collectors.toList());
            if (filteredByFio != null){
                filteredByFio.forEach(System.out::println);
                filteredUsers(userList, carList, orderList, email, nextMethod);
            } else {
                System.out.println("Full name data not found.");
                filteredUsers(userList, carList, orderList, email, nextMethod);
            }
        }
        else if (choice == 2) {
            System.out.println("Enter the age you are interested in: ");
            int ageClient = Main.scanner.nextInt();
            List<User> filteredByAge = userList.stream().filter(person -> ageClient == person.getAge() && person.getRole() == 3).collect(Collectors.toList());
            if (filteredByAge != null) {
                filteredByAge.forEach(System.out::println);
                filteredUsers(userList, carList, orderList, email, nextMethod);
            }
            else {
                System.out.println("No clients with the selected age were found.");
                filteredUsers(userList, carList, orderList, email, nextMethod);
            }
        }
        else if (choice == 3) {
            System.out.println("Enter the number of purchases you are interested in: ");
            int numOfPurchases = Main.scanner.nextInt();

            Map<String, Integer> clientOrderCountMap = new HashMap();
            for (Order order : orderList){
                String clientFio = order.getFioClient();
                clientOrderCountMap.put(clientFio, clientOrderCountMap.getOrDefault(clientFio, 0) + 1);
            }

            List<ClientOrderCount> clientOrderCountList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : clientOrderCountMap.entrySet()) {
                clientOrderCountList.add(new ClientOrderCount(entry.getKey(), entry.getValue()));
            }

            List<User> filteredByPerchases = new ArrayList<>();
            for (ClientOrderCount clientOrderCount : clientOrderCountList) {
                if (numOfPurchases == clientOrderCount.getOrderCount()) {
                    filteredByPerchases = userList.stream().filter(person -> person.getFio().equals(clientOrderCount.getClientName()) && person.getRole() == 3).collect(Collectors.toList());
                }
            }
            if (!filteredByPerchases.isEmpty()) {
                filteredByPerchases.forEach(System.out::println);
                filteredUsers(userList, carList, orderList, email, nextMethod);
            } else {
                System.out.println("No customers with this number of orders were found.");
            }
        }
        else if (choice == 4) {
            userManagement(userList, carList, orderList, nextMethod, email);
        } else {
            System.out.println("Enter the number listed");
            filteredUsers(userList, carList, orderList, email, nextMethod);
        }
    }

    public static void sortedUsers(List<User> userList, List<Cars> carsList, List<Order> orderList, String email, Runnable nextMethod) {
        System.out.println("Select the sort type: ");
        System.out.println("1. By full name");
        System.out.println("2. By age");
        System.out.println("3. By the number of purchases");
        System.out.println("4. Back");

        int choise = Main.scanner.nextInt();

        if (!userList.isEmpty()) {
            if (choise == 1) {
                List<User> sortedByFio = userList.stream().filter(person -> person.getRole() == 3)
                        .sorted(Comparator.comparing(User::getFio)).collect(Collectors.toList());
                sortedByFio.forEach(System.out::println);
                sortedUsers(userList, carsList, orderList, email, nextMethod);
            } else if (choise == 2) {
                List<User> sortedByAge = userList.stream().filter(person -> person.getRole() == 3).sorted(Comparator.comparingInt(User::getAge)).collect(Collectors.toList());
                sortedByAge.forEach(System.out::println);
                sortedUsers(userList, carsList, orderList, email, nextMethod);
            } else if (choise == 3) {
                Map<String, Integer> clientOrderCountMap = new HashMap();
                for (Order order : orderList){
                    String clientFio = order.getFioClient();
                    clientOrderCountMap.put(clientFio, clientOrderCountMap.getOrDefault(clientFio, 0) + 1);
                }

                List<ClientOrderCount> clientOrderCountList = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : clientOrderCountMap.entrySet()) {
                    clientOrderCountList.add(new ClientOrderCount(entry.getKey(), entry.getValue()));
                }

                List<ClientOrderCount> sortedClientOrderCount = clientOrderCountList.stream().sorted(Comparator.comparing(ClientOrderCount::getOrderCount)).collect(Collectors.toList());

                List<User> sortedByPurchases = new ArrayList<>();
                for (ClientOrderCount clientOrderCount : sortedClientOrderCount) {
                    for (User user : userList) {
                        if (clientOrderCount.getClientName() == user.getFio()) {
                            sortedByPurchases = userList.stream().filter(person -> person.getRole() == 3).sorted(Comparator.comparing(User::getFio)).collect(Collectors.toList());
                        }
                    }
                }
                sortedByPurchases.forEach(System.out::println);
                sortedUsers(userList, carsList, orderList, email, nextMethod);
            } else if (choise == 4) {
                userManagement(userList, carsList, orderList, nextMethod, email);
            } else {
                System.out.println("Enter the number listed");
                sortedUsers(userList, carsList, orderList, email, nextMethod);
            }
        } else {
            System.out.println("No registered users detected");
        }
    }
}
