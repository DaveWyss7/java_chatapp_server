package com.wyssengineeringai.chatserver.controller;

import com.wyssengineeringai.chatserver.service.DefaultUserService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthController {
    private final DefaultUserService defaultUserService;
    private final PasswordEncoder passwordEncoder;
    private final String jwtSecret;

    public AuthController(DefaultUserService defaultUserService, PasswordEncoder passwordEncoder, String jwtSecret) {
        this.defaultUserService = defaultUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
    }
}
