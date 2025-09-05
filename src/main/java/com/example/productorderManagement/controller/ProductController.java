package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.response.ProductResponse;
import com.example.productorderManagement.model.Product;
import com.example.productorderManagement.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody Product product, @RequestParam Long categoryId) {
        ProductResponse productDTO = productService.addNewProduct(product,categoryId);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductResponse dto) {
        ProductResponse productDTO = productService.updateProduct(productId, dto);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse productDTO = productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Void> adjustStock(@PathVariable Long productId, @RequestParam int delta) {
        productService.adjustStock(productId, delta);
        return ResponseEntity.noContent().build();
    }
}