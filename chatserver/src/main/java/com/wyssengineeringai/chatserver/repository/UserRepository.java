package com.wyssengineeringai.chatserver.repository;

import com.wyssengineeringai.chatserver.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

    @Query("SELECT * FROM users WHERE username ILIKE CONCAT('%', :search, '%') OR email ILIKE CONCAT('%', :search, '%')")
    Flux<User> searchByUsernameOrEmail(String search);

    Flux<User> findAllByOrderByUsernameAsc();

}
