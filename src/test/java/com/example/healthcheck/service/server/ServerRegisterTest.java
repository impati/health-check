package com.example.healthcheck.service.server;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.entity.server.QueryParam;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;

@SpringBootTest
@Transactional
class ServerRegisterTest {

	@Autowired
	private ServerRegister serverRegister;

	@Autowired
	private ServerRepository serverRepository;

	@AfterEach
	void tearDown() {
		serverRepository.deleteAll();
	}

	@Test
	@DisplayName("헬스체크할 서버 정보를 등록")
	void serverRegister_registerTest() {
		// given
		final String serverName = "서비스 허브";
		final String host = "https://service-hub.org";
		final String path = "/service/search";
		final Integer interval = 30;
		final String email = "email";
		final ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto.builder()
			.serverName(serverName)
			.active(true)
			.host(host)
			.path(path)
			.interval(interval)
			.method(EndPointHttpMethod.GET)
			.queryParams(new LinkedMultiValueMap<>())
			.build();

		// when
		serverRegister.register(email, serverRegistrationDto);

		// then
		assertThat(serverRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("헬스체크할 서버 정보를 등록 - queryParam 포함")
	void serverRegister_registerTestWithQueryParam() {
		// given
		final String serverName = "서비스 허브";
		final String host = "https://impati-customer.com";
		final Integer interval = 30;
		final String email = "email";
		final MultiValueMap<String, String> param = createParam("clientId", List.of("123"));
		final ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto.builder()
			.serverName(serverName)
			.active(true)
			.host(host)
			.interval(interval)
			.method(EndPointHttpMethod.GET)
			.queryParams(param)
			.build();

		// when
		serverRegister.register(email, serverRegistrationDto);

		// then
		final Server server = serverRepository.findAll().stream().findFirst().orElseThrow(IllegalStateException::new);
		assertThat(server.getServerName()).isEqualTo(serverName);
		assertThat(server.getHost()).isEqualTo(host);
		assertThat(server.getEmail()).isEqualTo(email);
		assertThat(server.getInterval()).isEqualTo(interval);
		assertThat(server.getMethod()).isEqualTo(EndPointHttpMethod.GET);
		assertThat(server.getQueryParams()).hasSize(1);
	}

	@Test
	@DisplayName("헬스체크할 서버 정보를 등록 - queryParam 포함")
	@Transactional
	void serverRegister_registerTestWithQueryParamTwo() {
		// given
		final String serverName = "서비스 허브";
		final String host = "https://impati-customer.com";
		final Integer interval = 30;
		final String email = "email";
		final MultiValueMap<String, String> param = createParam("clientId", List.of("123", "456", "aaa"));
		final ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto.builder()
			.serverName(serverName)
			.active(true)
			.host(host)
			.interval(interval)
			.method(EndPointHttpMethod.GET)
			.queryParams(param)
			.build();

		// when
		serverRegister.register(email, serverRegistrationDto);

		// then
		final Server server = serverRepository.findAll().stream().findFirst().orElseThrow(IllegalStateException::new);
		assertThat(server.getServerName()).isEqualTo(serverName);
		assertThat(server.getHost()).isEqualTo(host);
		assertThat(server.getEmail()).isEqualTo(email);
		assertThat(server.getInterval()).isEqualTo(interval);
		assertThat(server.getMethod()).isEqualTo(EndPointHttpMethod.GET);
		assertThat(server.getQueryParams()).hasSize(3);
		assertThat(server.getQueryParams().stream().map(QueryParam::getValue)).containsAnyOf(
			"123", "456", "aaa"
		);
		assertThat(server.getQueryParams().stream().map(QueryParam::getKey)).containsAnyOf(
			"clientId"
		);
	}

	private MultiValueMap<String, String> createParam(final String key, final List<String> values) {
		final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		for (var value : values) {
			param.add(key, value);
		}

		return param;
	}
}
