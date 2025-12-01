package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.ChatRoom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRoomService {
    Mono<ChatRoom> createChatRoom(String name, String description);
    Mono<ChatRoom> getChatRoomById(Long id);
    Mono<ChatRoom> getChatRoomByName(String name);
    Flux<ChatRoom> getAllChatRooms();
    Mono<Boolean> chatRoomExists(String name);
}
