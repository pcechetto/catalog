package com.example.catalog.tests;

import com.example.catalog.dto.ProductDTO;
import com.example.catalog.entities.Category;
import com.example.catalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Good Phone", 100.0, "https://example.com/img.jpg", Instant.parse("2021-01-01T00:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO() {
        return new ProductDTO(createProduct(), createProduct().getCategories());
    }

    public static Category createCategory() {
        return new Category(2L, "Electronics");
    }
}
