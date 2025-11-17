package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.User;
import com.wyssengineeringai.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<User> createUser(User user) {
        if(user == null) {
            return Mono.error(new IllegalArgumentException("User cannot be null"));
        }
        validateUser(user);

        return userRepository.existsByUsername(user.getUsername())
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.error(new IllegalArgumentException("Username already exists"));
                    }
                    return userRepository.existsByEmail(user.getEmail());
                })
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new IllegalArgumentException("Email already exists"));
                    }

                    // Passwort hashen
                    String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
                    user.setPasswordHash(hashedPassword);

                    return userRepository.save(user);
                })
                .onErrorMap(e -> new RuntimeException("Error creating user: " + e.getMessage(), e));
    }

    public Mono<User> getUserById(Integer id) {
        if(id == null || id <= 0) {
            return Mono.error( new IllegalArgumentException("Invalid user ID"));
        }
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .onErrorMap(e -> new RuntimeException("Error retrieving user: " + e.getMessage(), e));
    }

    public Mono<User> getUserByUsername(String username) {
        if(username == null) {
            return Mono.error(new IllegalArgumentException("Username cannot be null"));
        }
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .onErrorMap(e -> new RuntimeException("Error retrieving user: " + e.getMessage(), e));
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
