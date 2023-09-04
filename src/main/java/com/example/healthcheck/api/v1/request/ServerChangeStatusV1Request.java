package com.example.healthcheck.api.v1.request;

import com.example.healthcheck.service.server.dto.ServerDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerChangeStatusV1Request {

	private String serverName;
	private Long serverId;

	@Builder
	public ServerChangeStatusV1Request(final String serverName, final Long serverId) {
		this.serverName = serverName;
		this.serverId = serverId;
	}

	public ServerDto convert() {
		return ServerDto.builder()
			.serverName(serverName)
			.serverId(serverId)
			.build();
	}
}
