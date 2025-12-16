package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftRequirementRequest {
    
    @JsonProperty("requirements")
    private List<ShiftRequirement> requirements;
    
    public ShiftRequirementRequest() {
    }
    
    public ShiftRequirementRequest(List<ShiftRequirement> requirements) {
        this.requirements = requirements;
    }
    
    public List<ShiftRequirement> getRequirements() {
        return requirements;
    }
    
    public void setRequirements(List<ShiftRequirement> requirements) {
        this.requirements = requirements;
    }
}
