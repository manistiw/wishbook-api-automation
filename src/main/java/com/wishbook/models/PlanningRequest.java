package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanningRequest {
    
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("effectiveMoment")
    private String effectiveMoment;
    
    @JsonProperty("assignments")
    private List<ShiftAssignment> assignments;
    
    public PlanningRequest() {
    }
    
    public PlanningRequest(String date, String effectiveMoment, List<ShiftAssignment> assignments) {
        this.date = date;
        this.effectiveMoment = effectiveMoment;
        this.assignments = assignments;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getEffectiveMoment() {
        return effectiveMoment;
    }
    
    public void setEffectiveMoment(String effectiveMoment) {
        this.effectiveMoment = effectiveMoment;
    }
    
    public List<ShiftAssignment> getAssignments() {
        return assignments;
    }
    
    public void setAssignments(List<ShiftAssignment> assignments) {
        this.assignments = assignments;
    }
}
