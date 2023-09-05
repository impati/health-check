package com.example.healthcheck.service.health.dto;

import static com.example.healthcheck.util.TimeConverter.*;

import java.time.LocalDateTime;
import java.util.Objects;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class HealthCheckServer implements Comparable<HealthCheckServer> {

	private long serverId;
	private long checkTime;
	private int interval;

	public static HealthCheckServer of(final Server server, final LocalDateTime lastCheckTime) {
		return new HealthCheckServer(server.getId(), computeNextCheckTime(lastCheckTime, server.getInterval()),
			server.getInterval());
	}

	public static HealthCheckServer of(final Server server, final long targetTime) {
		return new HealthCheckServer(server.getId(), targetTime, server.getInterval());
	}

	public static HealthCheckServer from(final HealthRecord healthRecord) {
		final Server server = healthRecord.getServer();
		return new HealthCheckServer(
			server.getId(),
			computeNextCheckTime(healthRecord.getCreatedAt(), server.getInterval()),
			server.getInterval());
	}

	private static long computeNextCheckTime(final LocalDateTime time, final int interval) {
		return convertToLong(time) + interval;
	}

	public void update() {
		checkTime += interval;
	}

	@Override
	public int compareTo(final HealthCheckServer that) {
		return Long.compare(this.checkTime, that.checkTime);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof HealthCheckServer that)) {
			return false;
		}

		return serverId == that.serverId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(serverId);
	}
}
