package com.wyssengineeringai.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private Long id;
    private String content;
    private String username;
    private Long chatRoomId;
    private Instant createdAt;
}
