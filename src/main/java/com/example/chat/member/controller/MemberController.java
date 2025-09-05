package com.example.chat.member.controller;

import com.example.chat.auth.PrincipalDetails;
import com.example.chat.member.entity.Member;
import com.example.chat.member.repository.MemberRepository;
import com.example.chat.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
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

    // 👇 단건 조회 (이름만 필요할 때)
    @GetMapping("/{memberId}")
    public NameDto name(@PathVariable Long memberId) {
        // JPA/Repository 있는 구조면 주입해서 조회하세요.
        // 간단히 username만 필요한다면 MemberService로 바꿔도 됩니다.
        // 아래는 예시: 반드시 실제 조회 코드로 교체!
        memberService.findBy
        throw new UnsupportedOperationException("MemberRepository 주입 후 memberId로 조회해서 username 리턴해주세요");
    }

    @Getter
    @AllArgsConstructor
    static class MeDto { Long memberId; String username; String email; }
    @Getter @AllArgsConstructor
    static class NameDto { Long memberId; String username; }
}
