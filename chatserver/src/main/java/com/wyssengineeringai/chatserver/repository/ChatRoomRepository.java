package com.wyssengineeringai.chatserver.repository;

import com.wyssengineeringai.chatserver.entity.ChatRoom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, Long> {
    Mono<ChatRoom> findByName(String name);
    Mono<Boolean> existsByName(String name);
}