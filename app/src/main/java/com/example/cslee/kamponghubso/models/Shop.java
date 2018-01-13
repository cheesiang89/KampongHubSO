package com.example.cslee.kamponghubso.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Shop {

    public String uid;
    public String shopOwner;
    private String shopName;
    //This is Base64 string to allow user to upload own image
    private String shopImage;
    
    private String shopAddress;
    private String shopLatitude;
    private String shopLongitude;
    private String timeEnd;
    private String timeStart;

    //This was link to image stored in Firebase Storage in 1st version of program
    private String shopImageUrl;

    public Shop() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Shop(String shopname, String shopImageUrl,
                String address, String shopLatitude,
                String shopLongitude, String timeEnd, String timeStart) {
        this.shopName= shopname;
        this.shopImageUrl = shopImageUrl;
        this.shopAddress=address;
        this.shopLatitude= shopLatitude;
        this.shopLongitude=shopLongitude;
        this.timeEnd=timeEnd;
        this.timeStart=timeStart;
    }
    //For testing: Constructor to take in only ShopName, ShopImage. Other values are hardcoded
    public Shop(String uid, String shopOwner, String shopname, String shopImage) {
        this.uid=uid;
        this.shopOwner=shopOwner;
        this.shopName= shopname;
        this.shopImage = shopImage;
        this.shopAddress="Blk 28";
        this.shopLatitude= "1.3333301";
        this.shopLongitude="103.74329650000004";
        this.timeEnd="0000";
        this.timeStart="0000";
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String address) {
        this.shopAddress = address;
    }


    public String getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(String shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public String getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(String shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    // "toMap" is to put the whole object as Hashmap in Firebase child branch ("Shops")
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("shopOwner", shopOwner);
        result.put("shopName", shopName);
        result.put("shopImage", shopImage);
        result.put("shopAddress", shopAddress);
        result.put("shopLatitude", shopLatitude);
        result.put("shopLongitude", shopLongitude);
        result.put("timeEnd", timeEnd);
        result.put("timeStart", timeStart);

        return result;
    }

}


