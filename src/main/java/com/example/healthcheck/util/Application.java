package com.example.healthcheck.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Data
@ConfigurationProperties("health-check")
public class Application {

    private final static String CHECK_ENDPOINT = "/api/v1/check";
    private String schema;
    private String host;
    private String port;

    public String getUrl(){
        return UriComponentsBuilder.newInstance()
                .scheme(schema)
                .host(host)
                .port(port)
                .path(CHECK_ENDPOINT)
                .encode()
                .build()
                .toString();
    }

}
