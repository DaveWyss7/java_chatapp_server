package com.wyssengineeringai.chatserver.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data 
@Table(name = "messages")
public class Message {
    @Id
    private Long id;

    private String content;

    @Column("user_id")
    private Integer userId;

    @Column("chat_room_id")
    private Integer chatRoomId;

    @Column("created_at")
    private Instant createdAt;
}
