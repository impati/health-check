package com.example.healthcheck.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.healthcheck.service.alarm.AlarmSender;
import com.example.healthcheck.service.alarm.StubAlarmSender;

@TestConfiguration
public class TestConfig {

	@Bean
	public AlarmSender stubAlarmSender() {
		return new StubAlarmSender();
	}

	@Bean
	public DatabaseCleaner databaseCleaner() {
		return new DatabaseCleaner();
	}
}
