package io.github.hurelhuyag.jtecontext;

import gg.jte.Content;

import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

@SuppressWarnings("unused")
public class Context {

    private final ZoneId zone;
    private final Locale locale;
    private final MessageBundle messageBundle;

    public Context(ZoneId zone, Locale locale, MessageBundle messageBundle) {
        this.zone = zone;
        this.locale = locale;
        this.messageBundle = messageBundle;
    }

    public String formatMessage(String key) {
        var format = messageBundle.lookup(key, locale);
        if (format != null) {
            return format.apply();
        } else {
            return null;
        }
    }

    public Content formatMessage(String key, Object param1) {
        var format = messageBundle.lookup(key, locale);
        if (format != null) {
            return format.apply(param1);
        } else {
            return null;
        }
    }

    public Content formatMessage(String key, Object param1, Object param2) {
        var format = messageBundle.lookup(key, locale);
        if (format != null) {
            return format.apply(param1, param2);
        } else {
            return null;
        }
    }

    public Content formatMessage(String key, Object param1, Object param2, Object param3) {
        var format = messageBundle.lookup(key, locale);
        if (format != null) {
            return format.apply(param1, param2, param3);
        } else {
            return null;
        }
    }

    public Content formatMessage(String key, Object ... params) {
        var format = messageBundle.lookup(key, locale);
        if (format != null) {
            return format.apply(params);
        } else {
            return null;
        }
    }

    public Content formatDate(TemporalAccessor date, String pattern) {
        if (date == null) {
            return null;
        }
        var formatter = DateTimeFormats.get(pattern, zone);
        return output -> formatter.formatTo(date, output.getWriter());
    }
}
