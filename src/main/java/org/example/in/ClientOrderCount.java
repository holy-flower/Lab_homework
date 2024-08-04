package org.example.in;
public class ClientOrderCount {
    private String clientName;
    private int orderCount;

    public ClientOrderCount(String clientName, int orderCount) {
        this.clientName = clientName;
        this.orderCount = orderCount;
    }

    public String getClientName() {
        return clientName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    @Override
    public String toString() {
        return "Client: " + clientName + ", Order Count: " + orderCount;
    }
}
