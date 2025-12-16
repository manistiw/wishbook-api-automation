
package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftAssignment {
    
    @JsonProperty("shiftId")
    private Integer shiftId;
    
    @JsonProperty("wishIds")
    private List<String> wishIds;
    
    @JsonProperty("employees")
    private List<EmployeeAssignment> employees;
    
    @JsonProperty("assignmentCount")
    private Integer assignmentCount;
    
    public ShiftAssignment() {
    }
    
    public ShiftAssignment(Integer shiftId, List<String> wishIds) {
        this.shiftId = shiftId;
        this.wishIds = wishIds;
    }
    
    public Integer getShiftId() {
        return shiftId;
    }
    
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }
    
    public List<String> getWishIds() {
        return wishIds;
    }
    
    public void setWishIds(List<String> wishIds) {
        this.wishIds = wishIds;
    }
    
    public List<EmployeeAssignment> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<EmployeeAssignment> employees) {
        this.employees = employees;
    }
    
    public Integer getAssignmentCount() {
        return assignmentCount;
    }
    
    public void setAssignmentCount(Integer assignmentCount) {
        this.assignmentCount = assignmentCount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiftAssignment that = (ShiftAssignment) o;
        return Objects.equals(shiftId, that.shiftId) &&
               Objects.equals(wishIds, that.wishIds);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(shiftId, wishIds);
    }
}