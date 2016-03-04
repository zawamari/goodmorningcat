package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.cat.morning.goodmorningcat.test.TestSwipeActivity;

/**
 * Created by inai_marie on 2016/02/15.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    Context context;

    @Override   // アラームの時間になったら、まずここが動いて、指定のActivityを起動させる。
    public void onReceive(Context context, Intent intent) {

        this.context = context;

//        Toast.makeText(context, "ALARM きました！", Toast.LENGTH_SHORT).show();

        Intent morningIntent;

        morningIntent = new Intent(context, TestSwipeActivity.class);

        morningIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(morningIntent);


    }
}
