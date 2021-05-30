package com.example.todolist.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.todolist.R;
import com.example.todolist.activity.MainActivity;

public class MyReceiver extends BroadcastReceiver {
    final String CHANNEL_ID =
            "101";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("myAction") != null &&
                intent.getStringExtra("myAction").equals("mDoNotify")
                && intent.getStringExtra("Title")!=null
                && intent.getStringExtra("Description")!=null
                && intent.getStringExtra("id") != null){
            Log.e("Rev","rev");
            NotificationManager manager =
                    (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(
                        intent.getStringExtra("id"),
                        "Channel " + intent.getStringExtra("id")
                        ,
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("This is Channel 1");
                manager.createNotificationChannel(channel1);
            }
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context,CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                            .setContentTitle(intent.getStringExtra("Title"))
                            .setContentText(intent.getStringExtra("Description"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true);
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            i,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            builder.setContentIntent(pendingIntent);
            manager.notify(Integer.parseInt(intent.getStringExtra("id")), builder.build());
        }
    }
}