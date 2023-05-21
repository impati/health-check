package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class DefaultHealthCheckSuccessManager implements HealthCheckSuccessManager {

    private final HealthRecordRepository healthRecordRepository;
    private final ActiveTableManager activeTableManager;

    public void process(Server server){
        healthRecordRepository.save(HealthRecord.builder()
                .healthStatus(HealthStatus.SUCCESS)
                .server(server)
                .build());
        activeTableManager.updateTargetTime(server);
    }
}
