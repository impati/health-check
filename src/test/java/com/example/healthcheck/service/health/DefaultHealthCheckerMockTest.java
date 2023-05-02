package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.example.healthcheck.steps.ServerSteps.createStubServerWithHost;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DefaultHealthCheckerMockTest {

    @Autowired
    private HealthChecker defaultHealthChecker;

    @Autowired
    private RestTemplate restTemplate;

    private MockWebServer mockWebServer;



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
    @DisplayName("health check mockWebServer 성공 테스트")
    public void health_check_mockWebServer_SUCCESS() throws Exception{
        // given
        String baseUrl = mockWebServer.url("/").toString();

        Server server = createStubServerWithHost(baseUrl);

        // when
        mockWebServer.enqueue(new MockResponse()
                .setBody("ok")
                .setResponseCode(200));

        // then
        Assertions.assertThatCode(() -> defaultHealthChecker.check(server))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("3번의 시도 중 마지막에 성공")
    public void health_check_mockWebServer_SUCCESS_LAST() throws Exception{
        // given
        String baseUrl = mockWebServer.url("/").toString();

        Server server = createStubServerWithHost(baseUrl);

        // when
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200));

        // then
        Assertions.assertThatCode(() -> defaultHealthChecker.check(server))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("3번의 시도 모두 실패")
    public void health_check_mockWebServer_FAIL() throws Exception{
        // given
        String baseUrl = mockWebServer.url("/").toString();

        Server server = createStubServerWithHost(baseUrl);

        // when
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        // then
        Assertions.assertThatCode(() -> defaultHealthChecker.check(server))
                .isInstanceOf(HealthCheckException.class);
    }


}