package com.example.healthcheck.service.server;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.common.DefaultEntityFinder;
import com.example.healthcheck.service.server.dto.ServerDto;
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
@Import({ServerStatusManager.class, JpaConfig.class, DefaultEntityFinder.class})
class ServerStatusManagerTest {

    @Autowired
    private ServerStatusManager serverStatusManager;

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
        serverStatusManager.deactivate(server);

        // then
        Assertions.assertThat(server.isActive())
                .isFalse();
    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 외부 ,정상")
    public void deactivateTestExternal() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        ServerDto serverDto = ServerDto.builder()
                .serverName(server.getServerName())
                .serverId(server.getId())
                .build();
        // when
        serverStatusManager.deactivate(serverDto,server.getEmail());

        // then
        Assertions.assertThat(server.isActive())
                .isFalse();
    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 외부 ,서버 이름 다른 경우")
    public void deactivateTestExternalFailDifferent() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        ServerDto serverDto = ServerDto.builder()
                .serverName("다른 서버")
                .serverId(server.getId())
                .build();

        // expected
        HealthCheckException healthCheckException = assertThrows(HealthCheckException.class,
                () -> serverStatusManager.deactivate(serverDto,server.getEmail()));

        Assertions.assertThat(healthCheckException.getMessage())
                .isEqualTo("입력한 서버 정보가 올바르지 않습니다.");

    }

    @Test
    @DisplayName("서버 비활성화 테스트 - 외부 서버 ,사용자가 아닌 경우")
    public void deactivateTestExternalFailOtherCustomer() throws Exception{
        // given
        Server server = serverSteps.createDefault();
        ServerDto serverDto = ServerDto.builder()
                .serverName(server.getServerName())
                .serverId(server.getId())
                .build();
        // expected

        HealthCheckException healthCheckException = assertThrows(HealthCheckException.class,
                () -> serverStatusManager.deactivate(serverDto,server.getEmail() + "noisy"));

        Assertions.assertThat(healthCheckException.getMessage())
                .isEqualTo("다른 사용자 영역에 침범했습니다.");
    }

    @Test
    @DisplayName("서버 활성화 테스트")
    public void deactivateTest() throws Exception{
        // given
        Server server = serverSteps.create("테스트 서버",false);
        ServerDto serverDto = ServerDto.builder()
                .serverName(server.getServerName())
                .serverId(server.getId())
                .build();

        // when
        serverStatusManager.activate(serverDto,server.getEmail());

        // then
        Assertions.assertThat(server.isActive())
                .isTrue();

    }


}