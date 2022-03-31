package io.github.hurelhuyag.jtecontext;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFormatTest {

    @Test
    public void noParam() {
        var format = new MessageFormat("Hello world");
        assertEquals("Hello world", format.toString());
    }

    @Test
    public void leadingParam() {
        var format = new MessageFormat("{0}. Leading param");
        assertEquals("{0}. Leading param", format.toString());
    }

    @Test
    public void trailingParam() {
        var format = new MessageFormat("Trailing Param, {0}");
        assertEquals("Trailing Param, {0}", format.toString());
    }

    @Test
    public void middleParam() {
        var format = new MessageFormat("Start, {0}, End");
        assertEquals("Start, {0}, End", format.toString());
    }

    @Test
    public void touchingEachOtherParams() {
        var format = new MessageFormat("Start, {0}{1}, End");
        assertEquals("Start, {0}{1}, End", format.toString());
    }

    @Test
    public void multipleParams() {
        var format = new MessageFormat("{0}{1}{2}{3}{4}{5}");
        assertEquals("{0}{1}{2}{3}{4}{5}", format.toString());
    }
}
