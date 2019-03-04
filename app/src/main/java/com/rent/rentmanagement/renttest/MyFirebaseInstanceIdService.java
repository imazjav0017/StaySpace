package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{
    public static final String TOKEN_BROADCAST="fcmTokenBroadcast";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("token", "Refreshed token: " + refreshedToken);
        LoginActivity.sharedPreferences.edit().putString("nToken",refreshedToken).apply();
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
    }
}
