package com.wyssengineeringai.chatserver.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")  // Security: Passwort nicht loggen
public class RegisterDto {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, _ and -")
    private String username;

    @NotBlank(message = "Firstname is required")
    @Size(min = 1, max = 100, message = "Firstname must be 1-100 characters")
    private String firstname;

    @NotBlank(message = "Lastname is required")
    @Size(min = 1, max = 100, message = "Lastname must be 1-100 characters")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email too long")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be 8-128 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain uppercase, lowercase, digit and special character"
    )
    private String password;
}
