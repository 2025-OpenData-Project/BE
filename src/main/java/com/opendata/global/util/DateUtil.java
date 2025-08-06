package com.opendata.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentFormattedDateTime() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String formatLocalDateTime(String isoDateTime) {
        LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime floorToHour(LocalDateTime dateTime) {
        return dateTime.withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime roundToNearestHour(LocalDateTime dateTime) {
        if (dateTime.getMinute() > 30) {
            return dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        } else {
            return dateTime.withMinute(0).withSecond(0).withNano(0);
        }
    }

    public static String getCurrentRoundedFormattedDateTime() {
        return roundToNearestHour(LocalDateTime.now()).format(FORMATTER);
    }

    public static LocalDateTime parseTime(String time){
        return LocalDateTime.parse(time, FORMATTER);
    }
}