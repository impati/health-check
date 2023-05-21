package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.Alarm.AlarmRecord;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.alarm.AlarmRecordRepository;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.service.alarm.AlarmSender;
import com.example.healthcheck.service.server.ServerStatusManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DefaultHealthCheckFailManager implements  HealthCheckFailManager{

    private final static int MULTIPLE = 2;
    private final AlarmSender alarmSender;
    private final AlarmRecordRepository alarmRecordRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final ServerStatusManager serverStatusManager;
    private final ActiveTableManager activeTableManager;

    public void process(Server server) {
        if(isDeActivate(server)) serverStatusManager.deactivate(server);
        saveHealRecord(server);
        activeTableManager.updateTargetTime(server);
        alarmSender.send(server,server.getEmail());
        saveAlarmRecord(server);
    }

    private void saveHealRecord(Server server) {
        healthRecordRepository.save(HealthRecord.builder()
                .healthStatus(HealthStatus.FAIL)
                .server(server)
                .build());
    }

    private void saveAlarmRecord(Server server){
        alarmRecordRepository.save(AlarmRecord.builder()
                .server(server)
                .build());
    }

    private boolean isDeActivate(Server server){
        Optional<AlarmRecord> optionalAlarmRecord = alarmRecordRepository.findTopByOrderByCreatedAtDesc();
        if(optionalAlarmRecord.isPresent()){
            AlarmRecord alarmRecord = optionalAlarmRecord.get();
            return isOverTime(alarmRecord.getCreatedAt(),server);
        }
        return false;
    }

    private boolean isOverTime(LocalDateTime savedTime,Server server){
        return betweenTime(savedTime) <  MULTIPLE * server.getInterval();
    }

    private int betweenTime(LocalDateTime savedTime){
        return (int)(Duration.between(savedTime, LocalDateTime.now()).getSeconds() / 60);
    }

}
