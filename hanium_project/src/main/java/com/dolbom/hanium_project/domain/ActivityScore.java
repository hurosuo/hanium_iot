package com.dolbom.hanium_project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_scores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @Column(nullable = false)
    private int score; // 0 ~ 100 사이의 활동 점수

    @Column(nullable = false)
    private LocalDateTime recordedAt; // 기록된 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @Builder
    public ActivityScore(int score, PatientEntity patient) {
        this.score = score;
        this.patient = patient;
        this.recordedAt = LocalDateTime.now();
    }
}