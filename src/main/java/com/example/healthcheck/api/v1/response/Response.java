package com.example.healthcheck.api.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private final static String SUCCESS = "SUCCESS";
    private final String resultCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static Response<Void> error(String code) {
        return new Response<>(code, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>(SUCCESS, result);
    }

    public static <T> Response<T> success() {
        return new Response<T>(SUCCESS, null);
    }

}
