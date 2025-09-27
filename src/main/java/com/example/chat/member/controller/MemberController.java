package com.example.chat.member.controller;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.dto.response.MeDto;
import com.example.chat.dto.response.NameDto;
import com.example.chat.member.entity.Member;
import com.example.chat.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    // 내 정보 JSON (id/username/email)
    @GetMapping("/me.json")
    public ResponseEntity<MeDto> me(@AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null || principal.getMember() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Member m = principal.getMember();
        return ResponseEntity.ok(new MeDto(m.getMemberId(), m.getUsername(), m.getEmail()));
    }

    // 상대방 이름 조회용
    @GetMapping("/{memberId:\\d+}")
    public NameDto name(@PathVariable("memberId") Long memberId) {
        return memberService.findNameById(memberId);
    }

}
