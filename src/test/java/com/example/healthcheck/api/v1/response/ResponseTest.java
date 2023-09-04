package com.example.healthcheck.api.v1.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class ResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Response<Void> 출력 테스트")
    void Response_Void() throws Exception {

        final Response<Void> response = Response.success();
        final String result = objectMapper.writeValueAsString(response);
        final String expect = """
            {"resultCode":"SUCCESS"}""";

        assertThat(result).isEqualTo(expect);
    }

    @Test
    @DisplayName("Response<Pair> 출력 테스트")
    void Response_IncludeResult() throws Exception {
        final Response<Pair> response = Response.success(new Pair(2, 3));
        final String result = objectMapper.writeValueAsString(response);
        final String expect = """
            {"resultCode":"SUCCESS","result":{"x":2,"y":3}}""";

        assertThat(result).isEqualTo(expect);
    }

    static class Pair {
        int x, y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
