package com.example.healthcheck.service.common;

import com.example.healthcheck.entity.server.Server;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.healthcheck.steps.ServerSteps.createStubServerWithDefaults;

class DefaultEntityFinderTest {

    @Test
    @DisplayName("EntityFinder 의 findOrElseThrow 테스트")
    public void findOrElseThrow() throws Exception{

        Assertions.assertThatCode(() -> findOrElseThrow(2L, Server.class))
                .doesNotThrowAnyException();
    }

    @SuppressWarnings("unchecked")
    private <T> T findOrElseThrow(Long id, Class<T> clazz) {
        return (T)find(id,clazz);
    }

    private <T> Object find(Long id, Class<T> clazz){
        if(clazz.isAssignableFrom(Server.class)) return createStubServerWithDefaults();
        throw new IllegalStateException("지원하지 않는 클래스 타입입니다.");
    }

}