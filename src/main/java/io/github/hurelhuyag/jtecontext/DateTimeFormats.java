package io.github.hurelhuyag.jtecontext;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DateTimeFormats {

    private final static Map<String, DateTimeFormatter> formatterCache = new ConcurrentHashMap<>();

    public static DateTimeFormatter get(String pattern, ZoneId zone) {
        return formatterCache.computeIfAbsent(pattern + "-" + zone,
                s -> DateTimeFormatter.ofPattern(pattern).withZone(zone));
    }
}
