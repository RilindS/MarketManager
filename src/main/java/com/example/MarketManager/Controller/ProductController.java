package com.example.MarketManager.Controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MarketManager.model.Product;

import com.google.gson.Gson;
import java.io.Reader;
import java.io.FileReader;

@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getProducts() throws IOException {
        String filePath = "C:\\Users\\GJKK\\OneDrive - ubt-uni.net\\Desktop\\MarketManager\\src\\main\\resources\\products.json";

        Gson gson = new Gson();

        Reader reader = new FileReader(filePath);

        List<Product> products = Arrays.asList(gson.fromJson(reader, Product[].class));

        reader.close();

        return products;
    }
}