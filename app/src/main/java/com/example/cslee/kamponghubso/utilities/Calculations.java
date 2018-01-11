package com.example.cslee.kamponghubso.utilities;


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
}
