package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class CarManagement {
    private static List<Cars> carList = new ArrayList<>();
    //private static Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SearchCars.class.getName());

    public static void main(String[] args) {

    }

    public static void carManagement(List<User> userList, List<Order> orderList, String email) {
        System.out.println("Menu:");
        System.out.println("1. Add a car");
        System.out.println("2. Change the information about the car");
        System.out.println("3. Remove the car");
        System.out.println("4. Back");
        int change = Main.scanner.nextInt();

        if (change == 1) {
            addCars(carList, email);
            carManagement(userList, orderList, email);
        }
        else if (change == 2) {
            modificationCar(carList, email);
            carManagement(userList, orderList, email);
        }
        else if (change == 3) {
            deleteCar(carList, email);
            carManagement(userList, orderList, email);
        }
        else if (change == 4) {
            AdministratorsInterface.adminInterface(userList, carList, orderList, email);
        } else {
            System.out.println("Select a number from the list");
            carManagement(userList, orderList, email);
        }
    }

    public static void addCars(List<Cars> carList, String email) {
        Main.scanner.nextLine();
        System.out.println("Enter vehicle information: ");

        System.out.println("Brand: ");
        String brand = Main.scanner.nextLine();

        System.out.print("Model: ");
        String model = Main.scanner.nextLine();

        System.out.print("Year of manufacture: ");
        int year = Main.scanner.nextInt();

        System.out.print("Identified Number (VIN): ");
        Main.scanner.nextLine();
        String VIN = "";
        while (VIN.length() != 17 || !isValidVIN(VIN)) {
            VIN = Main.scanner.nextLine();
            if (VIN.length() != 17 || !isValidVIN(VIN)) {
                System.out.println("Invalid VIN. Please enter again.");
            }
        }

        System.out.print("Price: ");
        int price = Main.scanner.nextInt();

        Cars car = new Cars(brand, model, year, VIN, price);
        carList.add(car);

        logger.info("The manager " + email + " added the car " + VIN + " to the catalog");

        System.out.println("Updated list of cars:");
        for (Cars cars : carList) {
            System.out.println("Brand: " + cars.getBrand() +
                    ", Model: " + cars.getModel() +
                    ", Year of manufacture: " + cars.getYear() +
                    ", VIN: " + cars.getVIN() + ", Price: " + cars.getPrice());
        }
    }

    // Метод для проверки корректности VIN
    private static boolean isValidVIN(String VIN) {
        if (VIN.length() != 17) {
            return false;
        }
        for (char c : VIN.toCharArray()) {
            if (!Character.isDigit(c) && !(c >= 'A' && c <= 'Z')) {
                return false;
            }
            if (c == 'I' || c == 'O' || c == 'Q') {
                return false;
            }
        }
        return true;
    }

    private static void modificationCar(List<Cars> carList, String email) {
        System.out.print("Enter the VIN of the car you want to change: ");
        Main.scanner.nextLine();
        String vinToUpdate = Main.scanner.nextLine();
        Main.scanner.nextLine();

        if (!carList.isEmpty()) {
            Cars carToUpdate = findCarByVIN(carList, vinToUpdate);
            if (carToUpdate != null) {
                System.out.println("Enter new data for the car:");

                System.out.print("Brand: ");
                String newBrand = Main.scanner.nextLine();
                carToUpdate.setBrand(newBrand);

                System.out.print("Model: ");
                String newModel = Main.scanner.nextLine();
                carToUpdate.setModel(newModel);

                System.out.print("Year of manufacture: ");
                int newYear = Main.scanner.nextInt();
                Main.scanner.nextLine();
                carToUpdate.setYear(newYear);

                System.out.print("Price: ");
                int newPrice = Main.scanner.nextInt();
                Main.scanner.nextLine();
                carToUpdate.setPrice(newPrice);

                logger.info("The manager " + email + " changed the information about the car " + vinToUpdate);

                System.out.println("Vehicle information has been successfully updated.");
            } else {
                System.out.println("Автомобиль с VIN " + vinToUpdate + " не найден.");
            }
        } else {
            System.out.println("No cars yet");
        }

        System.out.println("Updated list of cars:");
        for (Cars cars : carList) {
            System.out.println("Brand: " + cars.getBrand() +
                    ", Model: " + cars.getModel() +
                    ", Year of manufacture: " + cars.getYear() +
                    ", VIN: " + cars.getVIN() + ", Price: " + cars.getPrice());
        }
    }

    public static void deleteCar(List<Cars> carList, String email) {
        System.out.print("Enter the VIN of the car you want to delete: ");
        Main.scanner.nextLine();
        String vinToDelete = Main.scanner.nextLine();
        Main.scanner.nextLine();

        if(!carList.isEmpty()) {
            Cars carToDelete = findCarByVIN(carList, vinToDelete);
            if (carToDelete != null) {
                carList.remove(carToDelete);

                logger.info("The manager " + email + " deleted the car " + vinToDelete + " from the catalog");
                System.out.println("Car with VIN " + vinToDelete + "successfully deleted.");
            } else {
                System.out.println("Car with VIN " + vinToDelete + "not found.");            }
        } else {
            System.out.println("No cars yet");
        }

        System.out.println("Updated list of cars:");
        for (Cars cars : carList) {
            System.out.println("Brand: " + cars.getBrand() +
                    ", Model: " + cars.getModel() +
                    ", Year of manufacture: " + cars.getYear() +
                    ", VIN: " + cars.getVIN() + ", Price: " + cars.getPrice());
        }
    }

    // Метод для поиска автомобиля по VIN
    private static Cars findCarByVIN(List<Cars> carList, String VIN) {
        for (Cars car : carList) {
            if (car.getVIN().equals(VIN)) {
                return car;
            }
        }
        return null;
    }
}
