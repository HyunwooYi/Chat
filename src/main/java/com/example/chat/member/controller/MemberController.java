package com.example.chat.member.controller;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.dto.response.MeDto;
import com.example.chat.dto.response.NameDto;
import com.example.chat.member.entity.Member;
import com.example.chat.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public String greet() {
        return "Welcome to Hyunwwo, Hyunwwo.com";
    }

    // 👇 내 정보 JSON (id/username/email)
    @GetMapping("/me.json")
    public MeDto me(@AuthenticationPrincipal PrincipalDetails principal) {
        Member m = principal.getMember();
        return new MeDto(m.getMemberId(), m.getUsername(), m.getEmail());
    }

    // 숫자인 경우에만 이 핸들럴로 온다
    @GetMapping("/{memberId:\\d+}")
    public NameDto name(@PathVariable("memberId") Long memberId) {
        return memberService.findNameById(memberId);
    }

}
