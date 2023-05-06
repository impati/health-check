package com.example.healthcheck.repository.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.HealthRecordSteps;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class HealthRecordRepositoryTest {

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
    @DisplayName("서버에 대해 가장 최근에 실시한 헬스 체크 가져오기")
    public void findTopByServerOrderByCreatedAtDescTest() throws Exception{
        // given
        Server server = serverSteps.createDefault();

        HealthRecord firstRecord = healthRecordSteps.create(server);
        Thread.sleep(100);
        HealthRecord midRecord = healthRecordSteps.create(server);
        Thread.sleep(100);
        HealthRecord lastRecord = healthRecordSteps.create(server);

        // when
        Optional<HealthRecord> response = healthRecordRepository.findTopByServerOrderByCreatedAtDesc(server);

        // then
        assertThat(response).isPresent();
        assertThat(response.get().getId()).isEqualTo(lastRecord.getId());

    }

    @Test
    @DisplayName("여러 서버 중 특정 서버 대해 가장 최근에 실시한 헬스 체크 가져오기")
    public void findTopByServerOrderByCreatedAtDescWithManyServer() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        Server other = serverSteps.createDefault();
        healthRecordSteps.create(server);
        HealthRecord lastRecord = healthRecordSteps.create(server);
        healthRecordSteps.create(other);

        // when
        Optional<HealthRecord> response = healthRecordRepository.findTopByServerOrderByCreatedAtDesc(server);

        // then
        assertThat(response).isPresent();
        assertThat(response.get().getId()).isEqualTo(lastRecord.getId());

    }

}