package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.service.common.EntityFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class HealthRecordSaver {

    private final HealthChecker healthChecker;
    private final HealthRecordRepository healthRecordRepository;
    private final EntityFinder entityFinder;

    public void saveRecord(Long serviceId){
        Server server = entityFinder.findOrElseThrow(serviceId, Server.class);
        try {
            healthChecker.check(server);
            healthRecordRepository.save(create(server,HealthStatus.SUCCESS));
        }catch (HealthCheckException e){
            healthRecordRepository.save(create(server,HealthStatus.FAIL));
        }
    }

    private HealthRecord create(Server server, HealthStatus healthStatus){
        return HealthRecord.builder()
                .server(server)
                .healthStatus(healthStatus)
                .build();
    }

}
