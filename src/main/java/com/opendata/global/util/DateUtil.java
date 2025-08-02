package com.opendata.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

    public static String getCurrentFormattedCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime floored = floorToHour(now);
        return floored.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}