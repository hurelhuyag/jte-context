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
    private final int[] paramOrders;

    public MessageFormat(String value) {
        var fragments = new ArrayList<String>();
        var paramOrders = new ArrayList<Integer>();
        var matcher = PARAM_PATTERN.matcher(value);
        if (matcher.find()) {
            int startIndex = 0;
            do {
                int endIndex = matcher.start();
                fragments.add(value.substring(startIndex, endIndex));
                startIndex = matcher.end();
                paramOrders.add(Integer.valueOf(value.substring(endIndex+1, startIndex-1)));
            } while (matcher.find());
            fragments.add(value.substring(startIndex));
        } else {
            fragments.add(value);
        }
        fragments.trimToSize();
        this.fragments = Collections.unmodifiableList(fragments);
        this.paramOrders = paramOrders.stream().mapToInt(Integer::intValue).toArray();
    }

    public String apply() {
        if (fragments.size() == 1) {
            return fragments.get(0);
        } else {
            throw new IllegalArgumentException("MessageFormat expected " + (fragments.size() - 1) + " parameters. But provided none");
        }
    }

    public Content apply(Object param1) {
        return output -> {
            if (fragments.size() == 2) {
                output.writeContent(fragments.get(0));
                writeParam(output, param1);
                output.writeContent(fragments.get(1));
            } else {
                throw new IllegalArgumentException("MessageFormat expected " + (fragments.size() - 1) + " parameters. But provided 1");
            }
        };
    }

    public Content apply(Object param1, Object param2) {
        return output -> {
            if (fragments.size() == 3) {
                output.writeContent(fragments.get(0));
                writeParam(output, paramOrders[0] == 0 ? param1 : param2);
                output.writeContent(fragments.get(1));
                writeParam(output, paramOrders[1] == 1 ? param2 : param1);
                output.writeContent(fragments.get(2));
            } else {
                throw new IllegalArgumentException("MessageFormat expected " + (fragments.size() - 1) + " parameters. But provided 2");
            }
        };
    }

    public Content apply(Object param1, Object param2, Object param3) {
        return output -> {
            if (fragments.size() == 4) {
                output.writeContent(fragments.get(0));
                writeParam(output, paramOrders[0] == 0 ? param1 : paramOrders[0] == 1 ? param2 : param3);
                output.writeContent(fragments.get(1));
                writeParam(output, paramOrders[1] == 1 ? param2 : paramOrders[0] == 0 ? param1 : param3);
                output.writeContent(fragments.get(2));
                writeParam(output, paramOrders[2] == 2 ? param3 : paramOrders[0] == 1 ? param2 : param1);
                output.writeContent(fragments.get(3));
            } else {
                throw new IllegalArgumentException("MessageFormat expected " + (fragments.size() - 1) + " parameters. But provided 3");
            }
        };
    }

    public Content apply(Object... params) {
        return output -> {
            for (int i=0; i<fragments.size(); i++) {
                output.writeContent(fragments.get(i));
                if (params.length > i) {
                    writeParam(output, params[paramOrders[i]]);
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
            sb.append("{").append(paramOrders[i-1]).append("}").append(fragments.get(i));
        }
        return sb.toString();
    }
}
