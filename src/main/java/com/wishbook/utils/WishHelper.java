package com.wishbook.utils;

import com.wishbook.models.WishRequest;
import com.wishbook.models.WishResponse;

import java.util.ArrayList;
import java.util.List;

public class WishHelper {
    
    private static final String WISHES_ENDPOINT = "/wishes";
    
    /**
     * Expand a wish template with multiple shifts into individual wishes
     */
    public static List<WishRequest> expandWish(WishRequest template) {
        List<WishRequest> wishes = new ArrayList<>();
        
        if (template.getShiftIds() != null && !template.getShiftIds().isEmpty()) {
            // Multiple shifts - create one wish per shift
            for (Integer shiftId : template.getShiftIds()) {
                WishRequest wish = new WishRequest();
                wish.setDate(template.getDate());
                wish.setShiftId(shiftId);
                wish.setShiftIds(null);
                wishes.add(wish);
            }
        } else {
            // Single shift
            WishRequest wish = new WishRequest();
            wish.setDate(template.getDate());
            wish.setShiftId(template.getShiftId());
            wish.setShiftIds(null);
            wishes.add(wish);
        }
        
        return wishes;
    }
    
    /**
     * Create a wish via API
     */
    public static WishResponse createWish(String token, WishRequest wishRequest) {
        return RestApiClient.request()
            .withToken(token)
            .withBody(wishRequest)
            .putAndGet(WISHES_ENDPOINT, WishResponse.class);
    }
    
    /**
     * Create a wish and return only the wish ID
     */
    public static String createWishAndGetId(String token, WishRequest wishRequest) {
        WishResponse response = createWish(token, wishRequest);
        return response.getWishId();
    }
    
    /**
     * Update a wish
     */
    public static WishResponse updateWish(String token, String wishId, WishRequest updateRequest) {
        return RestApiClient.request()
            .withToken(token)
            .withBody(updateRequest)
            .putAndGet(WISHES_ENDPOINT + "/" + wishId, WishResponse.class);
    }
    
    /**
     * Delete a wish
     */
    public static void deleteWish(String token, String wishId) {
        RestApiClient.request()
            .withToken(token)
            .delete(WISHES_ENDPOINT + "/" + wishId);
    }
    
    /**
     * Get wishes for a date and shift
     */
    public static WishResponse[] getWishes(String token, String date, Integer shiftId) {
        return RestApiClient.request()
            .withToken(token)
            .withQueryParam("date", date)
            .withQueryParam("shiftId", String.valueOf(shiftId))
            .getAndExtract(WISHES_ENDPOINT, WishResponse[].class);
    }
}