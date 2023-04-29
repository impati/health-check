package com.example.healthcheck.service.health;


import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.config.RetryConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.common.DefaultEntityFinder;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@DataJpaTest
@Import({
        RetryConfig.class, JpaConfig.class,
        DefaultEntityFinder.class,HealthRecordSaver.class,
        DefaultHealthChecker.class
})
class HealthRecordSaverTest {

    @Autowired
    private HealthRecordSaver healthRecordSaver;

    @MockBean
    private HealthChecker healthChecker;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;
    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("헬스 체크 성공시 저장")
    public void saveRecord_SUCCESS() throws Exception{
        // given
        Server existServer = serverSteps.createExistServer();

        willDoNothing().given(healthChecker).check(existServer);

        // when
        healthRecordSaver.saveRecord(existServer.getId());

        // then
        HealthRecord healthRecord = healthRecordRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow();

        assertThat(healthRecord.getHealthStatus()).isEqualTo(HealthStatus.SUCCESS);

    }

    @Test
    @DisplayName("헬스 체크 실패시 저장")
    public void saveRecord_FAIL() throws Exception{
        // given
        Server nonexistentServer = serverSteps.createNonexistentServer();

        willThrow(HealthCheckException.class).given(healthChecker).check(nonexistentServer);

        // when
        healthRecordSaver.saveRecord(nonexistentServer.getId());

        // then
        HealthRecord healthRecord = healthRecordRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow();

        assertThat(healthRecord.getHealthStatus()).isEqualTo(HealthStatus.FAIL);

    }

}