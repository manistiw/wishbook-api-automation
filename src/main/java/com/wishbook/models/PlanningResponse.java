
package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanningResponse {
    
    @JsonProperty("planningId")
    private String planningId;
    
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("effectiveMoment")
    private String effectiveMoment;
    
    @JsonProperty("assignments")
    private List<ShiftAssignment> assignments;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("totalShifts")
    private Integer totalShifts;
    
    @JsonProperty("totalAssignments")
    private Integer totalAssignments;
    
    public PlanningResponse() {
    }
    
    public String getPlanningId() {
        return planningId;
    }
    
    public void setPlanningId(String planningId) {
        this.planningId = planningId;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getTotalShifts() {
        return totalShifts;
    }
    
    
    public Integer getTotalAssignments() {
        return totalAssignments;
    }
    
    public void setTotalAssignments(Integer totalAssignments) {
        this.totalAssignments = totalAssignments;
    }
    public void setTotalShifts(Integer totalShifts) {
        this.totalShifts = totalShifts;
    }
}