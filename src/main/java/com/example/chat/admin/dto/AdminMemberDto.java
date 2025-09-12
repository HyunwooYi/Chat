package com.example.chat.admin.dto;

import com.example.chat.auth.Role;
import com.example.chat.member.entity.Member;
import com.example.chat.member.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminMemberDto {
    private Long memberId;
    private String loginId;
    private String username;
    private String email;
    private Role role;
    private MemberStatus status;

    public static AdminMemberDto from (Member m) {
        return new AdminMemberDto(
                m.getMemberId(),
                m.getLoginId(),
                m.getUsername(),
                m.getEmail(),
                m.getRole(),
                m.getMemberStatus()
        );
    }

}
