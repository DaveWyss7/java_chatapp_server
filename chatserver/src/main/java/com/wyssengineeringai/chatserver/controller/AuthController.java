package com.wyssengineeringai.chatserver.controller;

import com.wyssengineeringai.chatserver.dto.AuthResponseDto;
import com.wyssengineeringai.chatserver.dto.LoginDto;
import com.wyssengineeringai.chatserver.dto.RegisterDto;
import com.wyssengineeringai.chatserver.service.DefaultJwtService;
import com.wyssengineeringai.chatserver.service.DefaultUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public class AuthController {
    private final DefaultUserService defaultUserService;
    private final PasswordEncoder passwordEncoder;
    private final DefaultJwtService jwtService;

    public AuthController(DefaultUserService defaultUserService, PasswordEncoder passwordEncoder, DefaultJwtService jwtService) {
        this.defaultUserService = defaultUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponseDto>> register(@RequestBody RegisterDto registerDto) {
        return defaultUserService.getUserByUsername(registerDto.getUsername())
                .flatMap(existingUser -> Mono.error(new RuntimeException("Username already taken")))
                .switchIfEmpty(Mono.defer(() -> {
                    var hashedPassword = passwordEncoder.encode(registerDto.getPassword());

                    // User-Objekt erstellen
                    User newUser = new User();
                    newUser.setUsername(registerDto.getUsername());
                    newUser.setFirstName(registerDto.getFirstname());
                    newUser.setLastName(registerDto.getLastname());
                    newUser.setEmail(registerDto.getEmail());
                    newUser.setPasswordHash(hashedPassword);

                    return defaultUserService.createUser(newUser)
                            .flatMap(newUser -> {
                                // Here you would generate a JWT token using jwtService
                                String token = jwtService.generateToken(newUser.getUsername());
                                // Build and return the AuthResponseDto
                                return Mono.just(ResponseEntity.ok(new AuthResponseDto()));
                            });
                }));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponseDto>> login(@RequestBody LoginDto loginDto) {
        return defaultUserService.getUserByUsername(loginDto.getUsername())
                .flatMap(user -> {
                    if (passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
                        // Here you would generate a JWT token using jwtService
                        String token = jwtService.generateToken(user.getUsername());
                        // Build and return the AuthResponseDto
                        return Mono.just(ResponseEntity.ok(new AuthResponseDto()));
                    } else {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

}
