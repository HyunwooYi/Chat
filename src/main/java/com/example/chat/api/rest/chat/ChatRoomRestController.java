package com.example.chat.api.rest.chat;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.domain.chatroom.service.ChatRoomService;
import com.example.chat.dto.request.RequestChatRoomDto;
import com.example.chat.dto.response.ResponseChatRoomDto;
import com.example.chat.member.entity.MemberStatus;
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

        // 1. 로그인 사용자 정보 확인
        if (principal == null || principal.getMember() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2) 밴 여부 체크
        if (principal.getMember().getMemberStatus() == MemberStatus.BANNED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ResponseChatRoomDto dto = chatRoomService.createChatRoom(req);

        Long memberId = principal.getMember().getMemberId();
        chatRoomService.joinRoom(dto.getId(), memberId);

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
