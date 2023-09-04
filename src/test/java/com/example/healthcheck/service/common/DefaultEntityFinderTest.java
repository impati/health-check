package com.example.healthcheck.service.common;

import static com.example.healthcheck.steps.ServerSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.healthcheck.entity.server.Server;

class DefaultEntityFinderTest {

	@Test
	@DisplayName("EntityFinder 의 findOrElseThrow 테스트")
	void findOrElseThrow() {
		assertThatCode(() -> findOrElseThrow(2L, Server.class))
			.doesNotThrowAnyException();
	}

	@SuppressWarnings("unchecked")
	private <T> T findOrElseThrow(final Long id, final Class<T> clazz) {
		return (T)find(id, clazz);
	}

	private <T> Object find(final Long id, final Class<T> clazz) {
		if (clazz.isAssignableFrom(Server.class)) {
			return createStubServerWithDefaults();
		}

		throw new IllegalStateException("지원하지 않는 클래스 타입입니다.");
	}
}
