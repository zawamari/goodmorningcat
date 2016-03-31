package com.cat.morning.goodmorningcat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by inai_marie on 2016/03/30.
 */
public class GCMReceiver extends io.repro.android.GCMReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Implement procedures unique to the application here
        Bundle extras = intent.getExtras();
        String message = "";
        if (extras.containsKey("message")) {
            message = extras.getString("message");
        }

        // Get notification ID
        String notificationId = "";
        if (extras.containsKey(io.repro.android.GCMReceiver.NOTIFICATION_ID_KEY)) {
            notificationId = extras.getString(io.repro.android.GCMReceiver.NOTIFICATION_ID_KEY);
        }

        final Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.cat)
                .setAutoCancel(true);
        // TODO: メッセージが表示されないことがある。

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("repro-notification-id", notificationId);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.getNotification());

    }
}
