package com.example.cslee.kamponghubso.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CSLee on 21/2/2018.
 */

public class Advert {


    //This is Base64 string to allow user to upload own image
    private String adImage;

    private String shopId;

    public Advert() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }
    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("adImage", adImage);
        result.put("shopId", shopId);
       return result;
    }

}
