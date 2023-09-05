package com.example.healthcheck.entity.server;

import static com.example.healthcheck.steps.ServerSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class ServerTest {

	@Test
	@DisplayName("URL 만들기")
	void UriComponentsBuilderTest() {
		final List<QueryParam> queryParams = List.of(
			createQueryParam("key", "value"),
			createQueryParam("key", "value2")
		);

		final Server server = createStubServer("https://service-hub.org", "/service/search", toPrams(queryParams));

		assertThat(server.getUrl()).isEqualTo("https://service-hub.org/service/search?key=value&key=value2");
	}

	private MultiValueMap<String, String> toPrams(final List<QueryParam> queryParams) {
		final MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		for (var element : queryParams) {
			multiValueMap.add(element.getKey(), element.getValue());
		}

		return multiValueMap;
	}

	private QueryParam createQueryParam(final String key, final String value) {
		return QueryParam.builder()
			.key(key)
			.value(value)
			.build();
	}
}
