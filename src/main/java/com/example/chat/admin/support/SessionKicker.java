package com.example.chat.admin.support;

import com.example.chat.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionKicker {

    private final SessionRegistry sessionRegistry;

    /** 해당 memberId의 모든 로그인 세션을 즉시 만료(로그아웃 처리) */
    public int kickByMemberId(Long memberId) {
        int count = 0;
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof PrincipalDetails pd) {
                if (pd.getMember().getMemberId().equals(memberId)) {
                    for (SessionInformation si : sessionRegistry.getAllSessions(principal, false)) {
                        si.expireNow(); // 만료 마크
                        count++;
                    }
                }
            }
        }
        return count;
    }
}