package com.wyssengineeringai.chatserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.text.DateFormat;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private String token;

    private String username;

    private Integer userId;

    private Instant expiration;
}
