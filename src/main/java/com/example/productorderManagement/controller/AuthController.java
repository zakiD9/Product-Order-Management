package com.example.productorderManagement.controller;

import com.example.productorderManagement.dto.request.LoginRequest;
import com.example.productorderManagement.dto.request.UserRequest;
import com.example.productorderManagement.dto.response.AuthResponse;
import com.example.productorderManagement.dto.response.UserResponse;
import com.example.productorderManagement.exception.CustomBadCredentialsException;
import com.example.productorderManagement.model.User;
import com.example.productorderManagement.repository.UserRepository;
import com.example.productorderManagement.service.UserService;
import com.example.productorderManagement.Security.JwtUtil;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,UserService userService,UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }

   @PostMapping("/login")
public AuthResponse login(@RequestBody LoginRequest request) {
    Authentication authentication;
    try {
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    } catch (Exception ex) {
        throw new CustomBadCredentialsException("Invalid email or password");
    }

    if (!authentication.isAuthenticated()) {
        throw new CustomBadCredentialsException("Invalid login credentials");
    }

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new CustomBadCredentialsException("User not found"));

    String token = jwtUtil.generateToken(user);

    return new AuthResponse(token);
    }




    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
        @RequestBody UserRequest user,
        @RequestParam(required = false) Long addressId) {
    UserResponse createdUser = userService.createUser(user, addressId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

}
