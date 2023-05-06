package com.example.healthcheck.service.health;

import com.example.healthcheck.util.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncRequestHealthCheckSynchronizer implements HealthCheckSynchronizer{
    private final Application application;
    private final RestTemplate restTemplate;

    @Override
    @Async("healthCheckTaskExecutor")
    public void synchronize() {
        restTemplate.getForObject(application.getUrl(),String.class);
    }
}
