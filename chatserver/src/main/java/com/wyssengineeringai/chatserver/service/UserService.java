package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> createUser(User user);
    Mono<User> updateUser(Integer id, User user);
    Mono<User> deleteUser(Integer id);
    Mono<User> getUserById(Integer id);
    Mono<User> getUserByUsername(String username);
    Mono<User> userNameExists(String username);
    Mono<User> emailExists(String email);
    Mono<User> getAllUsers();
}
