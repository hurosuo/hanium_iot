package com.dolbom.hanium_project.dto.response;

import com.dolbom.hanium_project.domain.ActivityScore;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ActivityScoreResponseDto {
    private final Long scoreId;
    private final int score;
    private final LocalDateTime recordedAt;

    public ActivityScoreResponseDto(ActivityScore activityScore) {
        this.scoreId = activityScore.getId();
        this.score = activityScore.getScore();
        this.recordedAt = activityScore.getRecordedAt();
    }
}