package com.example.chat.admin.api;

import com.example.chat.admin.dto.AdminMessageDto;
import com.example.chat.admin.service.AdminMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/messages")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMessageController {

    private final AdminMessageService adminMessageService;

    // 최신순 Flux 스트림에서 페이징
    // 특정 방 메시지 최신순 조회
    @GetMapping
    public Flux<AdminMessageDto> byRoom(
            @RequestParam(name = "roomId") Long roomId,
            @RequestParam(name = "offset", defaultValue = "0") long offset,
            @RequestParam(name = "limit", defaultValue = "50") long limit
    ) {
        return adminMessageService.findByRoomDesc(roomId)
                .skip(offset)
                .take(limit)
                .map(AdminMessageDto::from);
    }

    // 메시지 단건 조회 -> 욕설 신고 처리할 때 사용
    @GetMapping("/{id}")
    public Mono<AdminMessageDto> get(@PathVariable(name = "id") String id) {
        return adminMessageService.getById(id).map(AdminMessageDto::from);
    }

    // 메시지 삭제
    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable(name = "id") String id) {
        return adminMessageService.deleteById(id);
    }

    // 방 메시지 개수
    @GetMapping("/count")
    public Mono<Long> count(@RequestParam(name = "roomId") Long roomId) {
        return adminMessageService.countByRoom(roomId);
    }
}
