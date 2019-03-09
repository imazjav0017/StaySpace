package com.mansa.StaySpace;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mansa.StaySpace.Owner.MainActivity;
import com.mansa.StaySpace.Owner.StudentActivity;

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
        Notification.Builder notf;
        notf=new Notification.Builder(getApplicationContext());
        notf.setAutoCancel(true);
        notf.setTicker("Test Notification");
        String id,description;
        CharSequence name;
        int importance;
       // notf.setVibrate(new long[]{1000,1000,1000,1000,1000});
        //notf.setLights(Color.RED,3000,3000);
        //notf.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notf.setWhen(System.currentTimeMillis());
        notf.setContentTitle(data.get("title"));
        notf.setContentText(data.get("body"));
        Notification.BigTextStyle bigTextStyle=new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(data.get("title"));
        bigTextStyle.bigText(data.get("body"));
        notf.setStyle(bigTextStyle);
        Intent i;
        PendingIntent pi;
        NotificationManager nm;
        Bundle b=new Bundle();
        NotificationChannel mChannel = null;
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
                 id = "Check-in Request";
// The user-visible name of the channel.
                 name = "Check-in Request";

// The user-visible description of the channel.
                 description = "It Shows The CheckIn Request For Owner";

                 importance = NotificationManager.IMPORTANCE_HIGH;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, name,importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),att);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    nm.createNotificationChannel(mChannel);
                    notf.setChannelId(id);
                }
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
                 id = "Check-in Response";
// The user-visible name of the channel.
                 name = "Check-in Response";

// The user-visible description of the channel.
                 description = "Response of room request for tenant";

                 importance = NotificationManager.IMPORTANCE_HIGH;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    nm.createNotificationChannel(mChannel);
                    notf.setChannelId(id);
                }
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
                id = "Payment";
// The user-visible name of the channel.
                name = "Payment";

// The user-visible description of the channel.
                description = "Payment Collection ";

                importance = NotificationManager.IMPORTANCE_HIGH;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    nm.createNotificationChannel(mChannel);
                    notf.setChannelId(id);
                }
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
                id = "Payment Due";
// The user-visible name of the channel.
                name = "Payment Due";

// The user-visible description of the channel.
                description = "Payment Due Reminder ";

                importance = NotificationManager.IMPORTANCE_HIGH;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    nm.createNotificationChannel(mChannel);
                    notf.setChannelId(id);
                }
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
                id = "Collect Rent";
// The user-visible name of the channel.
                name = "Collect Rent";

// The user-visible description of the channel.
                description = "Collect Rent Reminder ";

                importance = NotificationManager.IMPORTANCE_HIGH;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    nm.createNotificationChannel(mChannel);
                    notf.setChannelId(id);
                }
                nm.notify(111113, notf.build());
                break;
            default:
                notf.setSmallIcon(R.drawable.ic_action_notifications);
                notf.setLargeIcon(bm);
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                id = "Other";
// The user-visible name of the channel.
                name = "Other";

// The user-visible description of the channel.
                description = "Other ";

                importance = NotificationManager.IMPORTANCE_HIGH;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, name, importance);
                    mChannel.setDescription(description);
                    mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    nm.createNotificationChannel(mChannel);
                    notf.setChannelId(id);
                }
                nm.notify(10012, notf.build());
        }

        }
    }
