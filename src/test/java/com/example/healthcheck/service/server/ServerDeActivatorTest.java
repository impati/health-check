package com.example.healthcheck.service.server;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.common.DefaultEntityFinder;
import com.example.healthcheck.service.server.dto.ServerDisableDto;
import com.example.healthcheck.steps.ServerSteps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({ServerDeActivator.class, JpaConfig.class, DefaultEntityFinder.class})
class ServerDeActivatorTest {

    @Autowired
    private ServerDeActivator serverDeActivator;

    @Autowired
    private ServerRepository serverRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 내부")
    public void deactivateTestInner() throws Exception{
        // given
        Server server = serverSteps.createDefault();

        // when
        serverDeActivator.deactivate(server.getId());

        // then
        Assertions.assertThat(server.isActive())
                .isFalse();
    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 외부 ,정상")
    public void deactivateTestExternal() throws Exception{
        // given
        Server server = serverSteps.createDefault();

        // when
        serverDeActivator.deactivate(server.getId());

        // then
        Assertions.assertThat(server.isActive())
                .isFalse();
    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 외부 ,서버 이름 다른 경우")
    public void deactivateTestExternalFailDifferent() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        ServerDisableDto serverDisableDto = ServerDisableDto.builder()
                .serverName("다른 서버")
                .serverId(server.getId())
                .build();
        // expected

        HealthCheckException healthCheckException = assertThrows(HealthCheckException.class,
                () -> serverDeActivator.deactivate(server.getCustomerId(), serverDisableDto));

        Assertions.assertThat(healthCheckException.getMessage())
                .isEqualTo("입력한 서버 정보가 올바르지 않습니다.");

    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 외부 서버 ,사용자가 아닌 경우")
    public void deactivateTestExternalFailOtherCustomer() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        ServerDisableDto serverDisableDto = ServerDisableDto.builder()
                .serverName(server.getServerName())
                .serverId(server.getId())
                .build();
        // expected

        HealthCheckException healthCheckException = assertThrows(HealthCheckException.class,
                () -> serverDeActivator.deactivate(server.getCustomerId() + 1, serverDisableDto));

        Assertions.assertThat(healthCheckException.getMessage())
                .isEqualTo("다른 사용자 영역에 침범했습니다.");
    }


}