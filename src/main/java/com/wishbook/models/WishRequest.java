package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishRequest {

    @JsonProperty("date")
    private String date;

    @JsonProperty("shiftId")
    private Integer shiftId;

    @JsonProperty("shiftIds")
    private List<Integer> shiftIds;

    @JsonProperty("isActive")
    private Boolean isActive;

    public WishRequest() {
    }

    public WishRequest(String date, Integer shiftId, List<Integer> shiftIds, Boolean isActive) {
        this.date = date;
        this.shiftId = shiftId;
        this.shiftIds = shiftIds;
        this.isActive = isActive;
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

    public List<Integer> getShiftIds() {
        return shiftIds;
    }

    public void setShiftIds(List<Integer> shiftIds) {
        this.shiftIds = shiftIds;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public static WishRequestBuilder builder() {
        return new WishRequestBuilder();
    }

    public static class WishRequestBuilder {
        private String date;
        private Integer shiftId;
        private List<Integer> shiftIds;
        private Boolean isActive;

        public WishRequestBuilder date(String date) {
            this.date = date;
            return this;
        }

        public WishRequestBuilder shiftId(Integer shiftId) {
            this.shiftId = shiftId;
            return this;
        }

        public WishRequestBuilder shiftIds(List<Integer> shiftIds) {
            this.shiftIds = shiftIds;
            return this;
        }

        public WishRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public WishRequest build() {
            return new WishRequest(date, shiftId, shiftIds, isActive);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishRequest that = (WishRequest) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(shiftId, that.shiftId) &&
                Objects.equals(shiftIds, that.shiftIds) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, shiftId, shiftIds, isActive);
    }
}