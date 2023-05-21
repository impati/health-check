package com.example.healthcheck.repository.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.ActiveServer;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class ActiveServerRepositoryTest {

    @Autowired
    private ActiveServerRepository activeServerRepository;

    @Autowired
    private ServerRepository serverRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);

        Server testServer1 = serverSteps.create("[테스트 서버 1]", true);
        Server testServer2 = serverSteps.create("[테스트 서버 2]", true);
        Server testServer3 = serverSteps.create("[테스트 서버 3]", true);

        activeServerRepository.save(ActiveServer.builder()
                .server(testServer1)
                .targetTime(5555)
                .build()
        );

        activeServerRepository.save(ActiveServer.builder()
                .server(testServer2)
                .targetTime(5555)
                .build()
        );

        activeServerRepository.save(ActiveServer.builder()
                .server(testServer3)
                .targetTime(6666)
                .build()
        );
    }

    @Test
    @DisplayName("활성화된 서버 중 헬스 체크를 수행할 서버만을 거져온다.")
    public void findActiveServerByTargetTimeTest() throws Exception{
        // given
        long currentTime = 5555L;

        // when
        List<ActiveServer> servers = activeServerRepository.findActiveServerByTargetTime(currentTime);

        // then
        assertThat(servers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("활성화된 서버 정보를 서버 Entity 로 가져오기")
    public void findActiveServerByServerTest() throws Exception{
        // given
        Server server = serverRepository.findActiveServer()
                .stream()
                .findFirst()
                .get();

        // when
        Optional<ActiveServer> findServer = activeServerRepository.findActiveServerByServer(server);

        // then
        assertThat(findServer).isPresent();
        assertThat(findServer.get().getServer()).isEqualTo(server);
    }

}