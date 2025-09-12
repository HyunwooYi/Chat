package com.example.chat.admin.api;

import com.example.chat.admin.dto.AdminMemberDto;
import com.example.chat.admin.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/members")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    // 유저 목록 불러오기
    @GetMapping
    public Page<AdminMemberDto> list (@PageableDefault(size = 20, sort = "memberId")Pageable pageable) {
        return adminMemberService.findAll(pageable).map(AdminMemberDto::from);
    }

    // memberId로 member 찾기, 신고 하면 writerId가 반환 되는데 writerId == memberId
    @GetMapping("/{memberId}")
    public ResponseEntity<AdminMemberDto> find (@PathVariable(name = "memberId") Long memberId) {
        return  adminMemberService.findMember(memberId)
                .map(AdminMemberDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
