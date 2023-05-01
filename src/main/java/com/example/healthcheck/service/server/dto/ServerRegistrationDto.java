package com.example.healthcheck.service.server.dto;

import com.example.healthcheck.entity.server.EndPointHttpMethod;
import lombok.Builder;
import org.springframework.util.MultiValueMap;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

public record ServerRegistrationDto (
        String serverName,
        String host,
        String path,
        EndPointHttpMethod method,
        MultiValueMap<String,String> queryParams,
        Integer interval,
        boolean active
){
    @Builder
    public ServerRegistrationDto {
        hasText(serverName,"서버 이름은 필수입니다.");
        hasText(host,"서버 호스트는 필수입니다.");
        notNull(method,"앤드포인트 메서드는 필수입니다.");
        notNull(queryParams,"쿼리 파라미터는 null 이어서는 안됩니다.");
        notNull(interval,"서버 호스트는 필수입니다.");
    }
}
