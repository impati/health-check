package com.example.healthcheck.service.health;

import static com.example.healthcheck.steps.ServerSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

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
	void health_check_mockWebServer_SUCCESS() {
		// given
		final String baseUrl = mockWebServer.url("/").toString();
		final Server server = createStubServerWithHost(baseUrl);

		// when
		mockWebServer.enqueue(new MockResponse()
			.setBody("ok")
			.setResponseCode(200));

		// then
		assertThatCode(() -> defaultHealthChecker.check(server)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("3번의 시도 중 마지막에 성공")
	void health_check_mockWebServer_SUCCESS_LAST() {
		// given
		final String baseUrl = mockWebServer.url("/").toString();
		final Server server = createStubServerWithHost(baseUrl);

		// when
		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(400));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(500));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(200));

		// then
		assertThatCode(() -> defaultHealthChecker.check(server)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("3번의 시도 모두 실패")
	void health_check_mockWebServer_FAIL() {
		// given
		final String baseUrl = mockWebServer.url("/").toString();
		final Server server = createStubServerWithHost(baseUrl);

		// when
		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(400));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(500));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(500));

		// then
		assertThatCode(() -> defaultHealthChecker.check(server)).isInstanceOf(HealthCheckException.class);
	}
}
