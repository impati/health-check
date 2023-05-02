package com.example.healthcheck.service.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.alarm.AlarmRecordRepository;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.alarm.AlarmSender;
import com.example.healthcheck.service.alarm.MailAlarmSender;
import com.example.healthcheck.service.common.DefaultEntityFinder;
import com.example.healthcheck.service.server.ServerDeActivator;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;

@DataJpaTest
@Import({JpaConfig.class, MailAlarmSender.class,
        DefaultEntityFinder.class, ServerDeActivator.class,
        DefaultHealthCheckFailManager.class
})
class DefaultHealthCheckFailManagerTest {

    @Autowired
    private HealthCheckFailManager healthCheckFailManager;

    @MockBean
    private AlarmSender alarmSender;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private AlarmRecordRepository alarmRecordRepository;

    private ServerSteps serverSteps;


    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("헬스 체크 첫번째 실패 프로세스 테스트 - 1.헬스 체크 결과 저장 2.알람 보내기 3. 알람 보낸 기록 저장")
    public void DefaultHealthCheckFailManager_processTest_FirstFail() throws Exception{
        // given
        Server server = serverSteps.createDefault();

        willDoNothing().given(alarmSender).send(server, server.getEmail());

        // when
        healthCheckFailManager.process(server);

        // then
        HealthRecord healthRecord = healthRecordRepository.findAll().stream().findFirst().get();
        assertThat(healthRecord.getHealthStatus()).isEqualTo(HealthStatus.FAIL);
        assertThat(alarmRecordRepository.findAll().size()).isEqualTo(1);
        assertThat(server.isActive()).isTrue();
    }

    @Test
    @DisplayName("헬스 체크 연속 두번 실패 프로세스 테스트 - 1.서버 비활성화 2.헬스 체크 결과 저장 3.알람 보내기 4. 알람 보낸 기록 저장")
    public void DefaultHealthCheckFailManager_processTest_SecondFail() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        String email = server.getEmail();

        willDoNothing().given(alarmSender).send(server,email);
        willDoNothing().given(alarmSender).send(server,email);

        // when
        healthCheckFailManager.process(server);
        healthCheckFailManager.process(server);

        // then
        List<HealthRecord> healthRecords = healthRecordRepository.findAll();
        assertThat(healthRecords.stream()
                .filter(record -> record.getHealthStatus() == HealthStatus.FAIL)
                .count())
                .isEqualTo(2);
        assertThat(alarmRecordRepository.findAll().size()).isEqualTo(2);
        assertThat(server.isActive()).isFalse();
    }

}