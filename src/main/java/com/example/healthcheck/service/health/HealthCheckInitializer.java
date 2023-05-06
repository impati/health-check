package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.PriorityQueue;

import static java.util.stream.Collectors.toCollection;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckInitializer {
    private final ServerRepository serverRepository;
    private final HealthRecordRepository healthRecordRepository;

    public PriorityQueue<HealthCheckServer> getActiveServer(){
        return serverRepository.findActiveServer()
                .stream()
                .map(this::covert)
                .collect(toCollection(PriorityQueue::new));
    }

    private HealthCheckServer covert(Server server){
        Optional<HealthRecord> recentlyRecord = healthRecordRepository.findTopByServerOrderByCreatedAtDesc(server);
        return recentlyRecord
                    .map(healthRecord -> HealthCheckServer.of(server, healthRecord.getCreatedAt()))
                    .orElseGet(() -> HealthCheckServer.of(server, LocalDateTime.now()));
    }

}
