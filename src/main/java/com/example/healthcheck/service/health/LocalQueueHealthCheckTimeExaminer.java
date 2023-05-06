package com.example.healthcheck.service.health;

import com.example.healthcheck.service.health.dto.CheckQueue;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static java.time.LocalDateTime.now;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@Scope(value = SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class LocalQueueHealthCheckTimeExaminer implements HealthCheckTimeExaminer{
    private final static int UNIT_TIME = 1000 * 60;
    private final static int SYNCHRONIZATION_TIME = 10;
    private final HealthCheckInitializer healthCheckInitializer;
    private final HealthCheckRequester healthCheckRequester;
    private final HealthCheckSynchronizer healthCheckSynchronizer;
    private CheckQueue queue;
    private long time;
    private long offset;

    @Override
    public void examine() {
        init();
        while(isContinue()){
            sleep();
            timePass();
            checkQueue();
        }
        clear();
        synchronizeQueue();
    }

    private void init(){
        queue = new CheckQueue(healthCheckInitializer.getActiveServer());
        offset = convertToLong(now());
        time = offset;
    }

    private boolean isContinue(){
        return time < SYNCHRONIZATION_TIME  + offset && queue.isNonEmpty();
    }

    private void sleep(){
        try {
            Thread.sleep(UNIT_TIME);
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void timePass(){
        this.time += 1;
    }

    private void checkQueue(){
        while(queue.isCheckTime(time)){
            HealthCheckServer server = queue.poll();
            server.update();
            queue.add(server);
            healthCheckRequester.check(server.getServerId());
        }
    }

    private void clear(){
        queue.clear();
    }

    private void synchronizeQueue(){
        healthCheckSynchronizer.synchronize();
    }

}
