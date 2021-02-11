package io.quarkiverse.it.freemarker;

import static org.hamcrest.Matchers.is;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
public class ConfigDefaultsTest {

    @Test
    public void injectedTemplate() {
        RestAssured.when()
                .get("/configDefaults/injectedTemplate/?name=bob")
                .then()
                .statusCode(200)
                .body(is("Hello bob!Hi bob!"));
    }

    @SuppressWarnings("serial")
    @Test
    public void dynamicTemplate() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LinkedHashMap<String, String>() {
                    {
                        put("name", "Joe");
                    }
                })
                .queryParam("ftl", "includes/hello.ftl")
                .post("/configDefaults/dynamicTemplate")
                .then()
                .statusCode(200)
                .body(is("Hello Joe!"));
    }
}
