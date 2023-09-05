package com.example.healthcheck.util;

import static com.example.healthcheck.util.TimeConverter.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeConverterTest {

    @Test
    @DisplayName("LocalDateTime 을 long 으로 변환")
    void LocalDateTimeToLong() {
        // given
        final LocalDateTime current = LocalDateTime.of(2023, 5, 2, 0, 0);

        // when
        final long result = convertToLong(current);

        // then
        assertThat(result).isEqualTo(60 * 24);
    }

    @Test
    @DisplayName("long 을 LocalDateTime 으로 변환")
    void longToLocalDateTime() {
        // given
        final long current = 60 * 24;

        // when
        final LocalDateTime localDateTime = TimeConverter.convertToLocalDateTime(current);

        // then
        assertThat(localDateTime).isEqualTo(LocalDateTime.of(2023, 5, 2, 0, 0));
    }
}
