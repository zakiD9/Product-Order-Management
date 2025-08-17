package com.example.productorderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>{
}
