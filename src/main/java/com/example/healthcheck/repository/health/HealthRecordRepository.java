package com.example.healthcheck.repository.health;

import com.example.healthcheck.entity.health.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord,Long> {
}
