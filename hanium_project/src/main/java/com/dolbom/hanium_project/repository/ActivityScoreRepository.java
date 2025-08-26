package com.dolbom.hanium_project.repository;
import java.util.List;
import java.util.Optional;
import com.dolbom.hanium_project.domain.ActivityScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityScoreRepository extends JpaRepository<ActivityScore, Long> {
    List<ActivityScore> findAllByPatient_IdOrderByRecordedAtDesc(Long patientId);
    Optional<ActivityScore> findTopByPatient_IdOrderByRecordedAtDesc(Long patientId);
}