package com.example.healthcheck.service.common;

public interface EntityFinder {
    <T> T findOrElseThrow(Long id , Class<T> clazz);
}
