package com.example.chat.api.rest.chat;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.domain.chatroom.service.ChatRoomService;
import com.example.chat.dto.request.RequestChatRoomDto;
import com.example.chat.dto.response.ResponseChatRoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody RequestChatRoomDto req,
            @AuthenticationPrincipal PrincipalDetails principal) {

        ResponseChatRoomDto dto = chatRoomService.createChatRoom(req);

        Long memberId = principal.getMember().getMemberId();
        chatRoomService.joinRoom(dto.getId(), memberId); // ← 신규 메서드 추가(아래 참고)

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @GetMapping("/chatList")
    public ResponseEntity<List<ResponseChatRoomDto>> getChatRoomList() {
        List<ResponseChatRoomDto> responses = chatRoomService.findChatRoomList();
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<Void> joinRoom(@PathVariable("roomId") Long roomId,
                                         @AuthenticationPrincipal PrincipalDetails principal) {
        chatRoomService.joinRoom(roomId, principal.getMember().getMemberId());
        return ResponseEntity.ok().build();
    }
}
