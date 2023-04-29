package com.example.healthcheck.service.common;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEntityFinder implements EntityFinder{
    private final ServerRepository serverRepository;
    private final HealthRecordRepository healthRecordRepository;

    @Override
    public <T> T findOrElseThrow(Long id, Class<T> clazz) {
        return findWithCheckClassType(id,clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T findWithCheckClassType(Long id, Class<T> clazz){
        if(clazz.isAssignableFrom(Server.class)) return (T)findOrElseThrow(id);
        throw new IllegalStateException("지원하지 않는 클래스 타입입니다.");
    }

    private Server findOrElseThrow(Long id){
        return serverRepository.findById(id).orElseThrow(()-> new HealthCheckException(ErrorCode.NOT_FOUND_SERVER));
    }

}
