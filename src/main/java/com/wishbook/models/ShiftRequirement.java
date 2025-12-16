package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftRequirement {
    
    @JsonProperty("shiftId")
    private Integer shiftId;
    
    @JsonProperty("requiredCount")
    private Integer requiredCount;
    
    public ShiftRequirement() {
    }
    
    public ShiftRequirement(Integer shiftId, Integer requiredCount) {
        this.shiftId = shiftId;
        this.requiredCount = requiredCount;
    }
    
    public Integer getShiftId() {
        return shiftId;
    }
    
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }
    
    public Integer getRequiredCount() {
        return requiredCount;
    }
    
    public void setRequiredCount(Integer requiredCount) {
        this.requiredCount = requiredCount;
    }
}
