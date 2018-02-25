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
    private String shopName;


    private String shopZone;
    private String adDate;
    private String adDescription;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getAdDate() {
        return adDate;
    }

    public void setAdDate(String adDate) {
        this.adDate = adDate;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public String getShopZone() {
        return shopZone;
    }

    public void setShopZone(String shopZone) {
        this.shopZone = shopZone;
    }
    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }
    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("adImage", adImage);
        result.put("shopId", shopId);
        result.put("shopName", shopName);
        result.put("adDescription", adDescription);
        result.put("adDate", adDate);
        result.put("shopZone", shopZone);
       return result;
    }

}
