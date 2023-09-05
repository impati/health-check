package com.example.healthcheck.service.common;

public interface EntityFinder {

    <T> T findOrElseThrow(final Long id, final Class<T> clazz);
}
