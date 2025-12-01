/**
 * Default implementation of the MessageService interface.
 * Provides functionality for creating and retrieving chat messages.
 */

/**
 * Creates a new message in a chat room.
 * 
 * @param userId ID of the user sending the message
 * @param messageDto DTO containing message content and target chat room
 * @return Mono with the created Message entity
 * @throws RuntimeException if user or chat room is not found
 */

/**
 * Retrieves messages for a specific chat room ordered by creation time (oldest first).
 * 
 * @param chatRoomId ID of the chat room
 * @param limit Maximum number of messages to retrieve (1-100)
 * @return Flux of MessageResponseDto containing message details with username
 */
package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.dto.MessageResponseDto;
import com.wyssengineeringai.chatserver.dto.SendMessageDto;
import com.wyssengineeringai.chatserver.entity.Message;
import com.wyssengineeringai.chatserver.repository.ChatRoomRepository;
import com.wyssengineeringai.chatserver.repository.MessageRepository;
import com.wyssengineeringai.chatserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class DefaultMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageService.class);
    
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public DefaultMessageService(MessageRepository messageRepository,
                                UserRepository userRepository,
                                ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public Mono<Message> createMessage(Long userId, SendMessageDto messageDto) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found: " + userId)))
                .flatMap(user -> chatRoomRepository.findById(messageDto.getChatRoomId())
                        .switchIfEmpty(Mono.error(new RuntimeException("ChatRoom not found: " + messageDto.getChatRoomId())))
                        .flatMap(chatRoom -> {
                            Message message = new Message();
                            message.setContent(messageDto.getContent());
                            message.setUserId(userId);
                            message.setChatRoomId(messageDto.getChatRoomId());
                            message.setCreatedAt(Instant.now());
                            
                            return messageRepository.save(message)
                                    .doOnSuccess(m -> logger.info("Message created by user {} in room {}", userId, messageDto.getChatRoomId()));
                        })
                );
    }

    @Override
    public Flux<MessageResponseDto> getMessagesForChatRoom(Long chatRoomId, int limit) {
        var pageable = PageRequest.of(0, Math.max(1, Math.min(limit, 100)), 
                                     Sort.by(Sort.Direction.DESC, "createdAt"));
        
        return messageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable)
                .flatMap(message -> userRepository.findById(message.getUserId())
                        .map(user -> MessageResponseDto.builder()
                                .id(message.getId())
                                .content(message.getContent())
                                .username(user.getUsername())
                                .chatRoomId(message.getChatRoomId())
                                .createdAt(message.getCreatedAt())
                                .build())
                        .switchIfEmpty(Mono.just(MessageResponseDto.builder()
                                .id(message.getId())
                                .content(message.getContent())
                                .username("Unknown")
                                .chatRoomId(message.getChatRoomId())
                                .createdAt(message.getCreatedAt())
                                .build()))
                )
                .collectList()
                .flatMapMany(list -> {
                    java.util.Collections.reverse(list);
                    return Flux.fromIterable(list);
                });
    }
}