package io.quarkiverse.it.freemarker;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
public class FreemarkerTest {

    private static final Logger log = LoggerFactory.getLogger(FreemarkerTest.class);

    public static final String BASE64_EXPECTED_VALUE = "    hello in base64 is aGVsbG8=";

    public static final String NAME_EXPECTED_VALUE = "my name is bob";

    @Test
    public void hello() {
        RestAssured.when().get("/freemarker/hello/?name=bob").then().body(is("Hello bob!"));
    }

    @Test
    public void hello_ftl() {
        RestAssured.when().get("/freemarker/hello_ftl/?name=bob&ftl=hello.ftl").then().body(is("Hello bob!"));
    }

    @Test
    public void sub() {
        RestAssured.when().get("/freemarker/sub").then().body(containsString(BASE64_EXPECTED_VALUE));
    }

    @Test
    public void subInject() {
        RestAssured.when().get("/freemarker/subInject").then().body(containsString(BASE64_EXPECTED_VALUE));
    }

    @Test
    public void person() {
        RestAssured.when().get("/freemarker/person").then().body(is(NAME_EXPECTED_VALUE));
    }

    @Test
    public void personInject() {
        RestAssured.when().get("/freemarker/personInject").then().body(is(NAME_EXPECTED_VALUE));
    }

    @Test
    public void personext() {
        RestAssured.when().get("/freemarker/personext").then().body(is(NAME_EXPECTED_VALUE));
    }

    @Test
    public void dates() {
        // locale, time zone and formats from typed properties; boolean format overridden by the settings map
        // "é" in ISO-8859-1 (url-escaping-charset) is %E9, while it would be %C3%A9 in UTF-8
        RestAssured.when().get("/freemarker/dates").then()
                .body(is("15/01/2026|14:45|15/01/2026 14:45|fr_FR|oui|%E9"));
    }

    @Test
    public void settings() {
        RestAssured.when().get("/freemarker/settings").then()
                .body(is("sqlDateAndTimeTimeZone=UTC|outputEncoding=UTF-8|apiBuiltinEnabled=true"));
    }

}
