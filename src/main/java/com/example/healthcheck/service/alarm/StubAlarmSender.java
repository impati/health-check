package com.example.healthcheck.service.alarm;

import com.example.healthcheck.entity.server.Server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StubAlarmSender implements AlarmSender {

    @Override
    public void send(final Server server, final String target) {
        log.info("[{}] 서버 실패", server.getServerName());
    }
}
