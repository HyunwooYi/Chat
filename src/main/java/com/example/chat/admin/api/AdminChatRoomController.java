package com.example.chat.admin.api;

import com.example.chat.admin.dto.AdminChatRoomDto;
import com.example.chat.admin.service.AdminChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/chatrooms")
@PreAuthorize("hasRole('ADMIN')")
public class AdminChatRoomController {

    private final AdminChatRoomService adminChatRoomService;

    // 채팅방 목록 조회
    @GetMapping
    public List<AdminChatRoomDto> list() {
        return adminChatRoomService.findAll().stream()
                .map(AdminChatRoomDto::from)
                .toList();
    }

    // 채팅방 하나 조회
    @GetMapping("/{roomId}")
    public AdminChatRoomDto get(@PathVariable("roomId") Long roomId) {
        return AdminChatRoomDto.from(adminChatRoomService.get(roomId));
    }

    // 방 삭제
    @DeleteMapping("/{roomId}")
    public void delete(@PathVariable("roomId") Long roomId) {
        adminChatRoomService.delete(roomId);
    }
 }
