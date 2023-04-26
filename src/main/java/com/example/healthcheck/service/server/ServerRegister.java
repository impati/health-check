package com.example.healthcheck.service.server;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.ServerRepository;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerRegister {

    private final ServerRepository serverRepository;

    public void register(Long customerId , ServerRegistrationDto registrationDto){
        serverRepository.save(toServer(customerId,registrationDto));
    }

    private Server toServer(Long customerId,ServerRegistrationDto dto){
        return Server.builder()
                .customerId(customerId)
                .method(dto.method())
                .interval(dto.interval())
                .host(dto.host())
                .path(dto.path())
                .active(dto.active())
                .build();
    }
}
