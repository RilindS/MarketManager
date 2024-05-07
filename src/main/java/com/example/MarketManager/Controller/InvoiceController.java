package com.example.MarketManager.Controller;

import com.example.MarketManager.model.Product;
import com.example.MarketManager.Service.InvoiceService;
import com.example.MarketManager.model.Invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
public class InvoiceController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/invoicesHtml")
    public String generateInvoicesHtml() {
        String productsUrl = "http://localhost:8080/products";
        Product[] productsArray = restTemplate.getForObject(productsUrl, Product[].class);

        List<Product> products = Arrays.asList(productsArray);

        List<Invoice> invoices = invoiceService.calculateInvoices(products);

        return invoiceService.generateHTMLTable(invoices);
    }

    @GetMapping("/invoicesJson")
    public String generateInvoicesJson() {
        String productsUrl = "http://localhost:8080/products";
        Product[] productsArray = restTemplate.getForObject(productsUrl, Product[].class);

        List<Product> products = Arrays.asList(productsArray);

        List<Invoice> invoices = invoiceService.calculateInvoices(products);

        return invoiceService.generateJSON(invoices);
    }
}