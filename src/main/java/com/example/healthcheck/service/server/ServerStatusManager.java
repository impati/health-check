package com.example.healthcheck.service.server;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.service.common.EntityFinder;
import com.example.healthcheck.service.server.dto.ServerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerStatusManager {

    private final EntityFinder entityFinder;

    @Transactional
    public void deactivate(Server server){
        server.deactivate();
    }

    @Transactional
    public void deactivate(ServerDto serverDto,String email){
        Server server = getServer(serverDto.serverId(),serverDto.serverName(),email);
        server.deactivate();
    }

    @Transactional
    public void activate(ServerDto serverDto,String email){
        Server server = getServer(serverDto.serverId(),serverDto.serverName(),email);
        server.activate();
    }

    private Server getServer(Long serverId ,String serverName, String email){
        Server server = entityFinder.findOrElseThrow(serverId, Server.class);
        serverValidate(server,serverName,email);
        return server;
    }

    private void serverValidate(Server server,String serverName,String email){
        if(!isSameServer(server.getServerName(),serverName))throw new HealthCheckException(ErrorCode.SERVER_MISMATCH);
        if(!isOwner(server,email)) throw new HealthCheckException(ErrorCode.INVALID_ACCESS);
    }

    private boolean isSameServer(String realServerName,String requestServerName){
        return realServerName.equals(requestServerName);
    }

    private boolean isOwner(Server server,String email){
        return server.getEmail().equals(email);
    }

}
