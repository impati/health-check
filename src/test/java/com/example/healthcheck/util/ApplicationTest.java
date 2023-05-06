package com.example.healthcheck.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTest {

    @Autowired
    Application application;

    @Test
    @DisplayName("")
    public void projectUtilTest() throws Exception{

        assertThat(application.getUrl())
                .isEqualTo("http://localhost:8080/api/v1/check");

    }
}