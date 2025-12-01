package com.wyssengineeringai.chatserver.entity;

import org.springframework.data.annotation.Id;

import java.util.Collection;
import java.util.List;

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
