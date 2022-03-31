package io.github.hurelhuyag.jtecontext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageBundle {

    private static final Pattern NAME_PATTERN = Pattern.compile(".+_(?<l>[a-z]{2,4}([_-][A-Za-z]{4})?([_-]([A-Z]{2}|[0-9]{3}))?)\\.properties");

    private final ClassLoader classLoader;
    private final String[] baseNames;

    private Map<String, MessageFormat> defaultMessages;
    private Map<String, List<Variant>> messages;

    public MessageBundle(ClassLoader classLoader, String... baseNames) throws IOException {
        Objects.requireNonNull(classLoader);
        Objects.requireNonNull(baseNames);
        this.classLoader = classLoader;
        this.baseNames = baseNames;
        reload();
    }

    public void reload() throws IOException {
        Map<String, MessageFormat> defaultMessages = null;
        Map<String, List<Variant>> messages = new HashMap<>();

        for (var baseName : baseNames) {
            var resources = resolveResources(classLoader, baseName);
            for (var res : resources) {
                var properties = new Properties();
                try (var s = new InputStreamReader(res.openStream(), StandardCharsets.UTF_8)) {
                    properties.load(s);
                }
                var filename = res.getFile();
                var locale = resolveLocale(filename);
                if (locale == null) {
                    defaultMessages = properties.entrySet().stream()
                            .collect(Collectors.toMap(o -> (String) o.getKey(), o -> new MessageFormat((String) o.getValue())));
                } else {
                    var lang = locale.getLanguage();
                    var country = locale.getCountry();
                    var list = messages.computeIfAbsent(lang, s1 -> new ArrayList<>());
                    list.add(new Variant(
                                    country,
                                    properties.entrySet().stream()
                                            .collect(Collectors.toMap(o -> (String) o.getKey(), o -> new MessageFormat((String) o.getValue())))
                            )
                    );
                }
            }
        }
        this.defaultMessages = defaultMessages;
        this.messages = messages;
    }

    private List<URL> resolveResources(ClassLoader classLoader, String baseName) throws IOException {
        var result = new ArrayList<URL>();
        int i = baseName.lastIndexOf('/');
        var name = baseName.substring(i + 1);
        var dir = i > -1 ? baseName.substring(0, i + 1) : "";
        var dirResource = classLoader.getResource(dir);
        if (dirResource == null) {
            throw new RuntimeException("can't find resources with basename : " + baseName);
        }
        try (var s = new BufferedReader(new InputStreamReader(dirResource.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = s.readLine()) != null) {
                if (line.startsWith(name)) {
                    result.add(classLoader.getResource(dir + line));
                }
            }
        }
        return result;
    }

    private Locale resolveLocale(String filename) {
        var m = NAME_PATTERN.matcher(filename);
        if (m.matches()) {
            var l = m.group("l");
            var lr = l.replace('_', '-');
            return Locale.forLanguageTag(lr);
        } else {
            return null;
        }
    }

    private MessageFormat lookupDefaultIfMissing(String key, MessageFormat foundMessageFormat) {
        return foundMessageFormat == null && defaultMessages != null
                ? defaultMessages.get(key)
                : foundMessageFormat;
    }

    public MessageFormat lookup(String key, Locale locale) {
        var lang = locale.getLanguage();
        var country = locale.getCountry();

        var variants = messages.get(lang);
        if (variants == null || variants.size() == 0) {
            return lookupDefaultIfMissing(key, null);
        }

        if (variants.size() == 1) {
            var variant = variants.get(0);
            if (country.isEmpty() || country.equals(variant.country)) {
                return lookupDefaultIfMissing(key, variant.lookup(key));
            } else {
                return lookupDefaultIfMissing(key, null);
            }
        }

        Variant genericVariant = null;
        MessageFormat result;
        boolean countryFound = false;
        for (var v : variants) {
            if (v.country.isEmpty()) {
                genericVariant = v;
                if (countryFound) {
                    break;
                }
                continue;
            }
            if (v.country.equals(country)){
                result = v.lookup(key);
                if (result != null) {
                    return result;
                } else
                    if (genericVariant == null){
                        countryFound = true;
                    } else {
                        break;
                    }
            }
        }
        if (genericVariant != null) {
            return lookupDefaultIfMissing(key, genericVariant.lookup(key));
        } else {
            return lookupDefaultIfMissing(key, null);
        }
    }

    private static class Variant {

        private final String country;
        private final Map<String, MessageFormat> messages;

        public Variant(String country, Map<String, MessageFormat> messages) {
            this.country = country;
            this.messages = messages;
        }

        public MessageFormat lookup(String key) {
            return messages.get(key);
        }
    }
}
