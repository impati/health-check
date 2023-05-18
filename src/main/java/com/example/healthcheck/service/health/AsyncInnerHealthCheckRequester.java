package com.example.healthcheck.service.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public class AsyncInnerHealthCheckRequester implements HealthCheckRequester{

    private final HealthCheckManager healthCheckManager;

    @Async("healthCheckRequesterTaskExecutor")
    @Override
    public void check(Long serverId) {
        healthCheckManager.check(serverId);
    }

}
