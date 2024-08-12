package org.example.out.repositories;
import org.example.in.*;
import org.example.out.repositories.AdministratorsInterface;
import org.example.out.repositories.ManagersInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UsersManagement {
    public static void main(String[] args) {

    }

    public static void userManagement(Runnable nextMethod, String email) {
        System.out.println("Menu: ");
        System.out.println("1. Viewing clients");
        System.out.println("2. Customer filtering");
        System.out.println("3. Sorting clients");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();
        int k = 0;

        if (choice == 1) {
            try (Connection connection = DatabaseManager.getConnection()) {
                String query = "SELECT * FROM users WHERE role = 3";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString("fullName");
                    int role = resultSet.getInt("role");
                    int age = resultSet.getInt("age");
                    String userEmail = resultSet.getString("email");

                    System.out.println("name: " + name + ", role: " + role +
                            ", age: " + age + ", email: " + userEmail);
                }
                userManagement(nextMethod, email);
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        } else if (choice == 2) {
            filteredUsers(email, nextMethod);
            userManagement(nextMethod, email);
        }
        else if (choice == 3) {
            sortedUsers(email, nextMethod);
            userManagement(nextMethod, email);
        }
        else if (choice == 4) {
            nextMethod.run();
        } else {
            System.out.println("Enter the number listed");
            userManagement(nextMethod, email);
        }
    }

    public static void managers(String email) {
        userManagement(() -> ManagersInterface.managerInterface(email), email);
    }

    public static void admin(String email) {
        userManagement(() -> AdministratorsInterface.adminInterface(email), email);
    }


    public static void filteredUsers(String email, Runnable nextMethod) {
        System.out.println("Select the user filtering criteria: ");
        System.out.println("1. By full name");
        System.out.println("2. By age");
        System.out.println("3. By the number of purchases");
        System.out.println("4. Back");

        int choice = Main.scanner.nextInt();
        Main.scanner.nextLine();

        String query = "";
        PreparedStatement preparedStatement = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            if (choice == 1) {
                filteringByName(connection, query, preparedStatement);
                filteredUsers(email, nextMethod);
            } else if (choice == 2) {
                filteringByAge(connection, query, preparedStatement);
                filteredUsers(email, nextMethod);
            } else if (choice == 3) {
                filteringByPurchases(connection, query, preparedStatement);
                filteredUsers(email, nextMethod);
            } else if (choice == 4) {
                userManagement(nextMethod, email);
            } else {
                System.out.println("Enter the number listed");
                filteredUsers(email, nextMethod);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void filteringByName(Connection connection, String query, PreparedStatement preparedStatement) throws SQLException {
        System.out.println("Enter the name you are interested in: ");
        String fioClient = Main.scanner.nextLine();

        query = "SELECT * FROM users WHERE fullName = ? AND role = 3";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, fioClient);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            do {
                String name = resultSet.getString("fullName");
                int role = resultSet.getInt("role");
                int age = resultSet.getInt("age");
                String userEmail = resultSet.getString("email");

                System.out.println("name: " + name + ", role: " + role +
                        ", age: " + age + ", email: " + userEmail);
            } while (resultSet.next());
        } else {
            System.out.println("Full name data not found.");
        }
    }

    public static void filteringByAge(Connection connection, String query, PreparedStatement preparedStatement) throws SQLException {
        System.out.println("Enter the age you are interested in: ");
        int ageClient = Main.scanner.nextInt();

        query = "SELECT * FROM users WHERE age = ? AND role = 3";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ageClient);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            do {
                String name = resultSet.getString("fullName");
                int role = resultSet.getInt("role");
                int age = resultSet.getInt("age");
                String userEmail = resultSet.getString("email");

                System.out.println("name: " + name + ", role: " + role +
                        ", age: " + age + ", email: " + userEmail);
            } while (resultSet.next());
        } else {
            System.out.println("No clients with the selected age were found.");
        }
    }

    public static void filteringByPurchases(Connection connection, String query, PreparedStatement preparedStatement) throws SQLException {
        System.out.println("Enter the number of purchases you are interested in: ");
        int numOfPurchases = Main.scanner.nextInt();

        Map<String, Integer> clientOrderCountMap = new HashMap();
        String orderQuery = "SELECT fioClient, COUNT(*) as orderCount FROM orders GROUP BY fioClient";
        preparedStatement = connection.prepareStatement(orderQuery);
        ResultSet orderResultSet = preparedStatement.executeQuery();

        while (orderResultSet.next()) {
            String clientFio = orderResultSet.getString("fioClient");
            int orderCount = orderResultSet.getInt("orderCount");
            clientOrderCountMap.put(clientFio, orderCount);
        }

        List<ClientOrderCount> clientOrderCountList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : clientOrderCountMap.entrySet()) {
            clientOrderCountList.add(new ClientOrderCount(entry.getKey(), entry.getValue()));
        }

        List<User> filteredByPerchases = new ArrayList<>();
        query = "SELECT * FROM users WHERE fullName = ? AND role = 3";
        PreparedStatement userStatement = connection.prepareStatement(query);

        for (ClientOrderCount clientOrderCount : clientOrderCountList) {
            if (numOfPurchases == clientOrderCount.getOrderCount()) {
                userStatement.setString(1, clientOrderCount.getClientName());
                try (ResultSet userResultSet = userStatement.executeQuery()) {
                    while (userResultSet.next()) {
                        User user = new User();
                        user.setFio(userResultSet.getString("fio"));
                        user.setEmail(userResultSet.getString("email"));
                        user.setRole(userResultSet.getInt("role"));
                        filteredByPerchases.add(user);
                    }
                }
            }
        }

        if (!filteredByPerchases.isEmpty()) {
            filteredByPerchases.forEach(System.out::println);
        } else {
            System.out.println("Клиенты с таким количеством заказов не найдены.");
            return;
        }
    }

    public static void sortedUsers(String email, Runnable nextMethod) {
        System.out.println("Select the sort type: ");
        System.out.println("1. By full name");
        System.out.println("2. By age");
        System.out.println("3. By the number of purchases");
        System.out.println("4. Back");

        int choise = Main.scanner.nextInt();
        String query = "";
        PreparedStatement preparedStatement = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            if (choise == 1) {
                query = "SELECT * FROM users WHERE role = 3 ORDER BY fullName";
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    System.out.println("name: " + resultSet.getString("fullName") +
                            ", role: " + resultSet.getInt("role") +
                            ", age: " + resultSet.getInt("age") +
                            ", email: " + resultSet.getString("email"));
                }
                sortedUsers(email, nextMethod);
            } else if (choise == 2) {
                query = "SELECT * FROM users WHERE role = 3 ORDER BY age";
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    System.out.println("name: " + resultSet.getString("fullName") +
                            ", role: " + resultSet.getInt("role") +
                            ", age: " + resultSet.getInt("age") +
                            ", email: " + resultSet.getString("email"));
                }
                sortedUsers(email, nextMethod);
            } else if (choise == 3) {
                sortedByPurchases(connection, query, preparedStatement);
                sortedUsers(email, nextMethod);
            } else if (choise == 4) {
                userManagement(nextMethod, email);
            } else {
                System.out.println("Enter the number listed");
                sortedUsers(email, nextMethod);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void sortedByPurchases(Connection connection, String query, PreparedStatement preparedStatement) throws SQLException {
        Map<String, Integer> clientOrderCountMap = new HashMap<>();
        String orderQuery = "SELECT fioClient FROM orders";
        PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
        ResultSet orderResultSet = orderStatement.executeQuery();

        while (orderResultSet.next()) {
            String clientFio = orderResultSet.getString("fioClient");
            clientOrderCountMap.put(clientFio, clientOrderCountMap.getOrDefault(clientFio, 0) + 1);
        }

        List<ClientOrderCount> clientOrderCountList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : clientOrderCountMap.entrySet()) {
            clientOrderCountList.add(new ClientOrderCount(entry.getKey(), entry.getValue()));
        }

        List<ClientOrderCount> sortedClientOrderCount = clientOrderCountList.stream()
                .sorted(Comparator.comparing(ClientOrderCount::getOrderCount))
                .collect(Collectors.toList());

        List<User> sortedByPurchases = new ArrayList<>();
        query = "SELECT * FROM users WHERE role = 3";
        preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            for (ClientOrderCount clientOrderCount : sortedClientOrderCount) {
                if (clientOrderCount.getClientName().equals(resultSet.getString("fullName"))) {
                    sortedByPurchases.add(new User(resultSet.getString("fullName"), resultSet.getInt("role"),
                            resultSet.getInt("age"), resultSet.getString("email"),
                            resultSet.getString("password").toCharArray()));
                }
            }
        }
        sortedByPurchases.forEach(System.out::println);
    }
}
