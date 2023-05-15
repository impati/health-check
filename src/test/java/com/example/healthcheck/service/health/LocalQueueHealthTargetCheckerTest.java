package com.example.healthcheck.service.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocalQueueHealthTargetCheckerTest {

    @Autowired
    private HealthTargetChecker healthTargetChecker;

    @Test
    @DisplayName("")
    public void given_when_then() throws Exception{
        System.out.println("&&&" + healthTargetChecker.getClass().getName());
    }
}