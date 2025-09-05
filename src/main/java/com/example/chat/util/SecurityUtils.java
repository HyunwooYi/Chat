package com.example.chat.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.security.Principal;

public final class SecurityUtils {
    private SecurityUtils() {}
    public static Long getMemberId(Principal principal) {
        if (principal instanceof Authentication auth) {
            if (auth instanceof OAuth2AuthenticationToken token) {
                var pd = (com.example.chat.auth.PrincipalDetails) token.getPrincipal();
                return pd.getMember().getMemberId();
            }
        }
        throw new AccessDeniedException("unauthenticated");
    }
}