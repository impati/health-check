package com.example.healthcheck.service.health;

import static java.time.LocalDateTime.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultHealthTargetImporter implements HealthTargetImporter {

    private final ServerRepository serverRepository;
    private final HealthRecordRepository healthRecordRepository;

    @Override
    public List<HealthCheckServer> importTarget(final long time) {
        final List<HealthCheckServer> result = new ArrayList<>();
        final List<Server> activeServer = serverRepository.findActiveServer();
        final LocalDateTime afterTime = now().minusDays(1);

        result.addAll(healthRecordRepository.findLatestRecordOfActiveServer(activeServer, afterTime)
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
