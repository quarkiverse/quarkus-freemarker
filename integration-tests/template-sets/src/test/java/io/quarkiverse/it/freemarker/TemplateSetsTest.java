package io.quarkiverse.it.freemarker;

import static org.hamcrest.Matchers.is;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
public class TemplateSetsTest {

    @SuppressWarnings("serial")
    @Test
    public void defaultTemplatePathUnavailable() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LinkedHashMap<String, String>() {
                    {
                        put("name", "Joe");
                    }
                })
                .queryParam("ftl", "greetings.ftl")
                .post("/templateSets/dynamicTemplate")
                .then()
                .statusCode(500);
    }

    @SuppressWarnings("serial")
    @Test
    public void nameLessTemplateSet() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LinkedHashMap<String, String>() {
                    {
                        put("favorite", "Cycling");
                        put("disliked", "Jogging");
                    }
                })
                .queryParam("ftl", "sports.ftl")
                .post("/templateSets/dynamicTemplate")
                .then()
                .statusCode(200)
                .body(is("Cycling is my favorite sport! Jogging is my disliked sport!"));
    }

    @SuppressWarnings("serial")
    @Test
    public void customTemplateSet() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LinkedHashMap<String, String>() {
                    {
                        put("favorite", "Apple");
                        put("disliked", "Orange");
                    }
                })
                .queryParam("ftl", "fruits.ftl")
                .post("/templateSets/dynamicTemplate")
                .then()
                .statusCode(200)
                .body(is("Apple is my favorite fruit! Orange is my disliked fruit!"));
    }
}
