package com.example.healthcheck.service.health.dto;

import lombok.AllArgsConstructor;

import java.util.PriorityQueue;

@AllArgsConstructor
public class CheckQueue {
    private PriorityQueue<HealthCheckServer> queue;

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
