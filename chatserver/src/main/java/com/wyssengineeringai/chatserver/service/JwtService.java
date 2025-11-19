package com.wyssengineeringai.chatserver.service;

import java.time.Instant;

public interface JwtService {
    String generateToken(String username);
    String extractUsername(String token);
    Instant getExpirationFromToken(String token);
}
