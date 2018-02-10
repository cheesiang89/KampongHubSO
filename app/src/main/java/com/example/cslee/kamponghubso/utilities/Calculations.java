package com.example.cslee.kamponghubso.utilities;


import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import android.util.Base64;
import android.graphics.BitmapFactory;

public class Calculations {

    public static String calcShopOpen(String timeStart, String timeEnd, String currentTime){
        //TODO: Calculate is shop open
       return "Open";

    }
    public static String calcTime(String timeStart, String timeEnd){
         if (timeStart.equals(timeEnd)){
            return "24hrs";
        }
        else{
            return (String.format("%s am to %s pm",timeStart,timeEnd));
        }
    }
    public static String calcDistance(String currentDistance, String shopCoordinates){
        //TODO: Need to calculate distance based on current coordinates and shop coordinates
        return("1km");
    }

    public static String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String base64string){
        byte[] decodedString = Base64.decode(base64string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }

    public static String calculateZone(String postalCode){
        String zone ="";
        return zone;
    }

}
