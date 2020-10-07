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
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/vsevel"><img src="https://avatars3.githubusercontent.com/u/6041620?v=4" width="100px;" alt=""/><br /><sub><b>Vincent Sevel</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkiverse-freemarker/commits?author=vsevel" title="Code">💻</a> <a href="#maintenance-vsevel" title="Maintenance">🚧</a></td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

