package com.apitest.api;

import com.apitest.base.BaseSpec;
import com.apitest.pojo.Author;
import com.apitest.utils.ConfigManager;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class AuthorsApi {
    

    public static Response getAllAuthors() {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .get(ConfigManager.getProperty("AUTHORS_ENDPOINT"));
    }
    

    public static Response getAuthorById(int id) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .get(ConfigManager.getProperty("AUTHORS_ENDPOINT") + "/" + id);
    }
    

    public static Response createAuthor(Author author) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .body(author)
                .when()
                .post(ConfigManager.getProperty("AUTHORS_ENDPOINT"));
    }
    

    public static Response updateAuthor(int id, Author author) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .body(author)
                .when()
                .put(ConfigManager.getProperty("AUTHORS_ENDPOINT") + "/" + id);
    }
    

    public static Response deleteAuthor(int id) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .delete(ConfigManager.getProperty("AUTHORS_ENDPOINT") + "/" + id);
    }
}

