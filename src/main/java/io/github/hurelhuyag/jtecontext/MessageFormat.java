package io.github.hurelhuyag.jtecontext;

import gg.jte.Content;
import gg.jte.TemplateOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class MessageFormat {

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{(\\d+)}");

    private final List<String> fragments;

    public MessageFormat(String value) {
        var fragments = new ArrayList<String>();
        var matcher = PARAM_PATTERN.matcher(value);
        if (matcher.find()) {
            int startIndex = 0;
            do {
                fragments.add(value.substring(startIndex, matcher.start()));
                startIndex = matcher.end();
            } while (matcher.find());
            fragments.add(value.substring(startIndex));
        } else {
            fragments.add(value);
        }
        fragments.trimToSize();
        this.fragments = Collections.unmodifiableList(fragments);
    }

    public Content apply(Object param1) {
        return output -> {
            for (int i=0; i<fragments.size(); i++) {
                output.writeContent(fragments.get(i));
                if (i == 0) {
                    writeParam(output, param1);
                }
            }
        };
    }

    public Content apply(Object param1, Object param2) {
        return output -> {
            for (int i=0; i<fragments.size(); i++) {
                output.writeContent(fragments.get(i));
                if (i == 0) {
                    writeParam(output, param1);
                } else if (i == 1) {
                    writeParam(output, param2);
                }
            }
        };
    }

    public Content apply(Object[] params) {
        return output -> {
            for (int i=0; i<fragments.size(); i++) {
                output.writeContent(fragments.get(i));
                if (params.length > i) {
                    writeParam(output, params[i]);
                }
            }
        };
    }

    private void writeParam(TemplateOutput output, Object param) {
        if (param instanceof String) {
            output.writeUserContent((String) param);
        } else if (param instanceof Content) {
            output.writeUserContent((Content) param);
        } else if (param instanceof Enum) {
            output.writeUserContent((Enum<?>) param);
        } else if (param instanceof Boolean) {
            output.writeUserContent((boolean) param);
        } else if (param instanceof Byte) {
            output.writeUserContent((byte) param);
        } else if (param instanceof Short) {
            output.writeUserContent((short) param);
        } else if (param instanceof Integer) {
            output.writeUserContent((int) param);
        } else if (param instanceof Long) {
            output.writeUserContent((long) param);
        } else if (param instanceof Float) {
            output.writeUserContent((float) param);
        } else if (param instanceof Double) {
            output.writeUserContent((double) param);
        } else if (param instanceof Character) {
            output.writeUserContent((char) param);
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(fragments.get(0));
        for (int i=1; i<fragments.size(); i++) {
            sb.append("{").append(i-1).append("}").append(fragments.get(i));
        }
        return sb.toString();
    }
}
