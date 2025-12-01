package com.wyssengineeringai.chatserver.controller;

import com.wyssengineeringai.chatserver.dto.CreateChatRoomDto;
import com.wyssengineeringai.chatserver.entity.ChatRoom;
import com.wyssengineeringai.chatserver.service.ChatRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public Mono<ResponseEntity<ChatRoom>> createChatRoom(@Valid @RequestBody CreateChatRoomDto createChatRoomDto) {
        return chatRoomService.createChatRoom(createChatRoomDto.getName(), createChatRoomDto.getDescription())
                .map(chatRoom -> ResponseEntity.status(HttpStatus.CREATED).body(chatRoom));
    }

    @GetMapping
    public Flux<ChatRoom> getAllChatRooms() {
        return chatRoomService.getAllChatRooms();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ChatRoom>> getChatRoomById(@PathVariable Long id) {
        return chatRoomService.getChatRoomById(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/name/{name}")
    public Mono<ResponseEntity<ChatRoom>> getChatRoomByName(@PathVariable String name) {
        return chatRoomService.getChatRoomByName(name)
                .map(ResponseEntity::ok);
    }
}
