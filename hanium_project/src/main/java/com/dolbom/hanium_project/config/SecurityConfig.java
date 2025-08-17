package com.dolbom.hanium_project.config;

import com.dolbom.hanium_project.security.JwtAuthenticationFilter;
import com.dolbom.hanium_project.security.UserDetailsServiceImpl;
import com.dolbom.hanium_project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // ⭐️ 1. 생성자 주입을 위한 어노테이션 추가
public class SecurityConfig {

    // ⭐️ 2. JWT 필터에 필요한 의존성들을 주입받습니다.
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 세션 관리 정책을 STATELESS로 설정 (JWT 사용 시 필수)
        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 허용할 URL 목록
        String[] allowedUrls = {
                "/api/auth/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html"
        };

        // URL별 접근 권한 설정
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(allowedUrls).permitAll() // 허용 목록에 있는 URL은 인증 없이 접근 가능
                .anyRequest().authenticated()             // 그 외 모든 요청은 인증 필요
        );

        // ⭐️ 3. 우리가 만든 JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가합니다.
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}