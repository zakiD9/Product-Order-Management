package com.example.productorderManagement.dto.response;

import com.example.productorderManagement.model.Product;

import lombok.Data;

@Data
public class ProductResponse {
    private Long productId;
    private String productName;
    private String description;
    private Integer quantity;
    private Double price;

    public ProductResponse(Product product){
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.description = product.getDescription();
        this.quantity = product.getQuantity(); 
        this.price = product.getPrice();
    }
    
}
