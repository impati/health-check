package com.example.healthcheck.service.server;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.ActiveTableManager;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static java.time.LocalDateTime.now;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerRegister {

    private final ServerRepository serverRepository;
    private final ActiveTableManager activeTableManager;

    public void register(String email , ServerRegistrationDto registrationDto){
        Server server = serverRepository.save(toServer(email, registrationDto));
        activeTableManager.insert(server, convertToLong(now()));
    }

    private Server toServer(String email,ServerRegistrationDto dto){
        return Server.builder()
                .serverName(dto.serverName())
                .email(email)
                .method(dto.method())
                .interval(dto.interval())
                .host(dto.host())
                .path(dto.path())
                .active(dto.active())
                .params(dto.queryParams())
                .build();
    }
}
