package com.example.healthcheck.util;

import java.time.LocalDateTime;

import static java.time.Duration.between;

public abstract class TimeConverter {

    private static final LocalDateTime START = LocalDateTime.of(2023,5,1,0,0);
    private static final long UNIT = 60;

    private TimeConverter(){
    }

    public static LocalDateTime getStartDate(){
        return START;
    }

    public static long getStartMinutes(){
        return convertToLong(START);
    }

    public static long convertToLong(LocalDateTime dateTime) {
        return between(START,dateTime).getSeconds() / UNIT;
    }

    public static LocalDateTime convertToLocalDateTime(long dateTime){
        return START.plusMinutes(dateTime);
    }

}
