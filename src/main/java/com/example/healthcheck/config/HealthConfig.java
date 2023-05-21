package com.example.healthcheck.config;

import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.service.health.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class HealthConfig {

    @Bean
    public HealthTimeChecker healthTimeChecker(HealthTargetChecker healthTargetChecker){
        return new HealthTimeChecker(healthTargetChecker);
    }

    @Bean
    public HealthTargetChecker healthTargetChecker(HealthCheckRequester healthCheckRequester , HealthTargetImporter targetImporter, HealthRecordRepository healthRecordRepository){
        return new ActiveTableHealthTargetChecker(targetImporter,healthCheckRequester,healthRecordRepository);
    }

    @Bean
    public HealthCheckRequester healthCheckRequester(HealthCheckManager healthCheckManager){
        return new AsyncInnerHealthCheckRequester(healthCheckManager);
    }

    @Bean
    public HealthTargetImporter healthTargetImporter(ActiveTableManager activeTableManager){
        return new ActiveTableHealthTargetImporter(activeTableManager);
    }



}
