package com.wyssengineeringai.chatserver.dto;

import java.text.DateFormat;
import java.time.Instant;

public class AuthResponseDto {
    private String token;
    private String username;
    private Integer userId;
    private Instant expiration;

    public AuthResponseDto(String token, String username, Integer userId, Instant expiration) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }
}
