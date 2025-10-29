package com.apitest.utils;

import com.apitest.base.BaseSpec;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestUtils {

    private static final Logger logger = LogManager.getLogger(RestUtils.class);

    public static Response get(String endpoint, int expectedStatus, String jsonSchemaClasspath) {
        logger.info("GET {}", endpoint);
        Response response = given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .get(endpoint)
                .andReturn();

        attachAndValidate(response, expectedStatus, jsonSchemaClasspath);
        return response;
    }

    public static Response post(String endpoint, Object body, int expectedStatus, String jsonSchemaClasspath) {
        logger.info("POST {} - body: {}", endpoint, body);
        Response response = given()
                .spec(BaseSpec.getRequestSpec())
                .body(body)
                .when()
                .post(endpoint)
                .andReturn();

        attachAndValidate(response, expectedStatus, jsonSchemaClasspath);
        return response;
    }

    public static Response put(String endpoint, Object body, int expectedStatus, String jsonSchemaClasspath) {
        logger.info("PUT {} - body: {}", endpoint, body);
        Response response = given()
                .spec(BaseSpec.getRequestSpec())
                .body(body)
                .when()
                .put(endpoint)
                .andReturn();

        attachAndValidate(response, expectedStatus, jsonSchemaClasspath);
        return response;
    }

    public static Response delete(String endpoint, int expectedStatus, String jsonSchemaClasspath) {
        logger.info("DELETE {}", endpoint);
        Response response = given()
                .spec(BaseSpec.getRequestSpec())
                .when()
                .delete(endpoint)
                .andReturn();

        attachAndValidate(response, expectedStatus, jsonSchemaClasspath);
        return response;
    }

    private static void attachAndValidate(Response response, int expectedStatus, String jsonSchemaClasspath) {
        ExtentTest t = ExtentManager.getTest();
        if (t != null) {
            t.info("Response status: " + response.getStatusCode());
        }

        logger.info("Response status: {}", response.getStatusCode());

        try {
            org.testng.Reporter.getCurrentTestResult().setAttribute("response", response);
        } catch (Exception ignored) {
        }

        if (expectedStatus > 0 && response.getStatusCode() != expectedStatus) {
            String msg = "Expected status " + expectedStatus + " but got " + response.getStatusCode();
            logger.error(msg);
            throw new AssertionError(msg);
        }

        if (jsonSchemaClasspath != null && !jsonSchemaClasspath.isEmpty()) {
            try {
                response.then().assertThat().body(matchesJsonSchemaInClasspath(jsonSchemaClasspath));
            } catch (AssertionError e) {
                if (t != null) t.fail("Schema validation failed: " + e.getMessage());
                logger.error("Schema validation failed: {}", e.getMessage());
                throw e;
            }
        }
    }
}