package com.example.healthcheck.service.server;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.repository.ServerRepository;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ServerRegister.class, JpaConfig.class})
class ServerRegisterTest {

    @Autowired
    private ServerRegister serverRegister;
    @Autowired
    private ServerRepository serverRepository;

    @Test
    @DisplayName("헬스체크할 서버 정보를 등록")
    public void serverRegister_registerTest() throws Exception{
        // given
        String host = "https://service-hub.org";
        String path = "/service/search";
        Integer interval = 30;
        Long customerId = 1L;
        ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto
                .builder()
                .active(true)
                .host(host)
                .path(path)
                .interval(interval)
                .method(EndPointHttpMethod.GET)
                .build();

        // when
        serverRegister.register(customerId,serverRegistrationDto);

        // then
        assertThat(serverRepository.findAll().size())
                .isEqualTo(1);
    }
}