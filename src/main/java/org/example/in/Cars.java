package org.example.in;
public class Cars {
    private String brand;
    private String model;
    private int year;
    private String VIN;
    private int price;

    public Cars(String brand, String model, int year, String VIN, int price) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.VIN = VIN;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Brand: " + brand + ", Model: " + model + ", Year of manufacture: " + year + ", VIN: " + VIN + ", Price: " + price;
    }
}
