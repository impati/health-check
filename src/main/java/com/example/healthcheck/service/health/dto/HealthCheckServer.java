package com.example.healthcheck.service.health.dto;

import com.example.healthcheck.entity.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.healthcheck.util.TimeConverter.convertToLong;

@Getter
@AllArgsConstructor
public class HealthCheckServer implements Comparable<HealthCheckServer>{
    private long serverId;
    private long checkTime;
    private int interval;

    public static HealthCheckServer of(Server server, LocalDateTime lastCheckTime){
        return new HealthCheckServer(server.getId(), convertToLong(lastCheckTime) + server.getInterval(), server.getInterval());
    }

    public void update(){
        checkTime += interval;
    }

    @Override
    public int compareTo(HealthCheckServer that) {
        return Long.compare(this.checkTime,that.checkTime);
    }
}
