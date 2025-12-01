package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.entity.ChatRoom;
import com.wyssengineeringai.chatserver.repository.ChatRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class DefaultChatRoomService implements ChatRoomService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultChatRoomService.class);
    
    private final ChatRoomRepository chatRoomRepository;

    public DefaultChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public Mono<ChatRoom> createChatRoom(String name, String description) {
        return chatRoomExists(name)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("ChatRoom with name '" + name + "' already exists"));
                    }
                    
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setName(name);
                    chatRoom.setDescription(description);
                    chatRoom.setCreatedAt(Instant.now());
                    
                    return chatRoomRepository.save(chatRoom)
                            .doOnSuccess(room -> logger.info("Created chat room: {}", room.getName()));
                });
    }

    @Override
    public Mono<ChatRoom> getChatRoomById(Long id) {
        return chatRoomRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("ChatRoom not found with id: " + id)));
    }

    @Override
    public Mono<ChatRoom> getChatRoomByName(String name) {
        return chatRoomRepository.findByName(name)
                .switchIfEmpty(Mono.error(new RuntimeException("ChatRoom not found with name: " + name)));
    }

    @Override
    public Flux<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll()
                .doOnSubscribe(sub -> logger.debug("Fetching all chat rooms"));
    }

    @Override
    public Mono<Boolean> chatRoomExists(String name) {
        return chatRoomRepository.existsByName(name);
    }
}
