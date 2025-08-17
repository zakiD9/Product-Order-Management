package com.example.productorderManagement.dto;


import lombok.Data;

@Data
public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String description;

    public CategoryDTO(com.example.productorderManagement.model.Category category){
        this.categoryId = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
         
    }
    
}