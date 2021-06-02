package com.example.jonathan_gorovoy_android.classes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.renderscript.RenderScript;

import androidx.core.app.NotificationCompat;

import com.example.jonathan_gorovoy_android.ModifyDayActivity;
import com.example.jonathan_gorovoy_android.R;

public class ReminderCreator extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra("notificationVar");
        int notificationId = intent.getIntExtra("notificationId", 12120);
        notificationManager.notify(notificationId, notification);
    }
}
