package com.example.healthcheck.api.v1.request;

import com.example.healthcheck.service.server.dto.ServerDisableDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerDisableV1Request {
    private String serverName;
    private Long serverId;

    @Builder
    public ServerDisableV1Request(String serverName, Long serverId) {
        this.serverName = serverName;
        this.serverId = serverId;
    }

    public ServerDisableDto convert(){
        return ServerDisableDto.builder()
                .serverName(serverName)
                .serverId(serverId)
                .build();
    }
}
