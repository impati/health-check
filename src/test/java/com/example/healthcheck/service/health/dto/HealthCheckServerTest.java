package com.example.healthcheck.service.health.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;


class HealthCheckServerTest {

    @Test
    @DisplayName("checkTime 테스트")
    public void given_when_then() throws Exception{

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime after30Minute = now.plusMinutes(30);

        Assertions.assertThat(now.compareTo(after30Minute)).isLessThan(0);

        Duration between = Duration.between(now,after30Minute);
        Assertions.assertThat(between.getSeconds() / 60).isEqualTo(30);
    }
}