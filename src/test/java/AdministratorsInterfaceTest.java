import org.example.in.Cars;
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

        Order order1 = new Order("Aleksandr Sergeevich Pushkin", "Pushkin@mail.ru", "Model3", "WWD2174772A0022040", "reservation", 34000);
        Order order2 = new Order("Aleksey Alekseevich Block", "Block@mail.ru", "Model4", "NND2176798A9019040", "on the way", 99000);
        orderList.add(order1);
        orderList.add(order2);

        User user1 = new User("Lev Nikholaevich Tolstoy", 2, 35, "person@mail.ru", "1111".toCharArray());
        User user2 = new User("Aleksey Alekseevich Block", 3, 37, "Block@mail.ru", "4522".toCharArray());
        User user3 = new User("Aleksandr Sergeevich Pushkin", 3, 26, "Pushkin@mail.ru", "9573".toCharArray());
        User user4 = new User("Anna Andreevna Ahmatova", 1, 38, "Anna@mail.ru", "8533".toCharArray());
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

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
