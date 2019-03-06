package com.rent.rentmanagement.renttest;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rent.rentmanagement.renttest.Services.SendTokenOwnerService;
import com.rent.rentmanagement.renttest.Tenants.Services.SendTokenTenantService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{
    public static final String TOKEN_BROADCAST="fcmTokenBroadcast";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("token", "Refreshed token: " + refreshedToken);
        LoginActivity.sharedPreferences.edit().putString("nToken",refreshedToken).apply();
        if(LoginActivity.sharedPreferences.getBoolean("isLoggedIn",false)==true) {
            if (LoginActivity.sharedPreferences.getBoolean("isOwner", false) == true) {
                sendTokenOwner();
            } else
                sendTokenTenant();
        }

       // getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
    }
    void sendTokenOwner()
    {
        Log.i("its Working","Brio");
        Intent i=new Intent(getApplicationContext(), SendTokenOwnerService.class);
        startService(i);
    }
    void sendTokenTenant()
    {
        Intent i=new Intent(getApplicationContext(), SendTokenTenantService.class);
        startService(i);
    }
}
