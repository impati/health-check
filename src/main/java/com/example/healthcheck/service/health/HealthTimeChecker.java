package com.example.healthcheck.service.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
public class HealthTimeChecker {

    private final static int UNIT_TIME = 1000 * 60;
    private final HealthTargetChecker healthTargetChecker;
    private long time;
    private long offset;

    @EventListener(ApplicationReadyEvent.class)
    public void timeCheck(){
        init();
        while(true){
            sleep();
            timePass();
            healthTargetChecker.checkForTarget(time);
        }
    }

    private void init(){
        offset = convertToLong(now());
        time = offset;
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

}
