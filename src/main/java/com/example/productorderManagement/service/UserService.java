package com.example.productorderManagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.request.UserRequest;
import com.example.productorderManagement.dto.response.UserResponse;
import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.Role;
import com.example.productorderManagement.model.User;
import com.example.productorderManagement.repository.AddressRepository;
import com.example.productorderManagement.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }


    public UserResponse createUser(UserRequest userRequest ,Long addressId) {
        if (userRequest.getUsername() == null || userRequest.getUsername().isBlank()) {
        throw new IllegalArgumentException("Username is required");
    }
    if (userRequest.getEmail() == null || userRequest.getEmail().isBlank()) {
        throw new IllegalArgumentException("Email is required");
    }
    if (userRepository.existsByEmail(userRequest.getEmail())) {
        throw new IllegalStateException("Email already exists");
    }
    if (userRequest.getPassword() == null || userRequest.getPassword().length() < 6) {
        throw new IllegalArgumentException("Password must be at least 6 characters");
    }
    User user = new User();
        if (addressId != null) {
            Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
            user.getAddresses().add(address);
        }
        
        user.setEmail(userRequest.getEmail());
        user.setRole(Role.CUSTOMER);
        user.setPassword(userRequest.getPassword());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setUsername(userRequest.getUsername());
        user.setCreatedAt(LocalDate.now());

        userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse addAddressToUser(Long userId, Long addressId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    Address address = addressRepository.findById(addressId)
        .orElseThrow(() -> new IllegalArgumentException("Address not found with id: " + addressId));
    if (user.getAddresses().contains(address)) {
        throw new IllegalStateException("User already has this address");
    }
    user.getAddresses().add(address);
    User saved = userRepository.save(user);
    return new UserResponse(saved);
}

public UserResponse removeAddressFromUser(Long userId, Long addressId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    Address address = addressRepository.findById(addressId)
        .orElseThrow(() -> new IllegalArgumentException("Address not found with id: " + addressId));
    if (!user.getAddresses().contains(address)) {
        throw new IllegalStateException("User does not have this address");
    }
    user.getAddresses().remove(address);
    User saved = userRepository.save(user);
    return new UserResponse(saved);
}

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse updateUser(Long id, UserRequest updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getUsername() != null) existing.setUsername(updatedUser.getUsername());
        if (updatedUser.getEmail() != null) existing.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhoneNumber() != null) existing.setPhoneNumber(updatedUser.getPhoneNumber());

        User saved = userRepository.save(existing);
        return new UserResponse(saved);
    }


    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
