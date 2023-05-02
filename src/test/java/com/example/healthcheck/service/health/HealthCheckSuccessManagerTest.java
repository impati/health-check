package com.example.healthcheck.service.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
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
@Import({JpaConfig.class,DefaultHealthCheckSuccessManager.class})
class HealthCheckSuccessManagerTest {

    @Autowired
    private HealthCheckSuccessManager healthCheckSuccessManager;

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
    @DisplayName("헬스 체크 성공시 PROCESS")
    public void healthCheckSuccessManager_processTest() throws Exception{
        // given
        Server server = serverSteps.createDefault();

        // when
        healthCheckSuccessManager.process(server);

        // then
        assertThat(healthRecordRepository.findAll().size())
                .isEqualTo(1);

    }

}