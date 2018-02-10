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
        String first2digits = postalCode.substring(0,2);
        switch (first2digits) {
            case "01":
                zone="South";
                break;
            case "02":
                zone="South";
                break;
            case "03":
                zone="South";
                break;
            case "04":
                zone="South";
                break;
            case "05":
                zone="South";
                break;
            case "06":
                zone="South";
                break;
            case "07":
                zone="South";
                break;
            case "08":
                zone="South";
                break;
            case "09":
                zone="South";
                break;
            case "10":
                zone="South";
                break;
            case "11":
                zone="South";
                break;
            case "12":
                zone="South";
                break;
            case "13":
                zone="South";
                break;
            case "14":
                zone="South";
                break;
            case "15":
                zone="South";
                break;
            case "16":
                zone="South";
                break;
            case "17":
                zone="Central";
                break;
            case "18":
                zone="Central";
                break;
            case "19":
                zone="Central";
                break;
            case "20":
                zone="Central";
                break;
            case "21":
                zone="Central";
                break;
            case "22":
                zone="Central";
                break;
            case "23":
                zone="Central";
                break;
            case "24":
                zone="Central";
                break;
            case "25":
                zone="Central";
                break;
            case "26":
                zone="Central";
                break;
            case "27":
                zone="Central";
                break;
            case "28":
                zone="Central";
                break;
            case "29":
                zone="Central";
                break;
            case "30":
                zone="Central";
                break;
            case "31":
                zone="Central";
                break;
            case "32":
                zone="Central";
                break;
            case "33":
                zone="Central";
                break;
            case "34":
                zone="Central";
                break;
            case "35":
                zone="Central";
                break;
            case "36":
                zone="Central";
                break;
            case "37":
                zone="Central";
                break;
            case "38":
                zone="Central";
                break;
            case "39":
                zone="Central";
                break;
            case "40":
                zone="Central";
                break;
            case "41":
                zone="Central";
                break;
            case "42":
                zone="East";
                break;
            case "43":
                zone="East";
                break;
            case "44":
                zone="East";
                break;
            case "45":
                zone="East";
                break;
            case "46":
                zone="East";
                break;
            case "47":
                zone="East";
                break;
            case "48":
                zone="East";
                break;
            case "49":
                zone="East";
                break;
            case "50":
                zone="East";
                break;
            case "51":
                zone="East";
                break;
            case "52":
                zone="East";
                break;
            case "53":
                zone="Central";
                break;
            case "54":
                zone="Central";
                break;
            case "55":
                zone="Central";
                break;
            case "56":
                zone="Central";
                break;
            case "57":
                zone="Central";
                break;
            case "58":
                zone="West";
                break;
            case "59":
                zone="West";
                break;
            case "60":
                zone="West";
                break;
            case "61":
                zone="West";
                break;
            case "62":
                zone="West";
                break;
            case "63":
                zone="West";
                break;
            case "64":
                zone="West";
                break;
            case "65":
                zone="West";
                break;
            case "66":
                zone="West";
                break;
            case "67":
                zone="West";
                break;
            case "68":
                zone="West";
                break;
            case "69":
                zone="West";
                break;
            case "70":
                zone="West";
                break;
            case "71":
                zone="West";
                break;
            case "72":
                zone="North";
                break;
            case "73":
                zone="North";
                break;
/*            case "74":
                zone="North";
                break;*/
            case "75":
                zone="North";
                break;
            case "76":
                zone="North";
                break;
            case "77":
                zone="North";
                break;
            case "78":
                zone="North";
                break;
            case "79":
                zone="North";
                break;
            case "80":
                zone="North";
                break;
            case "81":
                zone="East";
                break;
            case "82":
                zone="Central";
                break;
        }

        return zone;
    }
    public static String getLatFromPostal(String postalCode){
        String latitude ="";
        return latitude;
    }
    public static String getLongFromPostal(String postalCode){
        String longtitude ="";
        return longtitude;
    }
    public static boolean checkPostalValid(String postalCode){
      boolean valid = false;

      return valid;
    }

}
