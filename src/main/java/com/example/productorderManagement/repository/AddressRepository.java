package com.example.productorderManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{
}
