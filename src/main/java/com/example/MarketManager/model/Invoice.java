package com.example.MarketManager.model;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private List<Product> products;
    private double total;

    public Invoice() {
        this.products = new ArrayList<>();
        this.total = 0;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (Product product : products) {
            totalQuantity += product.getQuantity();
        }
        return totalQuantity;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotal() {
        return total;
    }

    public int getProductQuantity(Product product) {
        int quantity = 0;
        for (Product p : products) {
            if (p.getDescription().equals(product.getDescription())) {
                quantity += p.getQuantity();
            }
        }
        return quantity;
    }

    public boolean hasSpaceForProduct(Product product) {
        double totalInvoiceAmount = calculateTotal();
        double productAmount = product.getQuantity() * product.getPrice();
        return totalInvoiceAmount + productAmount <= 500;
    }

    private double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getQuantity() * product.getPrice();
        }
        return total;
    }

    public int getProductCount() {
        return this.products.size();
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
