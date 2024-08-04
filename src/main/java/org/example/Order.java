package org.example;
public class Order {
    private static int orderIdCounter = 1;
    private int orderId;
    private String fioClient;
    private String email;
    private String carModel;
    private String VINCar;
    private String status = "";
    private int cost;

    public Order(String fioClient, String email, String carModel, String VINCar, String status, int cost) {
        this.orderId = orderIdCounter++;
        this.fioClient = fioClient;
        this.email = email;
        this.carModel = carModel;
        this.VINCar = VINCar;
        this.status = status;
        this.cost = cost;
    }

    public String getVINCar() {
        return VINCar;
    }

    public void setVINCar(String VINCar) {
        this.VINCar = VINCar;
    }

    public String getFioClient() {
        return fioClient;
    }

    public void setFioClient(String fioClient) {
        this.fioClient = fioClient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getEmail() {
        return email;
    }

    public String getCarModel() {
        return carModel;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId + ", Customer's full name: " + fioClient + ", Email: " + email + ", Car Model: " + carModel + ", Order status: " + status + ", order price: " + cost;
    }
}
