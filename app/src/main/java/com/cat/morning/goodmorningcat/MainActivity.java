package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
import com.squareup.picasso.Transformation;

import io.repro.android.Repro;

public class MainActivity extends AppCompatActivity {

    LinearLayout llCallList;
    LayoutInflater inflater;
    String[] catArray = {"みけこ"};

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

        showAlarmList();


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

                Log.d("test week", cursor.getString(0));
                String dbWeed = cursor.getString(0);

                Log.d("test time", cursor.getString(1));
                final String dbTime = cursor.getString(1);

                Log.d("test cat_type", cursor.getString(2));
                int dbCatType = cursor.getInt(2);

                Log.d("test status", cursor.getString(3));
                int dbStatus = cursor.getInt(3);

                Log.d("test id", cursor.getString(4));
                final int dbId = cursor.getInt(4);

                Log.d("test ", "---------------");

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
                        break;
                    case 1:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(false);
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
                        Log.d("test update switch is ", dbTime);
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
     * アラームを設定する
     */
    public void setAlarm(int alarmId, String time) {
        String[] times = time.split(":", 0);
        AlermSettingUtils.setAlarm(MainActivity.this, alarmId, times);
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

//    public class CropSquareTransformation implements Transformation {
//        @Override public Bitmap transform(Bitmap source) {
//            int size = Math.min(source.getWidth(), source.getHeight());
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
//            if (result != source) {
//                source.recycle();
//            }
//            return result;
//        }
//
//        @Override public String key() { return "square()"; }
//    }
}