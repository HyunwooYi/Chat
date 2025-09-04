package com.example.chat.api.rest.chat;

import com.example.chat.domain.chatroom.service.ChatRoomService;
import com.example.chat.dto.request.RequestChatRoomDto;
import com.example.chat.dto.response.ResponseChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 채팅방 REST
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomRestController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    public ResponseEntity<ResponseChatRoomDto> createChatRoom(
            @RequestBody RequestChatRoomDto requestChatRoomDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatRoomService.createChatRoom(requestChatRoomDto));
    }

    @GetMapping("/chatList")
    public ResponseEntity<List<ResponseChatRoomDto>> getChatRoomList() {
        List<ResponseChatRoomDto> responses = chatRoomService.findChatRoomList();
        return ResponseEntity.ok().body(responses);
    }
}
