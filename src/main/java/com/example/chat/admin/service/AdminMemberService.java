package com.example.chat.admin.service;

import com.example.chat.member.entity.Member;
import com.example.chat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;

    public Page<Member> findAll (Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Optional<Member> findMember(Long memberId) {
        return memberRepository.findByMemberId(memberId);
    }
}
