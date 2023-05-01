package com.example.healthcheck.service.server;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.service.common.EntityFinder;
import com.example.healthcheck.service.server.dto.ServerDisableDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerDeActivator {

    private final EntityFinder entityFinder;

    @Transactional
    public void deactivate(Long serverId){
        Server server = entityFinder.findOrElseThrow(serverId, Server.class);
        server.deactivate();
    }

    @Transactional
    public void deactivate(Long customerId,ServerDisableDto serverDisableDto){
        Server server = entityFinder.findOrElseThrow(serverDisableDto.serverId(), Server.class);
        serverValidate(server,serverDisableDto.serverName(),customerId);
        server.deactivate();
    }

    private void serverValidate(Server server,String serverName,Long customerId){
        if(!isSameServer(server.getServerName(),serverName))throw new HealthCheckException(ErrorCode.SERVER_MISMATCH);
        if(!isOwner(server,customerId)) throw new HealthCheckException(ErrorCode.INVALID_ACCESS);
    }

    private boolean isSameServer(String realServerName,String requestServerName){
        return realServerName.equals(requestServerName);
    }

    private boolean isOwner(Server server,Long customerId){
        return server.getCustomerId().equals(customerId);
    }

}
