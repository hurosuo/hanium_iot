package com.dolbom.hanium_project.service;

import com.dolbom.hanium_project.domain.PatientEntity;
import com.dolbom.hanium_project.domain.SensorEntity;
import com.dolbom.hanium_project.dto.request.SensorDataRequestDto;
import com.dolbom.hanium_project.repository.PatientEntityRepository;
import com.dolbom.hanium_project.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dolbom.hanium_project.dto.response.SensorDataResponseDto;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final PatientEntityRepository patientRepository;

    @Transactional
    public void saveSensorData(SensorDataRequestDto requestDto) {
        // 1. DTO에 담긴 patientId로 피보호자 엔티티를 조회
        PatientEntity patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("해당 피보호자를 찾을 수 없습니다. id=" + requestDto.getPatientId()));

        // 2. DTO와 조회된 Patient 엔티티를 사용하여 Sensor 엔티티 생성
        SensorEntity sensor = SensorEntity.builder()
                .temperature(requestDto.getTemperature())
                .humidity(requestDto.getHumidity())
                .fineDust(requestDto.getFineDust())
                .vocs(requestDto.getVocs())
                .movementCount(requestDto.getMovementCount())
                .lidar(requestDto.getLidar())
                .patient(patient)
                .build();

        // 3. 생성된 Sensor 엔티티를 DB에 저장
        sensorRepository.save(sensor);
    }
    public SensorDataResponseDto getLatestSensorData(Long patientId) {
        SensorEntity latestSensor = sensorRepository.findTopByPatient_IdOrderByMeasuredAtDesc(patientId)
                .orElseThrow(() -> new IllegalArgumentException("센서 데이터가 없습니다. patientId=" + patientId));

        return new SensorDataResponseDto(latestSensor);
    }
}