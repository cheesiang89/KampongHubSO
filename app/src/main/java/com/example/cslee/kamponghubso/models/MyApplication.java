package com.example.cslee.kamponghubso.models;

import android.app.Application;

/**
 * Singleton class to hold data Created by TTH on 22/1/2018.
 */

public class MyApplication extends Application {

    public MyApplication()
    {}

    private static String userEmail;
    private static String userName;
    private static String uid;
    private String shopName;

    private String userZone;

    public String getShopName()
    {return shopName;}

    public void setShopName(String shopName)
    {this.shopName = shopName;}

    public String getEmail()
    {return userEmail;}

    public void setEmail(String email)
    {this.userEmail = email;}

    public String getUserName()
    {return userName;}

    public void setUserName (String userName)
    {this.userName = userName;}

    public String getUID()
    {return uid;}

    public void setUID(String uidVal)
    {this.uid = uidVal;}

    public String getUserZone() {
        return userZone;
    }

    public void setUserZone(String userZone) {
        this.userZone = userZone;
    }

}
