package com.example.productorderManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {
    
    @NotBlank(message = "name is Required")
    @Size(min = 2 ,max = 50 , message = "name size must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Description is Required")
    @Size(min = 30 ,max = 200 , message = "description size must be between 30 and 200 characters")
    private String description;
}
