package com.example.healthcheck.service.health;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.Alarm.AlarmRecord;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.alarm.AlarmRecordRepository;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.service.alarm.AlarmSender;
import com.example.healthcheck.service.server.ServerStatusManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultHealthCheckFailManager implements HealthCheckFailManager {

	private static final int MULTIPLE = 2;

	private final AlarmSender alarmSender;
	private final AlarmRecordRepository alarmRecordRepository;
	private final HealthRecordRepository healthRecordRepository;
	private final ServerStatusManager serverStatusManager;
	private final ActiveTableManager activeTableManager;

	public void process(final Server server) {
		if (isDeActivate(server)) {
			serverStatusManager.deactivate(server);
		}

		saveHealRecord(server);
		activeTableManager.updateTargetTime(server);
		alarmSender.send(server, server.getEmail());
		saveAlarmRecord(server);
	}

	private void saveHealRecord(final Server server) {
		healthRecordRepository.save(HealthRecord.builder()
			.healthStatus(HealthStatus.FAIL)
			.server(server)
			.build());
	}

	private void saveAlarmRecord(final Server server) {
		alarmRecordRepository.save(AlarmRecord.builder()
			.server(server)
			.build());
	}

	private boolean isDeActivate(final Server server) {
		final Optional<AlarmRecord> optionalAlarmRecord = alarmRecordRepository.findTopByOrderByCreatedAtDesc();
		if (optionalAlarmRecord.isPresent()) {
			AlarmRecord alarmRecord = optionalAlarmRecord.get();
			return isOverTime(alarmRecord.getCreatedAt(), server);
		}

		return false;
	}

	private boolean isOverTime(final LocalDateTime savedTime, final Server server) {
		return betweenTime(savedTime) < MULTIPLE * server.getInterval();
	}

	private int betweenTime(final LocalDateTime savedTime) {
		return (int)(Duration.between(savedTime, LocalDateTime.now()).getSeconds() / 60);
	}
}
