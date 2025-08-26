package com.dolbom.hanium_project.dto.request;

import lombok.Getter;

@Getter
public class ActivityScoreRequestDto {
    private Long patientId;
    private int score;
}