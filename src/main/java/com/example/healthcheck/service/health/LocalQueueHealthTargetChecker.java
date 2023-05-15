package com.example.healthcheck.service.health;

import com.example.healthcheck.service.health.dto.CheckQueue;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalQueueHealthTargetChecker implements HealthTargetChecker {
    private final static int SYNCHRONIZATION_TIME = 3;
    private final HealthCheckRequester healthCheckRequester;
    private final HealthTargetImporter healthTargetImporter;
    private CheckQueue queue;

    @Async("healthTargetCheckTaskExecutor")
    @Override
    public void checkForTarget(long time) {
        if(isInitialTime()) initialize();
        if(isTimeToSync()) synchronize();
        while (queue.isCheckTime(time)) {
            HealthCheckServer server = queue.poll();
            server.update();
            queue.add(server);
            healthCheckRequester.check(server.getServerId());
        }
    }

    private boolean isInitialTime(){
        return queue == null || queue.getLastSynchronizationTime() == 0;
    }

    private void initialize(){
        queue = new CheckQueue(healthTargetImporter.importTarget());
    }

    private boolean isTimeToSync(){
        long currentTime = convertToLong(now());
        long passedTime = currentTime - queue.getLastSynchronizationTime();
        return passedTime >= SYNCHRONIZATION_TIME;
    }

    private void synchronize() {
        queue.clear();
        queue = new CheckQueue(healthTargetImporter.importTarget());
    }

}
