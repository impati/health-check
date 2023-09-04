package com.example.healthcheck.repository.alarm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.healthcheck.entity.Alarm.AlarmRecord;

public interface AlarmRecordRepository extends JpaRepository<AlarmRecord, Long> {

	Optional<AlarmRecord> findTopByOrderByCreatedAtDesc();
}
