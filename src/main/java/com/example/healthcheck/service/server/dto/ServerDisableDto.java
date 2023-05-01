package com.example.healthcheck.service.server.dto;

import lombok.Builder;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

public record ServerDisableDto(
        String serverName,
        Long serverId
){
    @Builder
    public ServerDisableDto {
        hasText(serverName,"서버 이름은 필수입니다.");
        notNull(serverId,"서버 아이디는 필수 입니다.");
    }
}
