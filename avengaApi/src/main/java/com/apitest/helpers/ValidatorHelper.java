package com.apitest.helpers;

import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidatorHelper {
    
    public static void assertMatchesSchema(Response response, String schemaFileName) {
        assertThat(response.asString(), matchesJsonSchemaInClasspath("schemas/" + schemaFileName));
    }
}

