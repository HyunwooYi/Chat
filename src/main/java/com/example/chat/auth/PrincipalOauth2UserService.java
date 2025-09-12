package com.example.chat.auth;

import com.example.chat.auth.userInfo.GoogleUserInfo;
import com.example.chat.auth.userInfo.OAuth2UserInfo;
import com.example.chat.member.entity.MemberStatus;
import com.example.chat.member.entity.Member;
import com.example.chat.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    // 후처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 강제회원 가입
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else {
            System.out.println("지원하지않음.");
        }
        String provider = Objects.requireNonNull(oAuth2UserInfo).getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getProviderEmail();
        String loginId = provider + "_" + providerId;
        String username = oAuth2UserInfo.getProviderName();
        Role role = Role.USER;
        MemberStatus memberStatus = MemberStatus.UNBANNED;

        Member member = memberRepository.findByLoginId(loginId).orElse(null);
        if (member == null) {
            member = Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .username(username)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .memberStatus(memberStatus)
                    .build();
            memberRepository.save(member);
        } else {
            System.out.println("이미 로그인을 한적이 있습니다.");
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}