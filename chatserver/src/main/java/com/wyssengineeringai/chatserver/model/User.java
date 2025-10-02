package com.wyssengineeringai.chatserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.aspectj.lang.annotation.RequiredTypes;

@Entity
public class User {
    @Id
    public Integer id;
    public String username;
    public String firstname;
    public String lastname;
    public String email;
    public String password;
}
