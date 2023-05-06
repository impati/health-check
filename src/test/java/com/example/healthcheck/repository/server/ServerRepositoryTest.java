package com.example.healthcheck.repository.server;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class ServerRepositoryTest {


    @Autowired
    private ServerRepository serverRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("활성화된 서버만 조회하기")
    public void findActiveServerTest() throws Exception{
        // given
        Server server = serverSteps.create("노말 서버",true);
        Server enableServer = serverSteps.create("활성화된 서버",true);
        Server disableServer = serverSteps.create("비활성화 서버",false);

        // when
        List<Server> response = serverRepository.findActiveServer();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response
                .stream()
                .map(Server::getServerName)
                .collect(toList()))
                .contains(server.getServerName() , enableServer.getServerName());
    }
}