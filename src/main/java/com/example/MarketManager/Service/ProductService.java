package com.example.MarketManager.Service;

import com.example.MarketManager.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public static List<Product> readProductsFromJsonFile(String filename) throws IOException {
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {
        }.getType();

        try {
            FileReader reader = new FileReader(filename);
            return gson.fromJson(reader, productListType);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            throw e;
        } catch (IOException e) {

            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        }
    }
}
