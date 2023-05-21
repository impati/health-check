package com.example.healthcheck.service.health;

import com.example.healthcheck.service.health.dto.HealthCheckServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ActiveTableHealthTargetChecker implements HealthTargetChecker{

    private final HealthTargetImporter healthTargetImporter;
    private final HealthCheckRequester healthCheckRequester;

    @Override
    public void checkForTarget(long time) {
        List<HealthCheckServer> targets = healthTargetImporter.importTarget(time);
        for (var target : targets) {
            healthCheckRequester.check(target.getServerId());
        }
    }

}
