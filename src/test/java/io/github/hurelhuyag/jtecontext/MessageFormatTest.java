package io.github.hurelhuyag.jtecontext;

import gg.jte.Content;
import gg.jte.output.StringOutput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFormatTest {

    private void assertEqualsContent(String expected, Content content) {
        var result = new StringOutput();
        content.writeTo(result);
        assertEquals(expected, result.toString());
    }

    @Test
    public void noParam() {
        var format = new MessageFormat("Hello world");
        assertEquals("Hello world", format.toString());
        assertEquals("Hello world", format.apply());
    }

    @Test
    public void leadingParam() {
        var format = new MessageFormat("{0}. Leading param");
        assertEquals("{0}. Leading param", format.toString());
        assertEqualsContent("P1. Leading param", format.apply("P1"));
    }

    @Test
    public void trailingParam() {
        var format = new MessageFormat("Trailing Param, {0}");
        assertEquals("Trailing Param, {0}", format.toString());
        assertEqualsContent("Trailing Param, P1", format.apply("P1"));
    }

    @Test
    public void middleParam() {
        var format = new MessageFormat("Start, {0}, End");
        assertEquals("Start, {0}, End", format.toString());
        assertEqualsContent("Start, P1, End", format.apply("P1"));
    }

    @Test
    public void touchingEachOtherParams() {
        var format = new MessageFormat("Start, {0}{1}, End");
        assertEquals("Start, {0}{1}, End", format.toString());
        assertEqualsContent("Start, P1P2, End", format.apply("P1", "P2"));
    }

    @Test
    public void multipleParams() {
        var format = new MessageFormat("{0}{1}{2}{3}{4}{5}");
        assertEquals("{0}{1}{2}{3}{4}{5}", format.toString());
        assertEqualsContent("123456", format.apply("1", "2", "3", "4", "5", "6"));
    }

    @Test
    public void multipleParamsChangeOrder() {
        var format = new MessageFormat("{0}{5}{2}{4}{3}{1}");
        assertEquals("{0}{5}{2}{4}{3}{1}", format.toString());
        assertEqualsContent("052431", format.apply("0", "1", "2", "3", "4", "5"));
    }
}
