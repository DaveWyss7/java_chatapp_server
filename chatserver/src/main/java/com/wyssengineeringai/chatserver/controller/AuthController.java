package com.wyssengineeringai.chatserver.controller;

import com.wyssengineeringai.chatserver.dto.AuthResponseDto;
import com.wyssengineeringai.chatserver.dto.LoginDto;
import com.wyssengineeringai.chatserver.dto.RegisterDto;
import com.wyssengineeringai.chatserver.entity.User;
import com.wyssengineeringai.chatserver.exception.InvalidCredentialsException;
import com.wyssengineeringai.chatserver.exception.UserNotFoundException;
import com.wyssengineeringai.chatserver.exception.UsernameAlreadyExistsException;
import com.wyssengineeringai.chatserver.service.DefaultJwtService;
import com.wyssengineeringai.chatserver.service.DefaultUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")  // Add base path
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
    public Mono<ResponseEntity<AuthResponseDto>> register(@Valid @RequestBody RegisterDto registerDto) {
        return defaultUserService.getUserByUsername(registerDto.getUsername())
                .flatMap(existingUser -> Mono.<ResponseEntity<AuthResponseDto>>error(
                        new UsernameAlreadyExistsException(registerDto.getUsername())))
                .switchIfEmpty(Mono.defer(() -> {
                    var hashedPassword = passwordEncoder.encode(registerDto.getPassword());

                    User newUser = new User();
                    newUser.setUsername(registerDto.getUsername());
                    newUser.setFirstname(registerDto.getFirstname());
                    newUser.setLastname(registerDto.getLastname());
                    newUser.setEmail(registerDto.getEmail());
                    newUser.setPasswordHash(hashedPassword);

                    return defaultUserService.createUser(newUser)
                            .map(createdUser -> {
                                String token = jwtService.generateToken(createdUser.getUsername());

                                AuthResponseDto response = AuthResponseDto.builder()
                                        .token(token)
                                        .username(createdUser.getUsername())
                                        .userId(createdUser.getId())
                                        .expiration(Instant.now().plusSeconds(3600))
                                        .build();

                                return ResponseEntity.ok(response);
                            });
                }));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponseDto>> login(@RequestBody LoginDto loginDto) {
        return defaultUserService.getUserByUsername(loginDto.getUsername())
                .flatMap(user -> {
                    if (passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
                        String token = jwtService.generateToken(user.getUsername());

                        AuthResponseDto response = AuthResponseDto.builder()
                                .token(token)
                                .username(user.getUsername())
                                .userId(user.getId())
                                .expiration(Instant.now().plusSeconds(3600))
                                .build();

                        return Mono.just(ResponseEntity.ok(response));
                    } else {
                        return Mono.error(new InvalidCredentialsException());
                    }
                })
                .switchIfEmpty(Mono.error(new UserNotFoundException(loginDto.getUsername())));
    }

}
