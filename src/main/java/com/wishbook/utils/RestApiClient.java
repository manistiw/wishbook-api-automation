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
     * Generic request builder
     */
    public static RequestBuilder request() {
        return new RequestBuilder();
    }
    
    /**
     * Fluent API for building requests
     */
    public static class RequestBuilder {
        private RequestSpecification request;
        private Object body;
        
        public RequestBuilder() {
            this.request = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        }
        
        public RequestBuilder withToken(String token) {
            if (token != null && !token.isEmpty()) {
                this.request.header("Authorization", "Bearer " + token);
            }
            return this;
        }
        
        public RequestBuilder withBody(Object body) {
            this.body = body;
            if (body != null) {
                this.request.body(body);
            }
            return this;
        }
        
        public RequestBuilder withQueryParams(Map<String, String> params) {
            if (params != null && !params.isEmpty()) {
                this.request.queryParams(params);
            }
            return this;
        }
        
        public RequestBuilder withQueryParam(String key, String value) {
            this.request.queryParam(key, value);
            return this;
        }
        
        public Response post(String endpoint) {
            return request.when().post(endpoint).then().extract().response();
        }
        
        public Response put(String endpoint) {
            return request.when().put(endpoint).then().extract().response();
        }
        
        public Response patch(String endpoint) {
            return request.when().patch(endpoint).then().extract().response();
        }
        
        public Response get(String endpoint) {
            return request.when().get(endpoint).then().extract().response();
        }
        
        public Response delete(String endpoint) {
            return request.when().delete(endpoint).then().extract().response();
        }
        
        /**
         * Execute POST and extract specific field
         */
        public <T> T postAndExtract(String endpoint, String field, Class<T> type) {
            Response response = post(endpoint);
            return response.path(field);
        }
        
        /**
         * Execute POST and return deserialized object
         */
        public <T> T postAndGet(String endpoint, Class<T> responseType) {
            Response response = post(endpoint);
            return response.as(responseType);
        }
        
        /**
         * Execute PUT and return deserialized object
         */
        public <T> T putAndGet(String endpoint, Class<T> responseType) {
            Response response = put(endpoint);
            return response.as(responseType);
        }
        
        /**
         * Execute PATCH and return deserialized object
         */
        public <T> T patchAndGet(String endpoint, Class<T> responseType) {
            Response response = patch(endpoint);
            return response.as(responseType);
        }
        
        /**
         * Execute GET and return deserialized object
         */
        public <T> T getAndExtract(String endpoint, Class<T> responseType) {
            Response response = get(endpoint);
            return response.as(responseType);
        }
    }
    
    // ========== Convenience Methods ==========
    
    /**
     * POST request
     */
    public static Response post(String endpoint, Object body, String token) {
        return request().withToken(token).withBody(body).post(endpoint);
    }
    
    /**
     * PUT request
     */
    public static Response put(String endpoint, Object body, String token) {
        return request().withToken(token).withBody(body).put(endpoint);
    }
    
    /**
     * PATCH request
     */
    public static Response patch(String endpoint, Object body, String token) {
        return request().withToken(token).withBody(body).patch(endpoint);
    }
    
    /**
     * GET request
     */
    public static Response get(String endpoint, Map<String, String> queryParams, String token) {
        return request().withToken(token).withQueryParams(queryParams).get(endpoint);
    }
    
    /**
     * GET request without query params
     */
    public static Response get(String endpoint, String token) {
        return request().withToken(token).get(endpoint);
    }
    
    /**
     * DELETE request
     */
    public static Response delete(String endpoint, String token) {
        return request().withToken(token).delete(endpoint);
    }
    
    // ========== Specialized Methods ==========
    
    /**
     * Generate token and extract it directly
     */
    public static String generateToken(String endpoint, String employeeId, String role) {
        Map<String, String> tokenRequest = Map.of(
            "employeeId", employeeId,
            "role", role
        );
        return request()
            .withBody(tokenRequest)
            .postAndExtract(endpoint, "token", String.class);
    }
    
    /**
     * Create resource and extract ID
     */
    public static String createAndGetId(String endpoint, Object body, String token, String idField) {
        return request()
            .withToken(token)
            .withBody(body)
            .postAndExtract(endpoint, idField, String.class);
    }
}