package com.aosama.it.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.aosama.it.constants.Constants;
import com.aosama.it.utiles.MyConfig;
import com.aosama.it.utiles.PreferenceProcessor;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        PreferenceProcessor.getInstance(this).setStr(MyConfig.MyPrefs.FIREBASE_TOKEN, s);
        Log.d(TAG, "Refreshed token: " + s);
    }
}
