package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.server.Server;

public interface HealthChecker {

	void check(final Server server);
}
