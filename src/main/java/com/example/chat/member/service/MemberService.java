package com.example.chat.member.service;

import com.example.chat.dto.response.NameDto;
import com.example.chat.global.RestApiException;
import com.example.chat.global.errorcode.MemberErrorCode;
import com.example.chat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public NameDto findNameById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(m -> new NameDto(m.getMemberId(), m.getUsername()))
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
