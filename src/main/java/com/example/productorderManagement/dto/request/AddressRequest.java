package com.example.productorderManagement.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {
    
    @NotBlank( message = "Street is Required")
    @Size(min = 2 , max = 100 , message = "Street must be between 2 and 100 characters")
    private String street;

    
    @NotBlank( message = "City is Required")
    @Size(min = 2 , max = 50 , message = "City must be between 2 and 100 characters")
    private String city;


    @NotBlank( message = "State is Required")
    @Size(min = 2 , max = 50 , message = "State must be between 2 and 100 characters")
    private String state;


    @NotBlank( message = "Zip code is Required")
    @Pattern(regexp = "\\d{4,10}" , message = "Zip code must be 4–10 digits")
    private String zipCode;

}
