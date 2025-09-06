package com.example.productorderManagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(
        regexp = "^(\\+\\d{1,3})?\\d{10,15}$",
        message = "Phone number must be valid (10-15 digits, optional country code)"
    )
    private String phoneNumber;

}
