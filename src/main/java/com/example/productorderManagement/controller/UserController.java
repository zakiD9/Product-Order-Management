package com.example.productorderManagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productorderManagement.dto.request.UserRequest;
import com.example.productorderManagement.dto.response.UserResponse;
import com.example.productorderManagement.model.User;
import com.example.productorderManagement.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest user,
            @RequestParam(required = false) Long addressId) {
        UserResponse createdUser = userService.createUser(user, addressId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/{userId}/address/{addressId}")
    public ResponseEntity<UserResponse> addAddressToUser(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        UserResponse updatedUser = userService.addAddressToUser(userId, addressId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/address/{addressId}")
    public ResponseEntity<UserResponse> removeAddressFromUser(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        UserResponse updatedUser = userService.removeAddressFromUser(userId, addressId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest updatedUser) {
        UserResponse user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    
}
