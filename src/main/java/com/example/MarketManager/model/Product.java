package com.example.MarketManager.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    private String description;
    private int quantity;
    private double price;
    private double vat;
    private double discount;

    public Product() {
        // Default constructor needed for Jackson deserialization
    }

    @JsonCreator
    public Product(
            @JsonProperty("description") String description,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("price") double price,
            @JsonProperty("vat") int vat,
            @JsonProperty("discount") double discount) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vat = vat;
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

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", vat=" + vat +
                ", discount=" + discount +
                '}';
    }
}
