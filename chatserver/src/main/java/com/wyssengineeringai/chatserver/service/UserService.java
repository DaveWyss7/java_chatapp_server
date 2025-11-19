package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService {
    Mono<User> createUser(User user);
    Mono<User> updateUser(Long id, User user);
    Mono<User> deleteUser(Long id);
    Mono<User> getUserById(Long id);
    Mono<User> getUserByUsername(String username);
    Mono<User> userNameExists(String username);
    Mono<User> emailExists(String email);
    Flux<List<User>> getAllUsers();
}
