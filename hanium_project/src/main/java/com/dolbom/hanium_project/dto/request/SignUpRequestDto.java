package com.dolbom.hanium_project.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {

    // 보호자(User) 정보
    private String email;
    private String password;
    private String phoneNumber;

    // 피보호자(Patient) 정보
    private String patientName;
    private int age;
    private double height;
    private double weight;
    private String medicalHistory;
    private String streetAddress;
    private String detailAddress;
}