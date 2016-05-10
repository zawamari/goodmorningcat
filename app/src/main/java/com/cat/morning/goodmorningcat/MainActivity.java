package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.cat.morning.goodmorningcat.util.AlermSettingUtils;

import java.util.ArrayList;

import io.repro.android.Repro;

public class MainActivity extends AppCompatActivity {

    LinearLayout llCallList;
    LayoutInflater inflater;
    String[] catArray = {"みけこ"};
    String nowTime;
    ArrayList setTimeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Repro
         */
        // Setup Repro
        Repro.setup("c5de83ed-5b81-42f3-83c8-bc92410147c0");
        Repro.startRecording();
        Repro.setUserID("y6w10q39");
        Repro.track("app test");

        // アプリ内メッセージを出す
        Repro.disableInAppMessageOnActive();
        Repro.setup("a1e6be89-3139-4814-bf5c-494634ea7cb5");
        Repro.showInAppMessage(this);


        // push通知設定
        Repro.setup("c5de83ed-5b81-42f3-83c8-bc92410147c0");
        Repro.enablePushNotification("662649025058");


        inflater = getLayoutInflater();

        llCallList = (LinearLayout)findViewById(R.id.llCallList);

        Log.d("test now time is ", AlermSettingUtils.getNowTime());
        nowTime = AlermSettingUtils.getNowTime();

        // 一度全てのアラームを解除
        canselAllAlarm();

        // アラームリストを表示、この中でアラームを全てセット
        showAlarmList();


        checkNextCall(setTimeArray);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlermSettingActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        findViewById(R.id.llNoSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlermSettingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ivTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.ivTimeSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlermSettingActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ivCatList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CatProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("repro-notification-id")) {
            Repro.trackNotificationOpened(extras.getString("repro-notification-id"));
        }
    }


    /*
     * 一覧を表示する
     */
    public void showAlarmList() {
        // DB呼び出し
        final SQLiteDatabase db = MyDBHelper.getInstance(MainActivity.this).getWritableDatabase();

        final Cursor cursor = db.rawQuery("SELECT week, time, cat_type, status, id FROM alert_set_table ORDER BY id", null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                final LinearLayout callListCell = (LinearLayout)inflater.inflate(R.layout.part_call_list, llCallList, false);
                callListCell.setId(i);
                final int count = i + 1;

                String dbWeed = cursor.getString(0);
                final String dbTime = cursor.getString(1);
                int dbCatType = cursor.getInt(2);
                int dbStatus = cursor.getInt(3);
                final int dbId = cursor.getInt(4);

                switch (dbCatType) {
                    case 0:
                        // 名前
                        ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[0]);
                        // 画像
                        ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.taro);
                        break;
                }

                // 時間
                ((TextView)callListCell.findViewById(R.id.tvAlertTime)).setText(dbTime);

                // on:off
                Switch onOff = (Switch)callListCell.findViewById(R.id.swStatus);
                switch (dbStatus) {
                    case 0:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(true);
                        setAlarm(dbId, dbTime);
                        setTimeArray.add(dbTime);
                        break;
                    case 1:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(false);
                        canselAlarm(dbId);
                        break;
                }

                onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        db.execSQL("UPDATE alert_set_table SET status = ? WHERE id = ?", new Integer[]{(isChecked ? 0 : 1), count});

                        // off->onにしたら
                        if (isChecked) {
                            setAlarm(dbId, dbTime);
                        } else {
                            canselAlarm(dbId);
                        }
                    }
                });
                llCallList.addView(callListCell);

                ((ImageView)callListCell.findViewById(R.id.ivTrash)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog = new Dialog(MainActivity.this);
                        // タイトル非表示
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        // フルスクリーン
                        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                        dialog.setContentView(R.layout.dialog_alarm_delete);

                        // OK ボタンのリスナ
                        dialog.findViewById(R.id.bOk).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                deleteAlarm(db, dbId);
                                callListCell.removeAllViews();

                            }
                        });
                        // Close ボタンのリスナ
                        dialog.findViewById(R.id.bNo).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });


                cursor.moveToNext();
            }
        }
        cursor.close();

    }

    /*
     *
     */
    public void checkNextCall(ArrayList setTimeArray){

//        String[] setTimes = setTime.split(":", 0);
        String[] nowTimes = nowTime.split(":", 0);

//        if (Integer.parseInt(setTimes[0]) >= Integer.parseInt(nowTimes[0])) {
//            if (Integer.parseInt(setTimes[0]) > Integer.parseInt(nowTimes[0])) {
//                ((TextView)findViewById(R.id.tvNextCallTime)).setText(setTime);
//            }
//        }

    }


    /*
     * アラームを設定する
     */
    public void setAlarm(int alarmId, String time) {
        String[] times = time.split(":", 0);
        Log.d("test set", time);
        AlermSettingUtils.setAlarm(MainActivity.this, alarmId, times);
    }

    /*
     * 全アラームを解除する
     */
    public void canselAllAlarm() {
        final SQLiteDatabase db = MyDBHelper.getInstance(MainActivity.this).getWritableDatabase();

        final Cursor cursor = db.rawQuery("SELECT id FROM alert_set_table ORDER BY id", null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                final int alarmId = cursor.getInt(0);
                Intent indent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), alarmId, indent, 0);

                // アラームを解除する
                AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                am.cancel(pending);

                Log.d("test cancel", Integer.toString(alarmId));

                cursor.moveToNext();
            }
            cursor.close();
        }
    }

    /*
     * アラームを解除する
     */
    public void canselAlarm(int alarmId) {
        Intent indent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), alarmId, indent, 0);

        // アラームを解除する
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        am.cancel(pending);
    }

    /*
     * アラームを削除する
     */
    public void deleteAlarm(SQLiteDatabase db, int alarmId) {
        Log.d("test delete id is ", Integer.toString(alarmId));
        db.execSQL("DELETE FROM alert_set_table WHERE id = ?", new Integer[] {alarmId});
        // 再描画
        canselAlarm(alarmId);
        llCallList.removeAllViews();
        showAlarmList();
    }

}