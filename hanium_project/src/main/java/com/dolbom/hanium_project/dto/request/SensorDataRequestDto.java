package com.dolbom.hanium_project.dto.request;


import lombok.Getter;

@Getter
public class SensorDataRequestDto {
    private Long patientId; // 어느 피보호자의 데이터인지 식별
    private double temperature;
    private double humidity;
    private double fineDust;
    private double vocs;
    private int movementCount;
    private int lidar;
}