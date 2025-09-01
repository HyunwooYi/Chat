package com.example.Chat.config;

import com.example.Chat.auth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                // 브라우저 기반 서비스면 CSRF 켜도 되지만, REST 혼용 시 편의상 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스나 건강 체크는 필요 시 열어두세요
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/health").permitAll()
                        // 그 외 전부 로그인 필요
                        .anyRequest().authenticated()
                )
                // 미인증 시 401이 아니라 구글 OAuth2로 리다이렉트
                .exceptionHandling(h -> h
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/google"))
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(u -> u.userService(userService))
                        // 로그인 성공 후 이동할 위치 고정
                        .defaultSuccessUrl("/api/v1/chat", true)
                );

        // 세션 기반 유지 (기본값). API 토큰 서버가 아니라면 STATELESS 설정 금지.
        return http.build();
    }
}