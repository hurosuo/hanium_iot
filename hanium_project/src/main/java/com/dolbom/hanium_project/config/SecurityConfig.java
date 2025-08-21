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
// --- CORS 관련 import 추가 ---
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ CORS 설정을 위한 Bean을 추가합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:5173"); // 프론트엔드 서버 주소
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명(쿠키 등) 허용
        configuration.addExposedHeader("Authorization"); // Authorization 헤더 노출 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 위 설정 적용
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ✅ CORS 설정을 필터 체인에 적용합니다.
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

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
                .requestMatchers(allowedUrls).permitAll()
                .anyRequest().authenticated()
        );

        // 우리가 만든 JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가합니다.
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}