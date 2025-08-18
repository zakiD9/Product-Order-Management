package com.example.productorderManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderItem;
import com.example.productorderManagement.model.Product;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
} 