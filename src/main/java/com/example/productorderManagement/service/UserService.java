package com.example.productorderManagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.response.UserResponse;
import com.example.productorderManagement.model.Address;
import com.example.productorderManagement.model.Role;
import com.example.productorderManagement.model.User;
import com.example.productorderManagement.repository.AddressRepository;
import com.example.productorderManagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }


    public UserResponse createUser(User user ,Long addressId) {
        user.setCreatedAt(LocalDate.now());

        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        if (addressId != null) {
            Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
            user.getAddresses().add(address);
        }
        User saved = userRepository.save(user);
        return new UserResponse(saved);
    }

    public UserResponse addAddressToUser(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        user.getAddresses().add(address);
        User saved = userRepository.save(user);
        return new UserResponse(saved);
    }

    public UserResponse removeAddressFromUser(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new RuntimeException("Address not found"));
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

    public UserResponse updateUser(Long id, User updatedUser) {
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
