import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientsInterfaceTest {
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

        User user = new User("Lev Nikholaevich Tolstoy", 3, 35, "person@mail.ru", "1111".toCharArray());
        userList.add(user);

        String email = "person@mail.ru";

        String simulatedInput = "1\n1\n1\n2020\n2\nAleksey Alekseevich Block\n3\nreservation\n4\nModel1\n5\n2\n2\n3\n1\n1G1YZ23J9P5803427\n2\n4\n1G1YZ23J9P5803427\n3\n3\n5\nexit";
        InputStream originalInput = System.in;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes())) {
            System.setIn(bais);
            scanner = new Scanner(System.in);
            ClientsInterface.setScanner(scanner);

            ClientsInterface.clientInterface(email, userList, carList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalInput);
        }
    }
}
