package com.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class AlarmReceiver extends BroadcastReceiver {
    private String CHANNEL_ID = "app_notification_channel";
    private String CHANNEL_DESC = "WGU Alert Channel";

    /**
     *
     * @param context
     * @param intent
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //retrieve stored(in intent) alert data
        String mNotificationTitle = intent.getStringExtra("notifyTitle");
        String mNotificationContent = intent.getStringExtra("notifyContent");
        int counter = intent.getIntExtra("counter",1);
        Intent resultIntent = new Intent(context, MainActivity.class);
        //build artificial TaskStack to retain up functionality on app bar
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        //alert build
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID);

        mBuilder.setSmallIcon(R.drawable.ic_assessments);
        mBuilder.setContentTitle(mNotificationTitle);
        mBuilder.setContentText(mNotificationContent);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //build alert channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
        mNotificationManager.createNotificationChannel(channel);
        //trigger the alert
        mNotificationManager.notify(counter, mBuilder.build());
    }
}