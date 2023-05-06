package com.example.healthcheck.service.health;

import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class HealthCheckTriggerTest {

    @MockBean
    private HealthCheckManager healthCheckManager;

    @Autowired
    private HealthCheckAsync healthCheckAsync;

    @Autowired
    private HealthCheckInitializer healthCheckInitializer;

    @Autowired
    private ServerRepository serverRepository;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        serverSteps = new ServerSteps(serverRepository);
    }

    @Test
    @DisplayName("")
    public void given_when_then() throws Exception{
        // given
        HealthCheckTrigger trigger = new HealthCheckTrigger(healthCheckManager,healthCheckAsync,healthCheckInitializer);

        // when
        trigger.process();

        // then

    }




}