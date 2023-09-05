package com.example.healthcheck.service.health;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.alarm.AlarmRecordRepository;
import com.example.healthcheck.repository.health.ActiveServerRepository;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.alarm.AlarmSender;
import com.example.healthcheck.steps.ServerSteps;

@SpringBootTest
@Transactional
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

	@Autowired
	private ActiveTableManager activeTableManager;

	@Autowired
	private ActiveServerRepository activeServerRepository;

	private ServerSteps serverSteps;

	@BeforeEach
	void setup() {
		serverSteps = new ServerSteps(serverRepository);
	}

	@AfterEach
	void tearDown() {
		activeServerRepository.deleteAll();
		serverRepository.deleteAll();
	}

	@Test
	@DisplayName("헬스 체크 첫번째 실패 프로세스 테스트 - 1.헬스 체크 결과 저장 2.알람 보내기 3. 알람 보낸 기록 저장")
	void DefaultHealthCheckFailManager_processTest_FirstFail() {
		// given
		final Server server = serverSteps.createDefault();
		activeTableManager.insert(server, 30);
		willDoNothing().given(alarmSender).send(server, server.getEmail());

		// when
		healthCheckFailManager.process(server);

		// then
		final HealthRecord healthRecord = healthRecordRepository.findAll().stream().findFirst().get();
		assertThat(healthRecord.getHealthStatus()).isEqualTo(HealthStatus.FAIL);
		assertThat(alarmRecordRepository.findAll()).hasSize(1);
		assertThat(server.isActive()).isTrue();
	}
}
