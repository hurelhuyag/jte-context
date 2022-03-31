package io.github.hurelhuyag.jtecontext;

import gg.jte.Content;
import gg.jte.output.StringOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {

    private Context context;

    @BeforeEach
    public void init() throws IOException {
        var messageBundle = new MessageBundle(ContextTest.class.getClassLoader(), "io/github/hurelhuyag/jtecontext/messages");
        context = new Context(ZoneId.systemDefault(), Locale.ENGLISH, messageBundle);
    }

    private void assertEqualsContent(String expected, Content content) {
        var result = new StringOutput();
        content.writeTo(result);
        assertEquals(expected, result.toString());
    }

    @Test
    public void format_with1param() {
        var result = context.formatMessage("message_1_param", "value1");
        assertEqualsContent("param1: value1.", result);
    }

    @Test
    public void format_with2param() {
        var result = context.formatMessage("message2", "value1", "value2");
        assertEqualsContent("param1: value1, param2: value2.", result);
    }

    @Test
    public void format_with3param() {
        var result = context.formatMessage("message_3_param", "value1", "value2", "value3");
        assertEqualsContent("param1: value1, param2: value2, param3: value3.", result);
    }

    @Test
    public void formatDate() {
        var input = LocalDateTime.of(2022, 3, 8, 1, 2, 3);
        var result = context.formatDate(input, "yyyy-MM-dd'T'HH:mm:ss");
        assertEqualsContent("2022-03-08T01:02:03", result);
    }
}
