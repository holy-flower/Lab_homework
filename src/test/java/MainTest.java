import org.example.in.Main;
import org.example.in.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    private List<User> userList;
    private Scanner scanner;

    @BeforeEach
    public void setUp() {
        userList = new ArrayList<>();

        scanner = new Scanner(System.in);
        Main.setScanner(scanner);
    }

    @Test
    public void testClientRegistration() {
        String simulatedInput = "2\n3\nBob Doe Smith\n25\nbob.doe@gmail.com\npassword123\n4\n3\n";
        InputStream originalInput = System.in;

        try (ByteArrayInputStream bais = new ByteArrayInputStream(simulatedInput.getBytes())) {
            System.setIn(bais);
            scanner = new Scanner(System.in);
            Main.setScanner(scanner);

            Main.firstList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalInput);
        }
    }
}
