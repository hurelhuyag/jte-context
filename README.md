# JTE Template utilities. ![ci badge](https://github.com/hurelhuyag/jte-context/actions/workflows/ci.yaml/badge.svg)

```java
Context::formatMessage
Context::formatDate
```

## Example

Example `template.jte` file
```jte
@import io.github.hurelhuyag.jtecontext.Context
@param Context ctx
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Title</title>
    <meta charset="UTF-8">
</head>
<body>
    <div>${ctx.formatMessage("message1")}</div>
    <div>${ctx.formatMessage("message_1_param", "p1")}</div>
    <div>${ctx.formatMessage("message_3_param", "p1", "p2", "p3")}</div>
</body>
</html>
```

Example java code

```java
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import io.github.hurelhuyag.jtecontext.Context;
import io.github.hurelhuyag.jtecontext.TemplateUtils;
import io.github.hurelhuyag.jtecontext.MessageBundle;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        var messageBundle = new MessageBundle(Main.class.getClassLoader(), "messages");

        var templateEngine = TemplateEngine.createPrecompiled(ContentType.Html);

        var params = Map.of("ctx", new Context(ZoneId.systemDefault(), Locale.ENGLISH, messageBundle));
        var result = new StringOutput();
        templateEngine.render("template.jte", params, result);

        System.out.println(result);
    }
}
```

## Features

- Format date times
- Format messages

## ToDo

- Format duration
- Format relative datetime

## Benchmark

- benchmark
