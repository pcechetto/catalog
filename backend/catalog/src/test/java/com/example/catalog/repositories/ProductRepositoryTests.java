package com.example.catalog.repositories;

import com.example.catalog.entities.Product;
import com.example.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long countTotalProducts;

    @BeforeEach
    void setup() {
        existingId = 1L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExists() {
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldSaveProductWithAutoIncrementId() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() {
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Product> result = productRepository.findById(9999999L);
        Assertions.assertFalse(result.isPresent());
    }


}
