package com.example.productorderManagement.dto.response;


import lombok.Data;

@Data
public class CategoryResponse {
    private Long categoryId;
    private String name;
    private String description;

    public CategoryResponse(com.example.productorderManagement.model.Category category){
        this.categoryId = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
         
    }
    
}