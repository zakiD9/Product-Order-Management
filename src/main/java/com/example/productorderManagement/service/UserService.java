package com.example.productorderManagement.service;

import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.User;

public class UserService {



    public Address addUserAddress(Long userId, Address address) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    address.getUsers().add(user);

    user.getAddresses().add(address);

    return addressRepository.save(address);
    }
    
}
