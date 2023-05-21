package com.example.healthcheck.service.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.ActiveServer;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.ActiveServerRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, ActiveTableManager.class})
class ActiveTableManagerTest {

    @Autowired
    private ActiveTableManager activeTableManager;

    @Autowired
    private ActiveServerRepository activeServerRepository;

    @Autowired
    private ServerRepository serverRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("ActiveServer 삽입 테스트")
    public void insertTest() throws Exception{
        // given
        Server activeServer = serverSteps.create("[테스트 서버 1]", true);
        Server inActiveServer = serverSteps.create("[테스트 서버 2]",false);

        // when
        activeTableManager.insert(activeServer, convertToLong(now()));
        activeTableManager.insert(inActiveServer,convertToLong(now()));

        // then
        List<ActiveServer> response = activeServerRepository.findAll();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.stream().findFirst().get().getServer())
                .isEqualTo(activeServer);
    }

    @Test
    @DisplayName("targetTime 업데이트 테스트")
    public void updateTargetTimeTest() throws Exception{
        // given
        Server activeServer = serverSteps.create("[테스트 서버 1]", true);
        long currentTime = convertToLong(now());
        activeTableManager.insert(activeServer,currentTime);

        // when
        activeTableManager.updateTargetTime(activeServer);

        // then
        long targetTime = currentTime + activeServer.getInterval() * 2;
        List<ActiveServer> response = activeTableManager.findTargetServer(targetTime);

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getServer()).isEqualTo(activeServer);
    }


    @Test
    @DisplayName("ActiveServer Table 에서 제거")
    public void deleteTest() throws Exception{
        // given
        Server activeServer = serverSteps.create("[테스트 서버 1]", true);
        long currentTime = convertToLong(now());
        activeTableManager.insert(activeServer,currentTime);

        // when
        activeTableManager.delete(activeServer);

        // then
        List<ActiveServer> response = activeServerRepository.findAll();
        assertThat(response.size()).isEqualTo(0);
    }


}