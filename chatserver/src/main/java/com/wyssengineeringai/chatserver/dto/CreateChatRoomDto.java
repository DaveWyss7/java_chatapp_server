package com.wyssengineeringai.chatserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChatRoomDto {
    @NotBlank(message = "Chat room name is required")
    @Size(min = 3, max = 100, message = "Name must be 3-100 characters")
    private String name;

    @Size(max = 500, message = "Description too long")
    private String description;
}
