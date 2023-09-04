package com.example.healthcheck.service.health;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.service.common.EntityFinder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HealthCheckManager {

    private final HealthChecker healthChecker;
    private final HealthCheckFailManager healthCheckFailManager;
    private final HealthCheckSuccessManager healthCheckSuccessManager;
    private final EntityFinder entityFinder;

    public void check(final Long serverId) {
        final Server server = entityFinder.findOrElseThrow(serverId, Server.class);
        if (!server.isActive()) {
            return;
        }

        try {
            healthChecker.check(server);
            healthCheckSuccessManager.process(server);
        } catch (HealthCheckException e) {
            healthCheckFailManager.process(server);
        }
    }
}
