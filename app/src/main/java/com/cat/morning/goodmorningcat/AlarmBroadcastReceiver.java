package com.cat.morning.goodmorningcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cat.morning.goodmorningcat.test.TestSwipeActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    Context context;

    @Override   // アラームの時間になったら、まずここが動いて、指定のActivityを起動させる。
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        Bundle bundle = intent.getExtras();
        bundle.getInt("dbId");

        Intent morningIntent;

        morningIntent = new Intent(context, TestSwipeActivity.class);

        morningIntent.putExtra("dbId", bundle.getInt("dbId"));
        morningIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(morningIntent);


    }
}
