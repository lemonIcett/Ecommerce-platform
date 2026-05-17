package com.ecommerce.product_service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_ShouldReturnSavedProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("Gaming Laptop");
        request.setPrice(999.99);
        request.setStock(10);

        Product saved = new Product(1L, "Laptop", "Gaming Laptop", 999.99, 10);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        assertEquals(999.99, result.getPrice());
        assertEquals(10, result.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Laptop", "Gaming Laptop", 999.99, 10),
                new Product(2L, "Phone", "Smartphone", 499.99, 20)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 999.99, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.getProductById(99L));

        assertEquals("Product not found with id: 99", exception.getMessage());
    }

    @Test
    void reduceStock_ShouldReturnTrue_WhenStockSufficient() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 999.99, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        boolean result = productService.reduceStock(1L, 3);

        assertTrue(result);
        assertEquals(7, product.getStock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void reduceStock_ShouldReturnFalse_WhenStockInsufficient() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 999.99, 2);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        boolean result = productService.reduceStock(1L, 5);

        assertFalse(result);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldCallRepository_WhenProductExists() {
        Product product = new Product(1L, "Laptop", "Gaming Laptop", 999.99, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}