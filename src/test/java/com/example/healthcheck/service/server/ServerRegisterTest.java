package com.example.healthcheck.service.server;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.entity.server.QueryParam;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.server.dto.ServerRegistrationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ServerRegister.class, JpaConfig.class})
class ServerRegisterTest {

    @Autowired
    private ServerRegister serverRegister;
    @Autowired
    private ServerRepository serverRepository;
    @Test
    @DisplayName("헬스체크할 서버 정보를 등록")
    public void serverRegister_registerTest() throws Exception{
        // given
        String serverName = "서비스 허브";
        String host = "https://service-hub.org";
        String path = "/service/search";
        Integer interval = 30;
        Long customerId = 1L;
        ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto.builder()
                .serverName(serverName)
                .active(true)
                .host(host)
                .path(path)
                .interval(interval)
                .method(EndPointHttpMethod.GET)
                .queryParams(new LinkedMultiValueMap<>())
                .build();

        // when
        serverRegister.register(customerId,serverRegistrationDto);

        // then
        assertThat(serverRepository.findAll().size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("헬스체크할 서버 정보를 등록 - queryParam 포함")
    public void serverRegister_registerTestWithQueryParam() throws Exception{
        // given
        String serverName = "서비스 허브";
        String host = "https://impati-customer.com";
        Integer interval = 30;
        Long customerId = 1L;
        MultiValueMap<String, String> param = createParam("clientId", List.of("123"));
        ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto.builder()
                .serverName(serverName)
                .active(true)
                .host(host)
                .interval(interval)
                .method(EndPointHttpMethod.GET)
                .queryParams(param)
                .build();

        // when
        serverRegister.register(customerId,serverRegistrationDto);

        // then
        Server server = serverRepository.findAll().stream().findFirst().orElseThrow(IllegalStateException::new);
        assertThat(server.getServerName()).isEqualTo(serverName);
        assertThat(server.getHost()).isEqualTo(host);
        assertThat(server.getCustomerId()).isEqualTo(customerId);
        assertThat(server.getInterval()).isEqualTo(interval);
        assertThat(server.getMethod()).isEqualTo(EndPointHttpMethod.GET);
        assertThat(server.getQueryParams().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("헬스체크할 서버 정보를 등록 - queryParam 포함")
    public void serverRegister_registerTestWithQueryParamTwo() throws Exception{
        // given
        String serverName = "서비스 허브";
        String host = "https://impati-customer.com";
        Integer interval = 30;
        Long customerId = 1L;
        MultiValueMap<String, String> param = createParam("clientId", List.of("123","456","aaa"));
        ServerRegistrationDto serverRegistrationDto = ServerRegistrationDto.builder()
                .serverName(serverName)
                .active(true)
                .host(host)
                .interval(interval)
                .method(EndPointHttpMethod.GET)
                .queryParams(param)
                .build();

        // when
        serverRegister.register(customerId,serverRegistrationDto);

        // then
        Server server = serverRepository.findAll().stream().findFirst().orElseThrow(IllegalStateException::new);
        assertThat(server.getServerName()).isEqualTo(serverName);
        assertThat(server.getHost()).isEqualTo(host);
        assertThat(server.getCustomerId()).isEqualTo(customerId);
        assertThat(server.getInterval()).isEqualTo(interval);
        assertThat(server.getMethod()).isEqualTo(EndPointHttpMethod.GET);
        assertThat(server.getQueryParams().size()).isEqualTo(3);

        assertThat(server.getQueryParams().stream().map(QueryParam::getValue)).containsAnyOf(
                "123","456","aaa"
        );
        assertThat(server.getQueryParams().stream().map(QueryParam::getKey)).containsAnyOf(
                "clientId"
        );
    }


    private MultiValueMap<String,String> createParam(String key,List<String> values){
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        for(var value : values) param.add(key,value);
        return param;
    }


}