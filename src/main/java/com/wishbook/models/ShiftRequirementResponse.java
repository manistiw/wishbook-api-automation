package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)  // ← ADD THIS - ignore extra fields
public class ShiftRequirementResponse {
    
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("requirements")
    private List<ShiftRequirement> requirements;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")  // ← ADD THIS FIELD
    private String message;
    
    public ShiftRequirementResponse() {
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public List<ShiftRequirement> getRequirements() {
        return requirements;
    }
    
    public void setRequirements(List<ShiftRequirement> requirements) {
        this.requirements = requirements;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}