package com.example.MarketManager.model;

public class Product {
    private String description;
    private int quantity;
    private double price;

    private double discount;
    private double vat;

    public Product(Product other) {
        this.description = other.description;
        this.quantity = other.quantity;
        this.price = other.price;
        this.vat = other.vat;
        this.discount = other.discount;
    }

    public Product(String description, int quantity, double price, double vat, double discount) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;

        this.discount = discount;
        this.vat = vat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +

                ", discount=" + discount +
                ", vat=" + vat +
                '}';
    }
}
