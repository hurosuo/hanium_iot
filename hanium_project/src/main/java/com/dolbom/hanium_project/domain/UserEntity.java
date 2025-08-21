package com.dolbom.hanium_project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // DB 테이블 이름을 'users'로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자를 필요로 합니다.
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT 사용
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true) // null 불가, 중복 불가
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number") // DB에는 phone_number 컬럼으로 생성됩니다.
    private String phoneNumber;

    // 한 명의 User가 여러 명의 Patient를 가질 수 있음 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientEntity> patients = new ArrayList<>();

    // 생성자
    public UserEntity(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}