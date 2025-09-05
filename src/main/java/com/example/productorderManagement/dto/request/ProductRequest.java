package com.example.productorderManagement.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    
    private String productName;
    private String description;
    private Integer quantity;
    private Double price;
}
