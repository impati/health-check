package com.example.healthcheck.util;

import static java.time.Duration.*;

import java.time.LocalDateTime;

public abstract class TimeConverter {

	private static final LocalDateTime START = LocalDateTime.of(2023, 5, 1, 0, 0);
	private static final long UNIT = 60;

	private TimeConverter() {
	}

	public static long convertToLong(final LocalDateTime dateTime) {
		return between(START, dateTime).getSeconds() / UNIT;
	}

	public static LocalDateTime convertToLocalDateTime(final long dateTime) {
		return START.plusMinutes(dateTime);
	}
}
