package com.wyssengineeringai.chatserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Data 
@Table(name = "messages")
public class Message {
    @Id
    private Long id;

    private String content;

    @Column("user_id")
    private Long userId;

    @Column("chat_room_id")
    private Long chatRoomId;

    @Column("created_at")
    private Instant createdAt;
}
