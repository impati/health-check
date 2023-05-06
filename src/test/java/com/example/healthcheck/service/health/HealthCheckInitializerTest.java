package com.example.healthcheck.service.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({HealthCheckInitializer.class, JpaConfig.class})
class HealthCheckInitializerTest {

    @Autowired
    private ServerRepository serverRepository;

    @MockBean
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private HealthCheckInitializer healthCheckInitializer;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("활성화 된 서버를 먼저 체크되어야할 순으로 큐에 넣는다.")
    public void getActiveServerTest() throws Exception{
        //given
        Server firstEnableServer = serverSteps.create("활성화 서버1", true,3);
        Server secondEnableServer = serverSteps.create("활성화 서버2", true,4);
        Server thirdEnableServer = serverSteps.create("활성화 서버3", true,2);
        Server firstDisenableServer = serverSteps.create("비활성화 서버1", false);
        Server secondDisableServer = serverSteps.create("비활성화 서버2", false);

        //when
        PriorityQueue<HealthCheckServer> queue = healthCheckInitializer.getActiveServer();


        //then
        assertThat(queue.size()).isEqualTo(3);
        assertThat(queue.poll().getServerId()).isEqualTo(thirdEnableServer.getId());
        assertThat(queue.poll().getServerId()).isEqualTo(firstEnableServer.getId());
        assertThat(queue.poll().getServerId()).isEqualTo(secondEnableServer.getId());
    }

}