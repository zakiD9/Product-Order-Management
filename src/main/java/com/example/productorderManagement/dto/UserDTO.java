package com.example.productorderManagement.dto;

import com.example.productorderManagement.model.Role;
import com.example.productorderManagement.model.User;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private Role role;

    public UserDTO(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
