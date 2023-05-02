package com.example.healthcheck.service.alarm;

import com.example.healthcheck.entity.server.Server;

public interface AlarmSender {
    void send(Server server, String target);
}
