package com.wyssengineeringai.chatserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.text.DateFormat;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    @NotBlank
    private String token;
    @NotBlank
    private String username;
    @Id
    @NotBlank
    private Integer userId;
    @NotBlank
    private Instant expiration;
}
