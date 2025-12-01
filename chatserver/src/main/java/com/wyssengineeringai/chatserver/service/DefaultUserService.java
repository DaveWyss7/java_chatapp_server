/**
 * Default implementation of the UserService interface for managing user operations.
 * <p>
 * This service provides CRUD operations for user management including:
 * <ul>
 *   <li>Creating new users with password hashing and duplicate validation</li>
 *   <li>Updating existing user information</li>
 *   <li>Deleting users by ID</li>
 *   <li>Retrieving users by ID or username</li>
 *   <li>Checking for existing usernames and emails</li>
 *   <li>Retrieving all users</li>
 * </ul>
 * </p>
 * <p>
 * All operations are implemented using reactive programming with Project Reactor's
 * Mono and Flux types for non-blocking I/O operations.
 * </p>
 * <p>
 * Password security is handled through Spring Security's PasswordEncoder,
 * ensuring all passwords are properly hashed before storage.
 * </p>
 * 
 * @author WyssEngineeringAI
 * @version 1.0
 * @see UserService
 * @see UserRepository
 * @see PasswordEncoder
 */
package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.User;
import com.wyssengineeringai.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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

                    // Password hashes
                    String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
                    user.setPasswordHash(hashedPassword);

                    return userRepository.save(user);
                })
                .onErrorMap(e -> new RuntimeException("Error creating user: " + e.getMessage(), e));
    }

    public Mono<User> updateUser(Long id, User user) {
        if(id == null || id <= 0) {
            return Mono.error(new IllegalArgumentException("Invalid user ID"));
        }
        if(user == null) {
            return Mono.error(new IllegalArgumentException("User cannot be null"));
        }
        validateUser(user);

        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setFirstname(user.getFirstname());
                    existingUser.setLastname(user.getLastname());
                    existingUser.setEmail(user.getEmail());
                    if(!user.getPasswordHash().isEmpty()) {
                        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
                        existingUser.setPasswordHash(hashedPassword);
                    }
                    return userRepository.save(existingUser);
                })
                .onErrorMap(e -> new RuntimeException("Error updating user: " + e.getMessage(), e));
    }

    public Mono<User> deleteUser(Long id) {
        if(id == null || id <= 0) {
            return Mono.error(new IllegalArgumentException("Invalid user ID"));
        }
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> userRepository.delete(user).thenReturn(user))
                .onErrorMap(e -> new RuntimeException("Error deleting user: " + e.getMessage(), e));
    }

    public Mono<User> getUserById(Long id) {
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

    public Mono<Boolean> usernameExists(String username) {
        if(username == null) {
            return Mono.error(new IllegalArgumentException("Username cannot be null"));
        }
        return userRepository.existsByUsername(username)
                .onErrorMap(e -> new RuntimeException("Error checking username: " + e.getMessage(), e));
    }

    public Mono<Boolean> emailExists(String email) {
        if(email == null) {
            return Mono.error(new IllegalArgumentException("Email cannot be null"));
        }
        return userRepository.existsByEmail(email)
                .onErrorMap(e -> new RuntimeException("Error checking email: " + e.getMessage(), e));
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .onErrorMap(e -> new RuntimeException("Error retrieving users: " + e.getMessage(), e));
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
