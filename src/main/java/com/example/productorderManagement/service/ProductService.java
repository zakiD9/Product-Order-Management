package com.example.productorderManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.request.ProductRequest;
import com.example.productorderManagement.dto.response.ProductResponse;
import com.example.productorderManagement.exception.BadRequestException;
import com.example.productorderManagement.exception.ResourceNotFoundException;
import com.example.productorderManagement.exception.ValidationException;
import com.example.productorderManagement.model.Category;
import com.example.productorderManagement.model.Product;
import com.example.productorderManagement.repository.CategoryRepository;
import com.example.productorderManagement.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse addNewProduct(ProductRequest productRequest,Long categoryId){
        boolean exists = productRepository.existsByName(productRequest.getProductName());
        if(exists){
            throw new BadRequestException("this product already exists");
        }
        Optional<Category> category =categoryRepository.findById(categoryId);
        if(!category.isPresent()){
            throw new ResourceNotFoundException("this category not exist");
        }
        Product product = new Product();
        product.setDescription(productRequest.getDescription());
        product.setName(productRequest.getProductName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(category.get());
        product.setCreatedAt(java.time.LocalDate.now());
        productRepository.save(product);
        return new ProductResponse(product);
    }

    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        product.setUpdatedAt(LocalDate.now());

        Product updated = productRepository.save(product);
        return new ProductResponse(updated);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(productId);
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .toList();
    }

    @Transactional
    public void adjustStock(Long productId, int delta) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int newQuantity = product.getQuantity() + delta;
        if (newQuantity < 0) {
            throw new ValidationException("Not enough stock");
        }

        product.setQuantity(newQuantity);
        product.setUpdatedAt(LocalDate.now());
        productRepository.save(product);
    }
}
