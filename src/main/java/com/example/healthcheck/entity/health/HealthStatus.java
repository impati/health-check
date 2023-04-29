package com.example.healthcheck.entity.health;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HealthStatus {
    SUCCESS("success") , FAIL("fail");
    private final String text;
}
