package com.dolbom.hanium_project.dto.response;

import com.dolbom.hanium_project.domain.SensorEntity;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class SensorDataResponseDto {
    private final Long sensorId;
    private final double temperature;
    private final double humidity;
    private final double fineDust;
    private final double vocs;
    private final int movementCount;
    private final int lidar;
    private final LocalDateTime measuredAt;

    public SensorDataResponseDto(SensorEntity sensor) {
        this.sensorId = sensor.getId();
        this.temperature = sensor.getTemperature();
        this.humidity = sensor.getHumidity();
        this.fineDust = sensor.getFineDust();
        this.vocs = sensor.getVocs();
        this.movementCount = sensor.getMovementCount();
        this.lidar = sensor.getLidar();
        this.measuredAt = sensor.getMeasuredAt();
    }
}