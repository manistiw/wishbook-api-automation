package com.wishbook.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkWishRequest {

    @JsonProperty("wishes")
    private List<WishRequest> wishes;

    public BulkWishRequest() {
    }

    public BulkWishRequest(List<WishRequest> wishes) {
        this.wishes = wishes;
    }

    public List<WishRequest> getWishes() {
        return wishes;
    }

    public void setWishes(List<WishRequest> wishes) {
        this.wishes = wishes;
    }

    public static BulkWishRequestBuilder builder() {
        return new BulkWishRequestBuilder();
    }

    public static class BulkWishRequestBuilder {
        private List<WishRequest> wishes;

        public BulkWishRequestBuilder wishes(List<WishRequest> wishes) {
            this.wishes = wishes;
            return this;
        }

        public BulkWishRequest build() {
            return new BulkWishRequest(wishes);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulkWishRequest that = (BulkWishRequest) o;
        return Objects.equals(wishes, that.wishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wishes);
    }
}