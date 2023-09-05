package com.example.healthcheck.service.health;

import java.util.List;

import com.example.healthcheck.service.health.dto.HealthCheckServer;

public interface HealthTargetImporter {

    List<HealthCheckServer> importTarget(final long time);
}
