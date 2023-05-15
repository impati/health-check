package com.example.healthcheck.service.health;

import com.example.healthcheck.service.health.dto.HealthCheckServer;

import java.util.List;

public interface HealthTargetImporter {
    List<HealthCheckServer> importTarget();
}
