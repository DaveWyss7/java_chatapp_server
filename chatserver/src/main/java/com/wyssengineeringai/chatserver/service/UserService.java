package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.User;
import com.wyssengineeringai.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        validateUser(user);
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        try {
            String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Integer id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user: " + e.getMessage(), e);
        }
    }
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        if(username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user: " + e.getMessage(), e);
        }
    }

    private void validateUser(User user) {
        if(user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if(user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if(user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if(user.getPasswordHash().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
    }
}
