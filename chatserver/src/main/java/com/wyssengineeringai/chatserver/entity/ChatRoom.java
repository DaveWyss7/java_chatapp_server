package com.wyssengineeringai.chatserver.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;

@Data
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    private Long id;

    private String name;

    private String description;

    @Column("created_at")
    private Instant createdAt;
}
