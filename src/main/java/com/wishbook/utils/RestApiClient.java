package com.wishbook.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RestApiClient {

    private static final String BASE_URL = System.getProperty("base.url", "http://localhost:3000");

    static {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Create a base request specification with common headers
     */
    private static RequestSpecification getBaseRequest(String authToken) {
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);

        if (authToken != null && !authToken.isEmpty()) {
            request.header("Authorization", "Bearer " + authToken);
        }

        return request;
    }

    /**
     * POST request
     */
    public static Response post(String endpoint, Object body, String authToken) {
        return getBaseRequest(authToken)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * PUT request
     */
    public static Response put(String endpoint, Object body, String authToken) {
        return getBaseRequest(authToken)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * PATCH request
     */
    public static Response patch(String endpoint, Object body, String authToken) {
        return getBaseRequest(authToken)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * GET request
     */
    public static Response get(String endpoint, Map<String, String> queryParams, String authToken) {
        RequestSpecification request = getBaseRequest(authToken);

        if (queryParams != null && !queryParams.isEmpty()) {
            request.queryParams(queryParams);
        }

        return request
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * GET request without query params
     */
    public static Response get(String endpoint, String authToken) {
        return get(endpoint, null, authToken);
    }

    /**
     * DELETE request
     */
    public static Response delete(String endpoint, String authToken) {
        return getBaseRequest(authToken)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}