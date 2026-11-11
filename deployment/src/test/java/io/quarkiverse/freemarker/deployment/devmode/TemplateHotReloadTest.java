package io.quarkiverse.freemarker.deployment.devmode;

import static org.hamcrest.Matchers.is;

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

/**
 * https://github.com/quarkiverse/quarkus-freemarker/issues/317
 */
public class TemplateHotReloadTest {

    private static final String TEMPLATE = "freemarker/templates/greeting.ftl";

    @RegisterExtension
    static final QuarkusDevModeTest TEST = new QuarkusDevModeTest()
            .withApplicationRoot(root -> root
                    .addClass(GreetingResource.class)
                    .addAsResource(new StringAsset("Hello ${name}!"), TEMPLATE));

    @Test
    public void templatesAreReloaded() {
        assertGreeting("Hello Bob!");

        TEST.modifyResourceFile(TEMPLATE, s -> s.replace("Hello", "Hi"));
        assertGreeting("Hi Bob!");

        TEST.modifyResourceFile(TEMPLATE, s -> s.replace("Hi", "Bonjour"));
        assertGreeting("Bonjour Bob!");
    }

    private static void assertGreeting(String expected) {
        RestAssured.get("/greeting/injected").then().statusCode(200).body(is(expected));
        RestAssured.get("/greeting/lookup").then().statusCode(200).body(is(expected));
    }
}
