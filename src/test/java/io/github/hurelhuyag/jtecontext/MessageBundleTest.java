package io.github.hurelhuyag.jtecontext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBundleTest {

    private MessageBundle messageBundle;
    private Context context;

    @BeforeEach
    public void init() throws IOException {
        messageBundle = new MessageBundle(MessageBundleTest.class.getClassLoader(), "io/github/hurelhuyag/jtecontext/messages");
        context = new Context(ZoneId.systemDefault(), Locale.ENGLISH, messageBundle);
    }

    @Test
    public void lookup_en() {
        var result = messageBundle.lookup("message1", new Locale("en"));
        assertEquals("Generic English Message", result.toString());
    }

    @Test
    public void lookup_en_GB() {
        var result = messageBundle.lookup("message1", new Locale("en", "GB"));
        assertEquals("Generic English Message", result.toString());
    }

    @Test
    public void lookup_en_US() {
        var result = messageBundle.lookup("message1", new Locale("en", "US"));
        assertEquals("US English Message", result.toString());
    }

    @Test
    public void lookup_en_US_with_unknown_key() {
        var result = messageBundle.lookup("message3", new Locale("en", "US"));
        assertEquals("This message only available as a generic en", result.toString());
    }

    @Test
    public void lookup_mn_but_avail_in_others() {
        var result = messageBundle.lookup("message3", new Locale("mn"));
        assertNull(result);
    }

    @Test
    public void lookup_mn() {
        var result = messageBundle.lookup("message1", new Locale("mn"));
        assertEquals("MN message", result.toString());
    }

    @Test
    public void lookup_unknown_locale() {
        var result = messageBundle.lookup("message1", new Locale("zh"));
        assertEquals("Generic Message", result.toString());
    }

    @Test
    public void lookup_en_but_avail_in_generic() {
        var result = messageBundle.lookup("message2", new Locale("en"));
        assertEquals("param1: {0}, param2: {1}.", result.toString());
    }

    @Test
    public void lookup_unknown_key() {
        var result = messageBundle.lookup("this.key.is.unknown", new Locale("en"));
        assertNull(result);
    }
}
