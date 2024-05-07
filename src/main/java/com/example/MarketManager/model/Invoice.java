package com.example.MarketManager.model;

import java.util.ArrayList;
import java.util.List;
import com.example.MarketManager.Service.InvoiceService;

public class Invoice {
    private List<Product> products;
    private double total;
    private InvoiceService invoiceService;

    public Invoice() {
        this.products = new ArrayList<>();
        this.total = 0;
        this.invoiceService = new InvoiceService();
    }

    public boolean containsProduct(Product productToCheck) {
        for (Product product : products) {
            if (product.equals(productToCheck)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotal() {
        double totalAmount = 0.0;
        for (Product product : products) {
            totalAmount += invoiceService.calculateTotal(product);
        }
        return totalAmount;
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
        double totalInvoiceAmount = getTotal();
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
