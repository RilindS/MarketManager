package com.example.MarketManager.model;

public class Product {
    private String description;
    private int quantity;
    private double price;
    private double vat;
    private double discount;

    public Product(String description, int quantity, double price, double vat) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vat = vat;
        this.discount = 0; // Default discount is 0
    }

    public Product(String description, int quantity, double price, double vat, double discount) {
        this(description, quantity, price, vat);
        this.discount = discount;
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
}
