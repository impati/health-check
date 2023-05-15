package com.example.healthcheck.service.health.dto;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.healthcheck.util.TimeConverter.convertToLong;

@ToString
@Getter
@AllArgsConstructor
public class HealthCheckServer implements Comparable<HealthCheckServer>{
    private long serverId;
    private long checkTime;
    private int interval;

    public static HealthCheckServer of(Server server, LocalDateTime lastCheckTime){
        return new HealthCheckServer(server.getId(), computeNextCheckTime(lastCheckTime,server.getInterval()), server.getInterval());
    }

    public static HealthCheckServer from(HealthRecord healthRecord){
        Server server = healthRecord.getServer();
        return new HealthCheckServer(server.getId(),computeNextCheckTime(healthRecord.getCreatedAt() ,server.getInterval()),server.getInterval());
    }

    private static long computeNextCheckTime(LocalDateTime time,int interval){
        return convertToLong(time) + interval;
    }

    public void update(){
        checkTime += interval;
    }

    @Override
    public int compareTo(HealthCheckServer that) {
        return Long.compare(this.checkTime,that.checkTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthCheckServer that)) return false;
        return serverId == that.serverId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId);
    }
}
