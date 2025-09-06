package com.example.productorderManagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.User;

import java.util.List;


@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{
    boolean existsByStreetAndCityAndStateAndZipCode(
        String street,
        String city,
        String state,
        String zipCode
    );
    Page<Address> findByCityContainingIgnoreCase(String city, Pageable pageable);
    Page<Address> findByStateContainingIgnoreCase(String state, Pageable pageable);
    Page<Address> findByZipCode(String zipCode, Pageable pageable);
    Page<Address> findByStreetContainingIgnoreCase(String street, Pageable pageable);
    List <Address> findByUsers(User user);
}
