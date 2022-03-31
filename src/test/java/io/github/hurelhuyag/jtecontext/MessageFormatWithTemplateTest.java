package io.github.hurelhuyag.jtecontext;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFormatWithTemplateTest {

    private TemplateEngine templateEngine;

    @BeforeEach
    public void init() {
        templateEngine = TemplateEngine.createPrecompiled(ContentType.Html);
    }

    @Test
    public void test1() throws IOException {
        var output = new StringOutput();

        var locale = Locale.ENGLISH;
        var mb = new MessageBundle(MessageFormatWithTemplateTest.class.getClassLoader(), "io/github/hurelhuyag/jtecontext/messages");

        var mf = new Context(ZoneId.systemDefault(), locale, mb);

        templateEngine.render("template.jte", Map.of("mf", mf), output);

        var outputString = output.toString();

        //System.out.println(output);

        assertTrue(outputString.contains("<div>Generic English Message</div>"));
        assertTrue(outputString.contains("<div>param1: p1.</div>"));
        assertTrue(outputString.contains("<div>param1: p1, param2: p2, param3: p3.</div>"));
    }
}
