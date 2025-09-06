package com.example.productorderManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    Boolean existsByAddress(Address address);
    List<Order> findAllByUser_UserId(Long userId);
    Boolean existsByUserIdAndStatus(Long userId ,OrderStatus status);
}