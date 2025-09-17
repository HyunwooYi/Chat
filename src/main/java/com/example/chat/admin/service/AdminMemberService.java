package com.example.chat.admin.service;

import com.example.chat.admin.support.SessionKicker;
import com.example.chat.auth.Role;
import com.example.chat.global.RestApiException;
import com.example.chat.global.errorcode.MemberErrorCode;
import com.example.chat.member.entity.Member;
import com.example.chat.member.entity.MemberStatus;
import com.example.chat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;
    private final SessionKicker sessionKicker;

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Optional<Member> findMember(Long memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Transactional
    public void updateStatus(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // AMIN 밴 방지
        if (member.getRole() == Role.ADMIN) {
            throw new RestApiException(MemberErrorCode.CANNOT_BAN_ADMIN);
        }

        member.setMemberStatus(
                member.getMemberStatus() == MemberStatus.UNBANNED
                ? MemberStatus.BANNED : MemberStatus.UNBANNED
        );
        // BANNED가 되는 순간 모든 세션 만료 → 다음 요청부터 자동 로그아웃
        if (member.getMemberStatus() == MemberStatus.BANNED) {
            int kicked = sessionKicker.kickByMemberId(memberId);
            System.out.println("kicked sessions = " + kicked);
        }
    }
}
