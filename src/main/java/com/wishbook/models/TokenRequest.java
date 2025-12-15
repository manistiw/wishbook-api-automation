
package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class TokenRequest {
    
    @JsonProperty("employeeId")
    private String employeeId;
    
    @JsonProperty("role")
    private String role;
    
    public TokenRequest() {
    }
    
    public TokenRequest(String employeeId, String role) {
        this.employeeId = employeeId;
        this.role = role;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenRequest that = (TokenRequest) o;
        return Objects.equals(employeeId, that.employeeId) &&
               Objects.equals(role, that.role);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(employeeId, role);
    }
}
