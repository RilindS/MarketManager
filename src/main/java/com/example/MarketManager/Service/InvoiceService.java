package com.example.MarketManager.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.MarketManager.model.Invoice;
import com.example.MarketManager.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    public List<Invoice> calculateInvoices(List<Product> products) {
        List<Invoice> invoices = new ArrayList<>();
        double currentInvoiceTotal = 0;

        for (Product product : products) {

            if (product.getPrice() > 500) {
                Invoice newInvoice = new Invoice();
                newInvoice.addProduct(product);
                invoices.add(newInvoice);
            }
            if (true) {
                boolean addedToExistingInvoice = false;
                for (Invoice invoice : invoices) {

                    if (invoice.hasSpaceForProduct(product) && (invoice.getTotal() + calculateTotal(product)) <= 500) {

                        Product productToAdd = new Product(product);
                        int remainingQuantity = 50 - invoice.getProductQuantity(product);
                        if (product.getQuantity() <= remainingQuantity) {

                            productToAdd.setQuantity(product.getQuantity());
                            invoice.addProduct(productToAdd);
                            addedToExistingInvoice = true;
                            break;
                        } else {

                            productToAdd.setQuantity(remainingQuantity);
                            invoice.addProduct(productToAdd);
                            product.setQuantity(product.getQuantity() - remainingQuantity);
                        }
                    }
                }

                if (!addedToExistingInvoice && (currentInvoiceTotal + calculateTotal(product)) <= 500) {

                    Invoice newInvoice = new Invoice();
                    Product productToAdd = new Product(product);
                    int quantityToAdd = Math.min(product.getQuantity(), 50);
                    productToAdd.setQuantity(quantityToAdd);
                    newInvoice.addProduct(productToAdd);
                    invoices.add(newInvoice);

                    if (product.getQuantity() > 50) {
                        product.setQuantity(product.getQuantity() - quantityToAdd);
                    }

                    currentInvoiceTotal += calculateTotal(productToAdd);
                }
            }
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

    public String generateHTMLTable(List<Invoice> invoices) {
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < invoices.size(); i++) {
            html.append("<html><head><title>Invoice ").append(i + 1).append("</title></head><body>");
            html.append("<h2>Invoice ").append(i + 1).append("</h2>");
            html.append("<table border='1'><tr>")
                    .append("<th>Product</th><th>Quantity</th><th>Price</th><th>Discount</th><th>Vat</th><th>Total</th>")
                    .append("</tr>");
            Invoice invoice = invoices.get(i);
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
            html.append("</table></body></html>");
        }
        return html.toString();
    }

    public String generateJSON(List<Invoice> invoices) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode invoicesArray = objectMapper.createArrayNode();

        for (Invoice invoice : invoices) {
            ObjectNode invoiceNode = objectMapper.createObjectNode();
            ArrayNode productsArray = objectMapper.createArrayNode();

            double invoiceTotal = 0.0;
            for (Product product : invoice.getProducts()) {
                ObjectNode productNode = objectMapper.createObjectNode();
                productNode.put("description", product.getDescription());
                productNode.put("quantity", product.getQuantity());
                productNode.put("price", product.getPrice());
                productNode.put("discount", product.getDiscount());
                productNode.put("vat", product.getVat());

                double productTotal = calculateTotal(product);
                productNode.put("total", productTotal);

                invoiceTotal += productTotal;

                productsArray.add(productNode);
            }

            invoiceNode.set("products", productsArray);

            invoiceNode.put("total", invoiceTotal);

            invoicesArray.add(invoiceNode);
        }

        return invoicesArray.toString();
    }

}
