package com.example.healthcheck.service.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SynchronousInnerHealCheckRequester implements HealthCheckRequester{

    private final HealthCheckManager healthCheckManager;

    @Override
    public void check(Long serverId) {
        healthCheckManager.check(serverId);
    }
}
