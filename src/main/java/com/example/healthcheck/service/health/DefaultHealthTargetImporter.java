package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultHealthTargetImporter implements HealthTargetImporter{
    private final ServerRepository serverRepository;
    private final HealthRecordRepository healthRecordRepository;

    @Override
    public List<HealthCheckServer> importTarget() {
        List<HealthCheckServer> result = new ArrayList<>();
        List<Server> activeServer = serverRepository.findActiveServer();
        LocalDateTime afterTime = now().minusDays(1);
        result.addAll(healthRecordRepository.findLatestRecordOfActiveServer(activeServer,afterTime)
                .stream()
                .map(HealthCheckServer::from)
                .toList());
        result.addAll(activeServer
                .stream()
                .map(server -> HealthCheckServer.of(server, now()))
                .filter(healthCheckServer -> !result.contains(healthCheckServer))
                .toList());
        return result;
    }

}
