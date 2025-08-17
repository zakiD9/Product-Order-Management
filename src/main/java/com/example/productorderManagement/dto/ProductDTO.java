package com.example.productorderManagement.dto;

import com.example.productorderManagement.model.Product;

import lombok.Data;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private String description;
    private Integer quantity;

    public ProductDTO(Product product){
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.description = product.getDescription();
        this.quantity = product.getQuantity(); 
    }
    
}
