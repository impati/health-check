package com.example.healthcheck.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.healthcheck.util.TimeConverter.convertLocalDateTimeFrom;
import static com.example.healthcheck.util.TimeConverter.convertToLong;
import static org.assertj.core.api.Assertions.assertThat;


class TimeConverterTest {

    @Test
    @DisplayName("LocalDateTime 을 long 으로 변환")
    public void LocalDateTimeToLong() throws Exception{
        // given
        LocalDateTime current = LocalDateTime.of(2023, 5, 2, 0, 0);
        // when
        long result = convertToLong(current);
        // then
        assertThat(result).isEqualTo(60 * 24);
    }

    @Test
    @DisplayName("long 을 LocalDateTime 으로 변환")
    public void longToLocalDateTime() throws Exception{
        // given
        long current = 60 * 24;
        // when
        LocalDateTime localDateTime = convertLocalDateTimeFrom(current);

        // then
        assertThat(localDateTime).isEqualTo(LocalDateTime.of(2023, 5, 2, 0, 0));
    }
}