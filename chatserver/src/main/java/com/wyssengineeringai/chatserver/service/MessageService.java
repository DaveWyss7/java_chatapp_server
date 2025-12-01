package com.wyssengineeringai.chatserver.service;

import com.wyssengineeringai.chatserver.dto.SendMessageDto;

public interface MessageService {
    CreateMessage(Longe userId, SendMessageDto message);
    Flux<MessageResponseDto> getMessagesForChatRoom(Long chatRoomId, int limit);
}
