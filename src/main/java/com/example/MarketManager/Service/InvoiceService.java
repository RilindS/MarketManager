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

        for (Product product : products) {
            while (product.getQuantity() > 0) {
                boolean productAdded = false;

                if (product.getPrice() > 500) {
                    Invoice newInvoice = new Invoice();
                    int quantityToAdd = Math.min(product.getQuantity(), 50);
                    Product productToAdd = new Product(product);
                    productToAdd.setQuantity(quantityToAdd);
                    newInvoice.addProduct(productToAdd);
                    invoices.add(newInvoice);
                    product.setQuantity(product.getQuantity() - quantityToAdd);
                    continue;
                }

                for (Invoice invoice : invoices) {
                    if (invoice.containsProduct(product) && invoice.getProductQuantity(product) >= 50) {
                        continue;
                    }
                    if ((invoice.getTotal() + calculateTotal(product)) <= 500) {
                        double productTotal = calculateTotal(product);
                        if (invoice.getTotal() + productTotal <= 500) {
                            int remainingQuantity = 50 - invoice.getProductQuantity(product);
                            int quantityToAdd = Math.min(product.getQuantity(), remainingQuantity);
                            if (quantityToAdd > 0) {
                                Product productToAdd = new Product(product);
                                productToAdd.setQuantity(quantityToAdd);
                                invoice.addProduct(productToAdd);
                                product.setQuantity(product.getQuantity() - quantityToAdd);
                                productAdded = true;
                            }
                        }
                    }
                }

                if (!productAdded) {
                    int quantityToAdd = Math.min(product.getQuantity(), 50);
                    Invoice newInvoice = new Invoice();
                    Product productToAdd = new Product(product);
                    productToAdd.setQuantity(quantityToAdd);
                    newInvoice.addProduct(productToAdd);
                    invoices.add(newInvoice);
                    product.setQuantity(product.getQuantity() - quantityToAdd);
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

    public double calculateTotal(Product product) {

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
            double subtotal = 0;
            for (Product product : invoice.getProducts()) {
                double productTotal = calculateTotal(product);
                double productVAT = calculateVAT(product);
                double productSubtotal = productTotal - productVAT;
                html.append("<tr>")
                        .append("<td>").append(product.getDescription()).append("</td>")
                        .append("<td>").append(product.getQuantity()).append("</td>")
                        .append("<td>").append(product.getPrice()).append("</td>")
                        .append("<td>").append(product.getDiscount()).append("</td>")
                        .append("<td>").append(product.getVat()).append("</td>")
                        .append("<td>").append(productSubtotal).append("+")
                        .append(calculateVAT(product)).append("=").append(productTotal)
                        .append("</td>")
                        .append("</tr>");
                subtotal += productSubtotal;
            }
            double vatTotal = calculateVAT(invoice.getProducts());
            double invoiceTotal = subtotal + vatTotal;
            html.append("<tr><td colspan='5'>Subtotal</td><td>").append(subtotal).append("</td></tr>")
                    .append("<tr><td colspan='5'>VAT</td><td>").append(vatTotal).append("</td></tr>")
                    .append("<tr><td colspan='5'>Total</td><td>").append(invoiceTotal).append("</td></tr>");
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
            double subtotal = 0.0;
            for (Product product : invoice.getProducts()) {
                ObjectNode productNode = objectMapper.createObjectNode();
                productNode.put("description", product.getDescription());
                productNode.put("quantity", product.getQuantity());
                productNode.put("price", product.getPrice());
                productNode.put("discount", product.getDiscount());
                productNode.put("vat", product.getVat());

                double productTotal = calculateTotal(product);
                double productVAT = calculateVAT(product);
                double productSubtotal = productTotal - productVAT;

                productNode.put("subtotal", productSubtotal);
                productNode.put("vat", productVAT);
                productNode.put("total", productTotal);

                subtotal += productSubtotal;
                invoiceTotal += productTotal;

                productsArray.add(productNode);
            }

            invoiceNode.set("products", productsArray);

            invoiceNode.put("subtotal", subtotal);
            double vatTotal = calculateVAT(invoice.getProducts());
            invoiceNode.put("vat", vatTotal);
            invoiceNode.put("total", invoiceTotal);

            invoicesArray.add(invoiceNode);
        }

        return invoicesArray.toString();
    }

    private double calculateVAT(List<Product> products) {
        double totalVAT = 0.0;
        for (Product product : products) {
            totalVAT += calculateVAT(product);
        }
        return totalVAT;
    }

}
