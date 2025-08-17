package com.dolbom.hanium_project.controller;

import com.dolbom.hanium_project.dto.request.LoginRequestDto;
import com.dolbom.hanium_project.dto.request.SignUpRequestDto;
import com.dolbom.hanium_project.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "사용자 관리", description = "사용자 관련 API")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 API
     * @param signUpRequestDto 회원가입 요청 정보
     * @return 성공 메시지
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * 로그인 API
     * @param loginRequestDto 로그인 요청 정보
     * @param response HTTP 응답 객체 (헤더에 토큰을 추가하기 위해 사용)
     * @return 성공 메시지
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.login(loginRequestDto, response);
        return ResponseEntity.ok("로그인이 성공적으로 완료되었습니다.");
    }
}