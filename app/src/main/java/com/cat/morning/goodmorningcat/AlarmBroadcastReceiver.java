package com.cat.morning.goodmorningcat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.cat.morning.goodmorningcat.test.TestSwipeActivity;

/**
 * Created by inai_marie on 2016/02/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    Context context;

    @Override   // データを受信した
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        Toast.makeText(context, "ALARM onReceive", Toast.LENGTH_SHORT).show();

        int bid = intent.getIntExtra("intentId",0);

        Intent intent2 = new Intent(context, TestSwipeActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, bid, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.cat)
                .setTicker("時間です")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("TestAlarm")
                .setContentText("時間になりました")
                // 音、バイブレート、LEDで通知
                .setDefaults(Notification.DEFAULT_ALL)
                // 通知をタップした時にMainActivityを立ち上げる
                .setContentIntent(pendingIntent)
                .build();

        // 古い通知を削除
        notificationManager.cancelAll();
        // 通知
        notificationManager.notify(R.string.app_name, notification);
    }
}
