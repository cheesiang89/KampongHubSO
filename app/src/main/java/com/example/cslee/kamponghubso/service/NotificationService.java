package com.example.cslee.kamponghubso.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cslee.kamponghubso.NavigationActivity;
import com.example.cslee.kamponghubso.utilities.MyNotificationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    public NotificationService() {
    }

    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }


}

