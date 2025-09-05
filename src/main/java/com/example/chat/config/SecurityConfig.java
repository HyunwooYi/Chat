package com.example.chat.config;

import com.example.chat.auth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/health").permitAll()
                        .requestMatchers("/ws/**").permitAll() // websocket handshake 허용
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )
                // 미인증 시 401이 아니라 구글 OAuth2로 리다이렉트
                .exceptionHandling(h -> h
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/google"))
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(u -> u.userService(userService))
                        // 로그인 성공 후 이동할 위치 고정
                        .defaultSuccessUrl("/api/v1/members/me", true)
                );

        // 세션 기반 유지 (기본값). API 토큰 서버가 아니라면 STATELESS 설정 금지.
        return http.build();
    }
}