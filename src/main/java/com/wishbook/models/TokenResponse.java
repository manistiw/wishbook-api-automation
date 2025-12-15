package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)  // ADD THIS - ignore extra fields
public class TokenResponse {
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("employeeId")
    private String employeeId;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("authorization")  // ADD THIS FIELD
    private String authorization;
    
    // No-arg constructor - MUST HAVE
    public TokenResponse() {
    }
    
    // All-args constructor
    public TokenResponse(String token, String employeeId, String role, String authorization) {
        this.token = token;
        this.employeeId = employeeId;
        this.role = role;
        this.authorization = authorization;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getAuthorization() {
        return authorization;
    }
    
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenResponse that = (TokenResponse) o;
        return Objects.equals(token, that.token) &&
               Objects.equals(employeeId, that.employeeId) &&
               Objects.equals(role, that.role);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(token, employeeId, role);
    }
    
    @Override
    public String toString() {
        return "TokenResponse{" +
                "token='" + (token != null ? token.substring(0, 20) + "..." : "null") + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", role='" + role + '\'' +
                ", authorization='" + (authorization != null ? authorization.substring(0, 27) + "..." : "null") + '\'' +
                '}';
    }
}