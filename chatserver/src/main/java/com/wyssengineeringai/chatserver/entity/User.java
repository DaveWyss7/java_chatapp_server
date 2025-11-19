package com.wyssengineeringai.chatserver.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@ToString(exclude = "passwordHash")
@Table(name = "users")
public class User {
    @Id
    private Long id;  // ← Integer → Long
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    @Column("password_hash")
    private String passwordHash;
}

