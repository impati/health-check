package com.example.healthcheck.repository.alarm;

import com.example.healthcheck.entity.Alarm.AlarmRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmRecordRepository extends JpaRepository<AlarmRecord,Long> {
    Optional<AlarmRecord> findTopByOrderByCreatedAtDesc();
}
