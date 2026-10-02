# Quarkus Freemarker

Freemarker is a very popular and mature templating engine. Its integration as a Quarkus extension
provides developers ease of configuration, and offers support for native images.

To get started, add the dependency:

```xml
<dependency>
    <groupId>io.quarkiverse.freemarker</groupId>
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

For more details, check the complete [documentation](https://quarkiverse.github.io/quarkiverse-docs/quarkus-freemarker/dev/index.html).

## Migration to Quarkus 4

Starting with Quarkus 4, the `quarkus.freemarker.excludes` and `quarkus.freemarker."template-set-name".excludes`
properties are no longer supported, because native image resource configuration only supports include globs.
Use more specific `includes` globs to select the templates to embed in the native image.

The deprecated `quarkus.freemarker.resource-paths` property is no longer supported.
Use template sets instead: `quarkus.freemarker.base-path` and `quarkus.freemarker.includes` for the default one,
`quarkus.freemarker."template-set-name".base-path` and `quarkus.freemarker."template-set-name".includes` for
additional ones. For instance `quarkus.freemarker.resource-paths=my/templates` becomes
`quarkus.freemarker.base-path=my/templates` and `quarkus.freemarker.includes=**`.

`includes` globs are passed as is to the native image builder, where `**` only matches
nested directories when it is a whole path segment. Write `**/*.ftl` instead of `**.ftl`, otherwise templates
located in subdirectories are silently left out of the native image.

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

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

