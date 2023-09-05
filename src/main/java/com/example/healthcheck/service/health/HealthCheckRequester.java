package com.example.healthcheck.service.health;

public interface HealthCheckRequester {

    void check(final Long serverId);
}
