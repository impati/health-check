package com.example.healthcheck.service.health;

import com.example.healthcheck.entity.server.Server;

public interface HealthCheckSuccessManager {

    void process(final Server server);
}
