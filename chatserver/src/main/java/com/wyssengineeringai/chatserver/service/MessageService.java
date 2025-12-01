package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.dto.MessageResponseDto;
import com.wyssengineeringai.chatserver.dto.SendMessageDto;
import com.wyssengineeringai.chatserver.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService {
    Mono<Message> createMessage(Long userId, SendMessageDto message);
    Flux<MessageResponseDto> getMessagesForChatRoom(Long chatRoomId, int limit);
}
