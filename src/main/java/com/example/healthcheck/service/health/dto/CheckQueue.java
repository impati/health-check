package com.example.healthcheck.service.health.dto;

import static com.example.healthcheck.util.TimeConverter.*;
import static java.time.LocalDateTime.*;

import java.util.List;
import java.util.PriorityQueue;

public class CheckQueue {

	private final PriorityQueue<HealthCheckServer> queue;
	private final long lastSynchronizationTime;

	public CheckQueue(final List<HealthCheckServer> healthCheckServers) {
		this.lastSynchronizationTime = convertToLong(now());
		this.queue = new PriorityQueue<>(healthCheckServers);
	}

	public long getLastSynchronizationTime() {
		return lastSynchronizationTime;
	}

	public int size() {
		return queue.size();
	}

	public boolean isNonEmpty() {
		return !queue.isEmpty();
	}

	public HealthCheckServer peek() {
		if (isNonEmpty()) {
			return queue.peek();
		}

		return null;
	}

	public boolean isCheckTime(final long currentTime) {
		return peek().getCheckTime() == currentTime;
	}

	public HealthCheckServer poll() {
		if (isNonEmpty()) {
			return queue.poll();
		}

		return null;
	}

	public void add(final HealthCheckServer healthCheckServer) {
		queue.add(healthCheckServer);
	}

	public void clear() {
		queue.clear();
	}
}
