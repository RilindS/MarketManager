package com.example.MarketManager.Controller;

import com.example.MarketManager.model.Product;
import com.example.MarketManager.model.Invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class InvoiceController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/invoices")
    public String generateInvoices() {

        String productsUrl = "http://localhost:8080/products";
        Product[] productsArray = restTemplate.getForObject(productsUrl, Product[].class);
        List<Product> products = Arrays.asList(productsArray);

        List<Invoice> invoices = calculateInvoices(products);

        return generateHTMLTable(invoices);
    }

    private List<Invoice> calculateInvoices(List<Product> products) {
        List<Invoice> invoices = new ArrayList<>();

        Invoice currentInvoice = new Invoice();
        double currentInvoiceTotal = 0;
        double currentInvoiceVAT = 0;

        for (Product product : products) {

            if (currentInvoiceTotal + product.getPrice() > 500 || currentInvoice.getProducts().size() >= 50) {
                invoices.add(currentInvoice);
                currentInvoice = new Invoice();
                currentInvoiceTotal = 0;
                currentInvoiceVAT = 0;
            }

            currentInvoice.addProduct(product);
            currentInvoiceTotal += product.getPrice();
            currentInvoiceVAT += calculateVAT(product);

            if (product.getPrice() > 500) {
                invoices.add(currentInvoice);
                currentInvoice = new Invoice();
                currentInvoice.addProduct(product);
                invoices.add(currentInvoice);
                currentInvoice = new Invoice();
                currentInvoiceTotal = 0;
                currentInvoiceVAT = 0;
            }
        }

        if (!currentInvoice.getProducts().isEmpty()) {
            invoices.add(currentInvoice);
        }

        return invoices;
    }

    private double calculateVAT(Product product) {

        double vatRate = product.getVat() / 100.0;
        double vatAmount = product.getQuantity() * product.getPrice() * vatRate;
        return vatAmount;
    }

    private double calculateTotal(Product product) {

        double totalPrice = product.getQuantity() * product.getPrice();
        double discountAmount = product.getPrice() * product.getDiscount();
        double totalPriceAfterDiscount = totalPrice - discountAmount;
        double vatAmount = calculateVAT(product);

        double totalPriceWithVAT = totalPriceAfterDiscount + vatAmount;

        return totalPriceWithVAT;
    }

    private double calculatePriceWithoutVat(Product product) {

        double total = product.getQuantity() * product.getPrice();
        return total;
    }

    private String generateHTMLTable(List<Invoice> invoices) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Faturat</title></head><body><table border='1'><tr>")
                .append("<th>Product</th><th>Quantity</th><th>Price</th><th>Discount</th><th>Vat</th><th>Total</th>")
                .append("</tr>");

        for (Invoice invoice : invoices) {
            for (Product product : invoice.getProducts()) {
                html.append("<tr>")
                        .append("<td>").append(product.getDescription()).append("</td>")
                        .append("<td>").append(product.getQuantity()).append("</td>")
                        .append("<td>").append(product.getPrice()).append("</td>")
                        .append("<td>").append(product.getDiscount()).append("</td>")
                        .append("<td>").append(product.getVat()).append("</td>")
                        .append("<td>").append(calculatePriceWithoutVat(product)).append("+")
                        .append(calculateVAT(product)).append("=").append(calculateTotal(product))
                        .append("</td>")
                        .append("</tr>");
            }

        }

        return html.toString();
    }

}
