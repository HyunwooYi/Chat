package com.example.chat.auth;

import com.example.chat.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class PrincipalDetails implements OAuth2User, UserDetails {
        private final Member member;
        private final Map<String, Object> attributes;

    // oauth 로그인
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    // 권한 주입
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security 는 hasRole("ADMIN")을 내부적으로 "ROLE_ADMIN"를 찾는다.
        String roleName = "ROLE_" + member.getRole().name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    // implements
    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getMember().getLoginId();
    }
}
