package com.example.healthcheck.service.health;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthTimeChecker {

	private static final int UNIT_TIME = 1000 * 60;

	private final HealthTargetChecker healthTargetChecker;

	@Scheduled(fixedRate = UNIT_TIME)
	public void timeCheck() {
		healthTargetChecker.checkForTarget(convertToLong(now()));
	}
}
