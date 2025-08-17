package com.example.productorderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    Boolean existsByAddress(Address address);
}