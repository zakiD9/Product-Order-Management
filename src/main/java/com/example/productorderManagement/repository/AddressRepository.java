package com.example.productorderManagement.repository;

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
    List <Address> findByUser(User user);
}
