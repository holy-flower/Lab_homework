import org.example.in.Cars;
import org.example.in.DatabaseManager;
import org.example.in.Order;
import org.example.in.User;
import org.example.out.repositories.AdministratorsInterface;
import org.example.out.mappers.SearchCars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Testcontainers
public class AdministratorsInterfaceTest {
    @Container
    public GenericContainer<?> container = new GenericContainer<>("mysql:8.0.26").withExposedPorts(3306);
    private List<User> userList;
    private List<Cars> carList;
    private List<Order> orderList;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        userList = new ArrayList<>();
        carList = new ArrayList<>();
        orderList = new ArrayList<>();

        scanner = new Scanner(System.in);
        SearchCars.setScanner(scanner);
    }

    @Test
    public void testClientRegistration() {
        Cars car1 = new Cars("Brand1", "Model1", 2020, "1HGBH41JXMN109186", 20000);
        Cars car2 = new Cars("Brand2", "Model2", 2022, "1G1YZ23J9P5803427", 25000);
        carList.add(car1);
        carList.add(car2);

        Order order1 = new Order("Aleksandr Sergeevich Pushkin", "Pushkin@mail.ru", "Model3", "WWD2867772A989264040", "reservation", 34000);
        Order order2 = new Order("Aleksey Alekseevich Block", "Block@mail.ru", "Model4", "NND2170056A9139040", "on the way", 99000);
        orderList.add(order1);
        orderList.add(order2);

        String address = container.getHost();
        Integer port = container.getMappedPort(3306);

        try (Connection connection = DatabaseManager.getConnection()) {
            for (Cars car : carList) {
                String insertUserSql = "INSERT INTO cars (brand, model, year, VIN, price) VALUES (?, ?, ?, ?, ?)";
                try (var preparedStatement = connection.prepareStatement(insertUserSql)) {
                    preparedStatement.setString(1, car.getBrand());
                    preparedStatement.setString(2, car.getModel());
                    preparedStatement.setInt(3, car.getYear());
                    preparedStatement.setString(4, car.getVIN());
                    preparedStatement.setInt(5, car.getPrice());
                    preparedStatement.executeUpdate();
                }
            }

            for (Order order : orderList) {
                String insertOrderSql = "INSERT INTO orders (fioClient, email, carModel, VINCar, status, cost) VALUES (?, ?, ?, ?, ?, ?)";
                try (var preparedStatement = connection.prepareStatement(insertOrderSql)) {
                    preparedStatement.setString(1, order.getFioClient());
                    preparedStatement.setString(2, order.getEmail());
                    preparedStatement.setString(3, order.getCarModel());
                    preparedStatement.setString(4, order.getVINCar());
                    preparedStatement.setString(5, order.getStatus());
                    preparedStatement.setInt(6, order.getCost());
                    preparedStatement.executeUpdate();
                }
            }

            System.out.println("Users inserted into the database successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String email = "Anna@mail.ru";

        String simulatedInput = "2\n1\nbrandNew\nModelNew\n2024\n1ADDH49JXUT100006\n26000\n3\n1G1YZ23J9P5803427\n\n4\n4\n1\n3\nLev Nikholaevich Tolstoy\n4\n5\nexit";
        InputStream originalInput = System.in;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes())) {
            System.setIn(bais);
            scanner = new Scanner(System.in);
            AdministratorsInterface.setScanner(scanner);

            AdministratorsInterface.adminInterface(email);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalInput);
        }
    }
}
