/**
 * Default implementation of the JwtService interface for JWT token operations.
 * 
 * This service handles the creation, parsing, and validation of JSON Web Tokens (JWT)
 * using HMAC-SHA encryption. It provides functionality to generate tokens for authenticated
 * users and extract user information from existing tokens.
 * 
 * @see JwtService
 * @see io.jsonwebtoken.Jwts
 */
package com.wyssengineeringai.chatserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class DefaultJwtService implements JwtService {
    private final SecretKey secretKey;
    private final long expirationMillis = 3600000; // 1 hour

    public DefaultJwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMillis))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public Instant getExpirationFromToken(String token) {
        return extractClaims(token).getExpiration().toInstant();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
