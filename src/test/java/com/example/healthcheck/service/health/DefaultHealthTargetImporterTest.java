package com.example.healthcheck.service.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import com.example.healthcheck.steps.HealthRecordSteps;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class,DefaultHealthTargetImporter.class})
class DefaultHealthTargetImporterTest {

    @Autowired
    private HealthTargetImporter healthTargetImporter;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private ServerRepository serverRepository;

    private HealthRecordSteps healthRecordSteps;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        healthRecordSteps = new HealthRecordSteps(healthRecordRepository);
        serverSteps = new ServerSteps(serverRepository);
    }


    @Test
    @DisplayName("헬스 체크 대상 서버를 HealthCheckServer 객체로 가져오기 테스트")
    public void importTargetTest() throws Exception{
        // given
        Server firstTestServer = serverSteps.create("[테스트 서버 1]", true,3);
        Server secondTestServer = serverSteps.create("[테스트 서버 2]", true,10);
        Server thirdTestServer = serverSteps.create("[테스트 서버 3]", true,30);

        HealthRecord firstRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);
        HealthRecord lastRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);

        HealthRecord firstRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);
        HealthRecord lastRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);

        // when
        List<HealthCheckServer> healthCheckServers = healthTargetImporter.importTarget();

        // then
        assertThat(healthCheckServers.size()).isEqualTo(3);

    }


}