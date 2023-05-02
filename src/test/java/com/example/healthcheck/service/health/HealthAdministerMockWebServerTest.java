package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HealthAdministerMockWebServerTest {

    @Autowired
    private HealthAdminister healthAdminister;

    private MockWebServer mockWebServer;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup() throws IOException {
        serverSteps = new ServerSteps(serverRepository);
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("헬스 체크 성공시 저장")
    public void saveRecord_SUCCESS() throws Exception{
        // given

        String baseUrl = mockWebServer.url("/").toString();

        Server server = serverRepository.save(serverSteps.createServer(baseUrl));

        // when

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setBody("OK")
                .setResponseCode(200));

        healthAdminister.checkAndSaveRecord(server.getId());


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

        String baseUrl = mockWebServer.url("/").toString();

        Server server = serverRepository.save(serverSteps.createServer(baseUrl));

        // when

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        healthAdminister.checkAndSaveRecord(server.getId());


        // then
        HealthRecord healthRecord = healthRecordRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow();

        assertThat(healthRecord.getHealthStatus()).isEqualTo(HealthStatus.FAIL);
    }

}