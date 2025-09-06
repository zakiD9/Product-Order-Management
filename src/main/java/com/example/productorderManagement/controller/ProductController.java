package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.request.ProductRequest;
import com.example.productorderManagement.dto.response.ProductResponse;
import com.example.productorderManagement.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product, @RequestParam Long categoryId) {
        ProductResponse productDTO = productService.addNewProduct(product,categoryId);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,@Valid @RequestBody ProductRequest dto) {
        ProductResponse productDTO = productService.updateProduct(productId, dto);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN') or @productService.isProductAvailable(#productId)")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse productDTO = productService.getProductById(productId);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public Page<ProductResponse> getAllProducts(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return productService.getAllProducts(name, page, size);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Void> adjustStock(@PathVariable Long productId, @RequestParam int delta) {
        productService.adjustStock(productId, delta);
        return ResponseEntity.noContent().build();
    }
}