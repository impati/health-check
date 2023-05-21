package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.health.ActiveServer;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.health.ActiveServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActiveTableManager {
    private final ActiveServerRepository activeServerRepository;

    public List<ActiveServer> findTargetServer(long time){
        return activeServerRepository.findActiveServerByTargetTime(time);
    }

    @Transactional
    public void updateTargetTime(Server server){
        ActiveServer activeServer = findActiveServer(server);
        activeServer.updateTargetTime(server.getInterval());
    }

    @Transactional
    public void delete(Server server){
        activeServerRepository.delete(findActiveServer(server));
    }

    @Transactional
    public void insert(Server server,long time){
        if(!server.isActive()) return;
        activeServerRepository.save(ActiveServer.builder()
                .server(server)
                .targetTime(time + server.getInterval())
                .build());
    }

    private ActiveServer findActiveServer(Server server){
        return activeServerRepository.findActiveServerByServer(server)
                .orElseThrow(() -> new HealthCheckException(ErrorCode.NOT_ACTIVE_SERVER));
    }
}
