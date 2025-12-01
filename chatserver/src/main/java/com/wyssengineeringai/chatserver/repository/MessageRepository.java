package com.wyssengineeringai.chatserver.repository;

import com.wyssengineeringai.chatserver.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, Long> {
    Flux<Message> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);
}