package com.example.productorderManagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.Order;
import com.example.productorderManagement.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    Boolean existsByAddress(Address address);
    Page<Order> findByUser_UserId(Long userId, Pageable pageable);
    Boolean existsByUser_UserIdAndStatus(Long userId ,OrderStatus status);
}