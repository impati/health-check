package com.example.healthcheck.service.health.dto;

import java.util.List;
import java.util.PriorityQueue;

import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static java.time.LocalDateTime.now;

public class CheckQueue {
    private PriorityQueue<HealthCheckServer> queue;
    private long lastSynchronizationTime  = 0;

    public CheckQueue(List<HealthCheckServer> healthCheckServers) {
        this.lastSynchronizationTime = convertToLong(now());
        this.queue = new PriorityQueue<>(healthCheckServers);
    }

    public long getLastSynchronizationTime(){
        return lastSynchronizationTime;
    }

    public int size() {return queue.size();}

    public boolean isNonEmpty(){
        return !queue.isEmpty();
    }

    public HealthCheckServer peek(){
        if(isNonEmpty()){
            return queue.peek();
        }
        return null;
    }

    public boolean isCheckTime(long currentTime){
        return peek().getCheckTime() == currentTime;
    }

    public HealthCheckServer poll(){
        if(isNonEmpty()){
            return queue.poll();
        }
        return null;
    }

    public void add(HealthCheckServer healthCheckServer){
        queue.add(healthCheckServer);
    }

    public void clear(){
        queue.clear();
    }
}
