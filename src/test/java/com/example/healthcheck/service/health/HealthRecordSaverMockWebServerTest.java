package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.health.HealthStatus;
import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
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
class HealthRecordSaverMockWebServerTest {

    @Autowired
    private HealthRecordSaver healthRecordSaver;

    private MockWebServer mockWebServer;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @BeforeEach
    void setup() throws IOException {
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

        Server server = serverRepository.save(stubServer(baseUrl));

        // when

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setBody("OK")
                .setResponseCode(200));

        healthRecordSaver.saveRecord(server.getId());


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

        Server server = serverRepository.save(stubServer(baseUrl));

        // when

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        healthRecordSaver.saveRecord(server.getId());


        // then
        HealthRecord healthRecord = healthRecordRepository
                .findAll()
                .stream()
                .findAny()
                .orElseThrow();

        assertThat(healthRecord.getHealthStatus()).isEqualTo(HealthStatus.FAIL);
    }

    private Server stubServer(String host){
        return Server.builder()
                .host(host)
                .customerId(1L)
                .active(true)
                .interval(30)
                .method(EndPointHttpMethod.GET)
                .build();
    }
}