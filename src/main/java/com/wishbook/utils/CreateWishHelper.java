package com.wishbook.utils;

import java.util.ArrayList;
import java.util.List;

import com.wishbook.models.WishRequest;

public class CreateWishHelper {
    public List<WishRequest> expandWish(WishRequest template) {
        List<WishRequest> wishes = new ArrayList<>();
        if (template.getShiftIds() != null && !template.getShiftIds().isEmpty()) {
            // Multiple shifts - create one wish per shift
            for (Integer shiftId : template.getShiftIds()) {
                WishRequest wish = new WishRequest();
                wish.setDate(template.getDate());
                wish.setShiftId(shiftId);
                wish.setShiftIds(null);  // Clear the array
                wishes.add(wish);
            }
        } else {
            // Single shift - ensure array is cleared
            WishRequest wish = new WishRequest();
            wish.setDate(template.getDate());
            wish.setShiftId(template.getShiftId());
            wish.setShiftIds(null);  // Clear the array
            wishes.add(wish);
        }
        
        return wishes;
    }
    
}
