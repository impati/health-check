package com.example.healthcheck.config;

import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class HealthConfig {

    private final ServerRepository serverRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final HealthCheckManager healthCheckManager;

    @Bean
    public HealthTimeChecker healthTimeChecker(){
        return new HealthTimeChecker(healthTargetChecker());
    }

    @Bean
    public HealthTargetChecker healthTargetChecker(){
        return new LocalQueueHealthTargetChecker(healthCheckRequester(),healthTargetImporter());
    }

    @Bean
    public HealthCheckRequester healthCheckRequester(){
        return new AsyncInnerHealthCheckRequester(healthCheckManager);
    }

    @Bean
    public HealthTargetImporter healthTargetImporter(){
        return new DefaultHealthTargetImporter(serverRepository,healthRecordRepository);
    }

}
