package com.example.productorderManagement.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.productorderManagement.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Page<User> findByUsernameContainingIgnoreCase(String name, Pageable pageable);

}

