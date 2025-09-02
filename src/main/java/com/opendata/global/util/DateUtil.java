package com.opendata.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTERWITHOUTSEC = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        return dateTime.withMinute(0).withSecond(0).withNano(0);
    }

    public static String getCurrentRoundedFormattedDateTime() {
        return roundToNearestHour(LocalDateTime.now()).format(FORMATTERWITHOUTSEC);
    }

    public static LocalDateTime parseTime(String time){
        String normalized = time.replace("T", " ");
        if (normalized.length() == 16) { // yyyy-MM-dd HH:mm
            normalized += ":00";
        }
        return LocalDateTime.parse(normalized, FORMATTER);
    }
}