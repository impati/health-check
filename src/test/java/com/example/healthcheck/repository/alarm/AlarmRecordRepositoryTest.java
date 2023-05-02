package com.example.healthcheck.repository.alarm;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.Alarm.AlarmRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class AlarmRecordRepositoryTest {

    @Autowired
    private AlarmRecordRepository alarmRecordRepository;

    @Autowired
    private ServerRepository serverRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("findTopByOrderByCreatedAtDescTest")
    public void findTopByOrderByCreatedAtDescTest() throws Exception{

        Server one = serverSteps.createWithEmail("one");
        Server two = serverSteps.createWithEmail("two");
        Server three = serverSteps.createWithEmail("three");

        alarmRecordRepository.save(AlarmRecord.builder()
                .server(one)
                .build());
        Thread.sleep(100);

        alarmRecordRepository.save(AlarmRecord.builder()
                .server(two)
                .build());
        Thread.sleep(100);

        alarmRecordRepository.save(AlarmRecord.builder()
                .server(three)
                .build());
        Thread.sleep(100);

        AlarmRecord result = alarmRecordRepository.findTopByOrderByCreatedAtDesc().orElseThrow();

        assertThat(result.getServer()).isEqualTo(three);

    }


}