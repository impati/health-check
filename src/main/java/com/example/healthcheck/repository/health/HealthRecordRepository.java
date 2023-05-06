package com.example.healthcheck.repository.health;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthRecordRepository extends JpaRepository<HealthRecord,Long> {
    Optional<HealthRecord> findTopByServerOrderByCreatedAtDesc(Server server);
}
