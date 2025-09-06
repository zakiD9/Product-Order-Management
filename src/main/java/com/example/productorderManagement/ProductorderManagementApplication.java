package com.example.productorderManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductorderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductorderManagementApplication.class, args);
	}

}
