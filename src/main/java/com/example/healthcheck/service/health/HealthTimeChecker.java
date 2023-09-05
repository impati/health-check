package com.example.healthcheck.service.health;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HealthTimeChecker {

	private static final int UNIT_TIME = 1000 * 60 * 30;

	private final HealthTargetChecker healthTargetChecker;

	private long time;

	@Scheduled(fixedRate = UNIT_TIME)
	public void timeCheck() {
		init();
		timePass();
		healthTargetChecker.checkForTarget(time);
	}

	private void init() {
		time = convertToLong(now());
	}

	private void timePass() {
		this.time += 1;
	}
}
