package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishResponse {

    @JsonProperty("wishId")
    private String wishId;

    @JsonProperty("employeeId")
    private String employeeId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("shiftId")
    private Integer shiftId;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    public WishResponse() {
    }

    public WishResponse(String wishId, String employeeId, String date, Integer shiftId,
                        Boolean isActive, String createdAt, String updatedAt) {
        this.wishId = wishId;
        this.employeeId = employeeId;
        this.date = date;
        this.shiftId = shiftId;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getWishId() {
        return wishId;
    }

    public void setWishId(String wishId) {
        this.wishId = wishId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static WishResponseBuilder builder() {
        return new WishResponseBuilder();
    }

    public static class WishResponseBuilder {
        private String wishId;
        private String employeeId;
        private String date;
        private Integer shiftId;
        private Boolean isActive;
        private String createdAt;
        private String updatedAt;

        public WishResponseBuilder wishId(String wishId) {
            this.wishId = wishId;
            return this;
        }

        public WishResponseBuilder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public WishResponseBuilder date(String date) {
            this.date = date;
            return this;
        }

        public WishResponseBuilder shiftId(Integer shiftId) {
            this.shiftId = shiftId;
            return this;
        }

        public WishResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public WishResponseBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WishResponseBuilder updatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WishResponse build() {
            return new WishResponse(wishId, employeeId, date, shiftId, isActive, createdAt, updatedAt);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishResponse that = (WishResponse) o;
        return Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(shiftId, that.shiftId) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, date, shiftId, isActive);
    }
}