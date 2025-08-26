package com.dolbom.hanium_project.service;

import com.dolbom.hanium_project.domain.ActivityScore;
import com.dolbom.hanium_project.domain.PatientEntity;
import com.dolbom.hanium_project.domain.SensorEntity;
import com.dolbom.hanium_project.domain.UserEntity;
import com.dolbom.hanium_project.dto.request.ActivityScoreRequestDto;
import com.dolbom.hanium_project.dto.request.SensorDataRequestDto;
import com.dolbom.hanium_project.dto.response.ActivityScoreResponseDto;
import com.dolbom.hanium_project.dto.response.SensorDataResponseDto;
import com.dolbom.hanium_project.repository.ActivityScoreRepository;
import com.dolbom.hanium_project.repository.PatientEntityRepository;
import com.dolbom.hanium_project.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final PatientEntityRepository patientRepository;
    private final ActivityScoreRepository activityScoreRepository;
    private final SmsService smsService; // ✅ 1. SmsService 의존성 주입 추가

    @Transactional
    public void saveSensorData(SensorDataRequestDto requestDto) {
        PatientEntity patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("해당 피보호자를 찾을 수 없습니다. id=" + requestDto.getPatientId()));

        SensorEntity sensor = SensorEntity.builder()
                .temperature(requestDto.getTemperature())
                .humidity(requestDto.getHumidity())
                .fineDust(requestDto.getFineDust())
                .vocs(requestDto.getVocs())
                .movementCount(requestDto.getMovementCount())
                .lidar(requestDto.getLidar())
                .patient(patient)
                .build();

        sensorRepository.save(sensor);
    }

    // ✅ 2. 중복된 메서드를 하나로 합치고 문자 발송 로직을 포함
    @Transactional
    public void saveActivityScore(ActivityScoreRequestDto requestDto) {
        // 1. 피보호자 엔티티 조회
        PatientEntity patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("해당 피보호자를 찾을 수 없습니다. id=" + requestDto.getPatientId()));

        // 2. ActivityScore 엔티티 생성
        ActivityScore activityScore = ActivityScore.builder()
                .score(requestDto.getScore())
                .patient(patient)
                .build();

        // 3. DB에 저장
        activityScoreRepository.save(activityScore);

        // 4. 점수가 10점 이하일 경우 문자 발송
        if (requestDto.getScore() <= 10) {
            UserEntity guardian = patient.getUser();
            if (guardian != null && guardian.getPhoneNumber() != null) {
                String guardianPhoneNumber = guardian.getPhoneNumber().replaceAll("-", "");
                String message = String.format("[돌봄 시스템] 경고: %s 님의 활동 점수가 %d점으로 매우 낮습니다. 확인이 필요합니다.",
                        patient.getName(), requestDto.getScore());
                smsService.sendSms(guardianPhoneNumber, message);
            }
        }
    }

    @Transactional(readOnly = true)
    public SensorDataResponseDto getLatestSensorData(Long patientId) {
        SensorEntity latestSensor = sensorRepository.findTopByPatient_IdOrderByMeasuredAtDesc(patientId)
                .orElseThrow(() -> new IllegalArgumentException("센서 데이터가 없습니다. patientId=" + patientId));
        return new SensorDataResponseDto(latestSensor);
    }

    @Transactional(readOnly = true)
    public List<ActivityScoreResponseDto> getActivityScoreHistory(Long patientId) {
        List<ActivityScore> scoreHistory = activityScoreRepository.findAllByPatient_IdOrderByRecordedAtDesc(patientId);
        return scoreHistory.stream()
                .map(ActivityScoreResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ActivityScoreResponseDto getLatestActivityScore(Long patientId) {
        ActivityScore latestScore = activityScoreRepository.findTopByPatient_IdOrderByRecordedAtDesc(patientId)
                .orElseThrow(() -> new IllegalArgumentException("활동 점수 데이터가 없습니다. patientId=" + patientId));
        return new ActivityScoreResponseDto(latestScore);
    }
}