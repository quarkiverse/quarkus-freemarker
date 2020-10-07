# Quarkus Freemarker

Freemarker is a very popular and mature templating engine. Its integration as a Quarkus extension
provides developers ease of configuration, and offers support for native images.

To get started, add the dependency:

```xml
<dependency>
    <groupId>io.quarkiverse</groupId>
    <artifactId>quarkus-freemarker</artifactId>
</dependency>
```

Add some `ftl` templates in `src/main/resources/freemarker/templates`:
```
Hello ${name}!
```

Inject the template in your code:

```java
@Inject
@TemplatePath("hello.ftl")
Template hello;
```

Build a model and start rendering your template:

```java
StringWriter stringWriter = new StringWriter();
hello.process(Map.of("name", "bob"), stringWriter);
String result = stringWriter.toString();
```

For more details, check the complete [documentation](https://quarkus.io/guides/freemarker).

## Contributors
<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

