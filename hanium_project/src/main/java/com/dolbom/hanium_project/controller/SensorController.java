package com.dolbom.hanium_project.controller;

import com.dolbom.hanium_project.dto.request.SensorDataRequestDto;
import com.dolbom.hanium_project.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dolbom.hanium_project.dto.response.SensorDataResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.dolbom.hanium_project.dto.response.ActivityScoreResponseDto;
import java.util.List;

import java.util.List;
import com.dolbom.hanium_project.dto.request.ActivityScoreRequestDto;
@Tag(name = "Sensor API", description = "센서 데이터 저장 및 조회 API") // API 그룹 설정
@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping("/data")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorDataRequestDto requestDto) {
        sensorService.saveSensorData(requestDto);
        return ResponseEntity.ok("센서 데이터가 성공적으로 저장되었습니다.");
    }

    @Operation(summary = "최근 센서 데이터 조회", description = "특정 피보호자의 가장 최근 센서 데이터 1건을 조회합니다.")
    @GetMapping("/latest/{patientId}")
    public ResponseEntity<SensorDataResponseDto> getLatestSensorData(
            @Parameter(description = "피보호자 ID", required = true) @PathVariable Long patientId) {
        SensorDataResponseDto responseDto = sensorService.getLatestSensorData(patientId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "활동 점수 저장", description = "피보호자의 활동 점수를 받아 DB에 저장합니다.")
    @PostMapping("/activity")
    public ResponseEntity<String> receiveActivityScore(@RequestBody ActivityScoreRequestDto requestDto) {
        sensorService.saveActivityScore(requestDto);
        return ResponseEntity.ok("활동 점수가 성공적으로 저장되었습니다.");
    }
    @Operation(summary = "활동 점수 기록 조회", description = "특정 피보호자의 전체 활동 점수 기록을 최신순으로 조회합니다.")
    @GetMapping("/activity/{patientId}")
    public ResponseEntity<List<ActivityScoreResponseDto>> getActivityScoreHistory(
            @Parameter(description = "피보호자 ID", required = true) @PathVariable Long patientId) {
        List<ActivityScoreResponseDto> scoreHistory = sensorService.getActivityScoreHistory(patientId);
        return ResponseEntity.ok(scoreHistory);
    }
    @Operation(summary = "최근 활동 점수 조회", description = "특정 피보호자의 가장 최근 활동 점수 1건을 조회합니다.")
    @GetMapping("/activity/latest/{patientId}")
    public ResponseEntity<ActivityScoreResponseDto> getLatestActivityScore(
            @Parameter(description = "피보호자 ID", required = true) @PathVariable Long patientId) {
        ActivityScoreResponseDto latestScoreDto = sensorService.getLatestActivityScore(patientId);
        return ResponseEntity.ok(latestScoreDto);
    }
}