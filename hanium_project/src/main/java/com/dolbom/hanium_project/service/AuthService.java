package com.dolbom.hanium_project.service;

import com.dolbom.hanium_project.domain.PatientEntity;
import com.dolbom.hanium_project.domain.UserEntity;
import com.dolbom.hanium_project.dto.request.SignUpRequestDto;
import com.dolbom.hanium_project.repository.PatientEntityRepository;
import com.dolbom.hanium_project.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dolbom.hanium_project.dto.request.LoginRequestDto; // LoginRequestDto 임포트
import com.dolbom.hanium_project.util.JwtUtil;                   // JwtUtil 임포트
import jakarta.servlet.http.HttpServletResponse;             // HttpServletResponse 임포트







@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserEntityRepository userEntityRepository;
    private final PatientEntityRepository patientEntityRepository; // Patient 저장을 위해 추가
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
        // 1. 이메일 중복 확인
        if (userEntityRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. User 엔티티 생성
        UserEntity userEntity = new UserEntity(requestDto.getEmail(), encodedPassword, requestDto.getPhoneNumber());
        // 4. Patient 엔티티 생성
        PatientEntity patientEntity = PatientEntity.builder()
                .name(requestDto.getPatientName())
                .age(requestDto.getAge())
                .height(requestDto.getHeight())
                .weight(requestDto.getWeight())
                .medicalHistory(requestDto.getMedicalHistory())
                .streetAddress(requestDto.getStreetAddress())
                .detailAddress(requestDto.getDetailAddress())
                .build();

        // 5. 연관 관계 설정
        patientEntity.setUserEntity(userEntity); // Patient에 User 설정

        // 6. DB에 저장
        userEntityRepository.save(userEntity);
        patientEntityRepository.save(patientEntity);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 1. 사용자 확인
        UserEntity user = userEntityRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 생성 및 응답 헤더에 추가
        String token = jwtUtil.createToken(user.getEmail());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }
}