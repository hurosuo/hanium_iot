package com.dolbom.hanium_project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SensorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private Long id;

    private double temperature;
    private double humidity;
    private double fineDust;
    private double vocs;
    private int movementCount;
    private int lidar;
    private LocalDateTime measuredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @Builder
    public SensorEntity(double temperature, double humidity, double fineDust, double vocs, int movementCount, int lidar, PatientEntity patient) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.fineDust = fineDust;
        this.vocs = vocs;
        this.movementCount = movementCount;
        this.lidar = lidar;
        this.patient = patient;
        this.measuredAt = LocalDateTime.now(); // 데이터 생성 시점의 시간 자동 저장
    }
}
