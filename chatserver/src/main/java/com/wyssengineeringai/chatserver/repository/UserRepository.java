package com.wyssengineeringai.chatserver.repository;

import com.wyssengineeringai.chatserver.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {  // ← Integer → Long

    // Basic Queries
    Mono<User> findByUsername(String username);
    Mono<User> findByEmail(String email);

    // Existence Checks
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);

    // Search mit Limit (verhindert DoS)
    @Query("SELECT * FROM users WHERE LOWER(username) LIKE LOWER('%' || :search || '%') OR LOWER(email) LIKE LOWER('%' || :search || '%') LIMIT :limit")
    Flux<User> searchByUsernameOrEmail(String search, int limit);

    // Sortierte Liste (nutze Pageable für Limit/Offset)
    Flux<User> findAllByOrderByUsernameAsc(Pageable pageable);

}
