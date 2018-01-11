package com.example.cslee.kamponghubso.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Shop {

    private String shopName;
    private String shopImageUrl;
    private String shopAddress;
    private String shopLatitude;
    private String shopLongitude;
    private String timeEnd;
    private String timeStart;

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

}
// [END comment_class]

