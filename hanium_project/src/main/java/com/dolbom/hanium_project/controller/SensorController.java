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


import java.util.List;

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
}