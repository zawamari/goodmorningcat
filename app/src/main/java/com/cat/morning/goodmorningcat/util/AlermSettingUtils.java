package com.cat.morning.goodmorningcat.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cat.morning.goodmorningcat.AlarmBroadcastReceiver;
import com.cat.morning.goodmorningcat.MyDBHelper;

import java.util.Calendar;

/**
 * Created by imarie on 16/04/04.
 */
public class AlermSettingUtils {

    public static void setAlerm(Activity activity, SQLiteDatabase db, String[] time){

        Cursor cs = db.rawQuery("SELECT MAX(id) FROM alert_set_table ", null);
        cs.moveToLast();

        int requestCode = cs.getInt(0); // pendingIntent登録用requestCode
        Log.d("test requestCode is ", Integer.toString(requestCode));
        cs.close();

        setAlarm(activity, requestCode, time);


    }

    public static void setAlarm(Activity activity, int requestCode, String[] time) {
        // アラーム時間設定
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        // 設定した時刻をカレンダーに設定
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 過去だったら明日にする
        if(cal.getTimeInMillis() < System.currentTimeMillis()){
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }


        Intent intent = new Intent(activity, AlarmBroadcastReceiver.class);
        intent.putExtra("intentId", 2);

        // もし新規登録だったら、DBに登録されている数の次の数をrequestCodeにする. 登録済みのやつの編集なら、そのIDをrequestCodeに使用する
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, requestCode, intent, 0);

        // アラームをセットする
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(activity.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


    }


}
