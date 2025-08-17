package com.dolbom.hanium_project.repository;

import com.dolbom.hanium_project.domain.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SensorRepository extends JpaRepository<SensorEntity, Long> {
    Optional<SensorEntity> findTopByPatient_IdOrderByMeasuredAtDesc(Long patientId);
    List<SensorEntity> findAllByPatient_IdOrderByMeasuredAtDesc(Long patientId);
}