package com.wyssengineeringai.chatserver.controller;

import com.wyssengineeringai.chatserver.dto.MessageResponseDto;
import com.wyssengineeringai.chatserver.dto.SendMessageDto;
import com.wyssengineeringai.chatserver.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/rooms/{roomId}/messages")
    public Mono<ResponseEntity<Void>> sendMessage(
            @PathVariable Long roomId,
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody SendMessageDto messageDto) {
        
        return messageService.createMessage(userId, messageDto)
                .map(message -> ResponseEntity.ok().<Void>build());
    }

    @GetMapping("/rooms/{roomId}/messages")
    public Flux<MessageResponseDto> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "50") int limit) {
        
        return messageService.getMessagesForChatRoom(roomId, limit);
    }
}