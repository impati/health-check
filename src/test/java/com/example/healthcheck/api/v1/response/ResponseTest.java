package com.example.healthcheck.api.v1.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Response<Void> 출력 테스트")
    public void Response_Void() throws Exception{

        Response<Void> response = Response.success();
        String result = objectMapper.writeValueAsString(response);
        String expect = """
                {"resultCode":"SUCCESS"}""";
        Assertions.assertThat(result).isEqualTo(expect);
    }

    @Test
    @DisplayName("Response<Pair> 출력 테스트")
    public void Response_IncludeResult() throws Exception{
        Response<Pair> response = Response.success(new Pair(2,3));
        String result = objectMapper.writeValueAsString(response);
        String expect = """
                {"resultCode":"SUCCESS","result":{"x":2,"y":3}}""";
        Assertions.assertThat(result).isEqualTo(expect);
    }


    static class Pair{
        int x,y;
        Pair(int x,int y){
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