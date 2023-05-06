package com.example.healthcheck.steps;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;

public class HealthRecordSteps {

    private final HealthRecordRepository healthRecordRepository;

    public HealthRecordSteps(HealthRecordRepository healthRecordRepository) {
        this.healthRecordRepository = healthRecordRepository;
    }


    public HealthRecord create(Server server){
        return healthRecordRepository.save(HealthRecord.builder()
                .server(server)
                .healthStatus(HealthStatus.SUCCESS)
                .build());
    }
}
