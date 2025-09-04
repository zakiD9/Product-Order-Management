package com.example.productorderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Category;
import com.example.productorderManagement.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
    Boolean existsByCategory(Category category);
    Boolean existsByName(String name);
}

