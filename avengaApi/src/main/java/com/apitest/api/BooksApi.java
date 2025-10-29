package com.apitest.api;

import com.apitest.base.BaseSpec;
import com.apitest.pojo.Book;
import com.apitest.utils.ConfigManager;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class BooksApi {
    

    public static Response getAllBooks() {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .get(ConfigManager.getProperty("BOOKS_ENDPOINT"));
    }
    

    public static Response getBookById(int id) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .get(ConfigManager.getProperty("BOOKS_ENDPOINT") + "/" + id);
    }
    

    public static Response createBook(Book book) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .body(book)
                .when()
                .post(ConfigManager.getProperty("BOOKS_ENDPOINT"));
    }
    

    public static Response updateBook(int id, Book book) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .body(book)
                .when()
                .put(ConfigManager.getProperty("BOOKS_ENDPOINT") + "/" + id);
    }
    

    public static Response deleteBook(int id) {
        return given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .delete(ConfigManager.getProperty("BOOKS_ENDPOINT") + "/" + id);
    }
}

