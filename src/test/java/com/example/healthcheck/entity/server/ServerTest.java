package com.example.healthcheck.entity.server;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.example.healthcheck.steps.ServerSteps.createStubServer;

class ServerTest {

    @Test
    @DisplayName("URL 만들기")
    public void UriComponentsBuilderTest() throws Exception{
        List<QueryParam> queryParams = List.of(createQueryParam("key","value"),createQueryParam("key","value2"));

        Server server = createStubServer("https://service-hub.org","/service/search",toPrams(queryParams));

        Assertions.assertThat(server.getUrl())
                .isEqualTo("https://service-hub.org/service/search?key=value&key=value2");

    }

    private MultiValueMap<String,String> toPrams(List<QueryParam> queryParams){
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        for(var element : queryParams){
            multiValueMap.add(element.getKey(),element.getValue());
        }
        return multiValueMap;
    }

    private QueryParam createQueryParam(String key,String value) {
        return  QueryParam.builder()
                .key(key)
                .value(value)
                .build();
    }
}