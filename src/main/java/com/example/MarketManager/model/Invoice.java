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

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotal() {
        return total;
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

    /*
     * @Override
     * public String toString() {
     * StringBuilder sb = new StringBuilder();
     * sb.append("Invoice:\n");
     * for (Product product : products) {
     * sb.append("Description: ").append(product.getDescription()).append(", ");
     * sb.append("Quantity: ").append(product.getQuantity()).append(", ");
     * sb.append("Price: ").append(product.getPrice()).append(", ");
     * sb.append("VAT: ").append(product.getVat()).append(", ");
     * sb.append("Discount: ").append(product.getDiscount()).append("\n");
     * }
     * sb.append("Total: ").append(total).append("\n");
     * return sb.toString();
     * }
     */
}
