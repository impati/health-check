package com.example.healthcheck.service.health;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;

import org.springframework.scheduling.annotation.Async;

import com.example.healthcheck.service.health.dto.CheckQueue;
import com.example.healthcheck.service.health.dto.HealthCheckServer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LocalQueueHealthTargetChecker implements HealthTargetChecker {

	private static final int SYNCHRONIZATION_TIME = 30;

	private final HealthCheckRequester healthCheckRequester;
	private final HealthTargetImporter healthTargetImporter;

	private CheckQueue queue;

	@Async("healthTargetCheckTaskExecutor")
	@Override
	public void checkForTarget(final long time) {
		if (isInitialTime()) {
			initialize();
		}
		if (isTimeToSync()) {
			synchronize();
		}

		while (queue.isCheckTime(time)) {
			HealthCheckServer server = queue.poll();
			server.update();
			queue.add(server);
			healthCheckRequester.check(server.getServerId());
		}
	}

	private boolean isInitialTime() {
		return queue == null || queue.getLastSynchronizationTime() == 0;
	}

	private void initialize() {
		queue = new CheckQueue(healthTargetImporter.importTarget(convertToLong(now())));
	}

	private boolean isTimeToSync() {
		final long currentTime = convertToLong(now());
		final long passedTime = currentTime - queue.getLastSynchronizationTime();
		return passedTime >= SYNCHRONIZATION_TIME;
	}

	private void synchronize() {
		queue.clear();
		queue = new CheckQueue(healthTargetImporter.importTarget(convertToLong(now())));
	}
}
