package com.apitest.base;

import com.apitest.utils.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class BaseSpec {
    
    private static String token = null;
    private static RequestSpecification requestSpec;
    private static ResponseSpecification responseSpec;

    public static void initializeToken() {
        if (token == null) {
            String apiKey = ConfigManager.getApiKey();
            if (apiKey != null && !apiKey.isEmpty()) {
                token = apiKey;
            }
        }
    }

    public static void clearToken() {
        token = null;
    }

    public static String getToken() {
        return token;
    }

    public static RequestSpecification getRequestSpec() {
        if (token == null) {
            initializeToken();
        }

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);

        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        requestSpec = builder.build();
        return requestSpec;
    }

    public static ResponseSpecification getResponseSpec() {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        return responseSpec;
    }

    public static ResponseSpecification getResponseSpec(int expectedStatusCode) {
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        return responseSpec;
    }
}