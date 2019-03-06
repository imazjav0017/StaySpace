package com.rent.rentmanagement.renttest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rent.rentmanagement.renttest.Owner.MainActivity;
import com.rent.rentmanagement.renttest.Owner.StudentActivity;

import java.util.Map;

public class FirebaseRecieveNotificationService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        Log.i("NotificationFrom","onRecieve");

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

            //Log.i("Notification", "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.i("Notification", "Message data payload: " + remoteMessage.getData());
                Map<String, String> data = remoteMessage.getData();
                Log.i("NotificationBody",data.get("body"));
                Log.i("NotificationTitle",data.get("title"));
                showNotification(data);
           /* if (/* Check if data needs to be processed by long running job true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.i("Notification", "Message Notification Body: " + remoteMessage.getNotification().getBody());
                Log.i("Notification","Title:"+remoteMessage.getNotification().getTitle());
                Log.i("Notification:","Data"+remoteMessage.getData());
               // showNotification(remoteMessage);
            }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    void showNotification(Map<String,String>data)
    {
        Bitmap bm=BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        NotificationCompat.Builder notf;
        notf=new NotificationCompat.Builder(getApplicationContext());
        notf.setAutoCancel(true);
        notf.setTicker("Test Notification");
        notf.setVibrate(new long[]{1000,1000,1000,1000,1000});
        notf.setLights(Color.RED,3000,3000);
        notf.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notf.setWhen(System.currentTimeMillis());
        notf.setContentTitle(data.get("title"));
        notf.setContentText(data.get("body"));
        NotificationCompat.BigTextStyle bigTextStyle=new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(data.get("title"));
        bigTextStyle.bigText(data.get("body"));
        notf.setStyle(bigTextStyle);
        Intent i;
        PendingIntent pi;
        NotificationManager nm;
        Bundle b=new Bundle();
        switch (data.get("title")) {
            case "Check-in Request":
                i = new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                b.putString("title","Check-in Request");
                i.putExtras(b);
                pi = PendingIntent.getActivity(this, 0, i,PendingIntent.FLAG_ONE_SHOT);
                notf.setSmallIcon(R.drawable.ic_action_add_person);
                notf.setLargeIcon(bm);
                notf.setContentIntent(pi);
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(10000, notf.build());
                break;
            case "Check-in Response":
                i = new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                b.putString("title","Check-in Response");
                i.putExtras(b);
                pi = PendingIntent.getActivity(getApplicationContext(), 0, i,PendingIntent.FLAG_ONE_SHOT);
                notf.setContentIntent(pi);
                notf.setSmallIcon(R.drawable.ic_action_notifications);
                notf.setLargeIcon(bm);
                nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(111110, notf.build());
                break;
            case "Payment":
                i = new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                b.putString("title","Payment");
                i.putExtras(b);
                pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
                notf.setContentIntent(pi);
                notf.setSmallIcon(R.drawable.ic_action_payment);
                notf.setLargeIcon(bm);
                nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(111111, notf.build());
                break;
            case "Payment Due":
                i = new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                b.putString("title","Payment Due");
                i.putExtras(b);
                pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
                notf.setContentIntent(pi);
                notf.setSmallIcon(R.drawable.ic_action_payment);
                notf.setLargeIcon(bm);
                nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(111112, notf.build());
                break;
            case "Collect Rent":
                i = new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                b.putString("title","Collect Rent");
                i.putExtras(b);
                pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
                notf.setContentIntent(pi);
                notf.setSmallIcon(R.drawable.ic_action_payment);
                notf.setLargeIcon(bm);
                nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(111113, notf.build());
                break;
            default:
                notf.setSmallIcon(R.drawable.ic_action_notifications);
                notf.setLargeIcon(bm);
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(10012, notf.build());
        }

        }
    }
