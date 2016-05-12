package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import java.util.Calendar;
import io.repro.android.Repro;

public class MainActivity extends AppCompatActivity {

    LinearLayout llCallList;
    LayoutInflater inflater;
    String[] catArray = {"みけこ"};
    String nowTime;

    public static final int SET_ON = 0;
    public static final int SET_OFF = 1;

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

        nowTime = AlermSettingUtils.getNowTime();

        // 一度全てのアラームを解除
        canselAllAlarm();

        // アラームリストを表示、この中でアラームを全てセット
        showAlarmList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlermSettingActivity.class);
                startActivityForResult(intent, 0);
                MainActivity.this.finish();
            }
        });

        findViewById(R.id.llNoSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlermSettingActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
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
                MainActivity.this.finish();
            }
        });

        findViewById(R.id.ivCatList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CatListActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
//                AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//
//                int maxVol = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                int vol = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                Log.d("test", "最大音量：" + Integer.toString(maxVol) + ", 現在音量;" + Integer.toString(vol));
//
//                if (testflg) {
//                    mp = MediaPlayer.create(MainActivity.this, R.raw.cat1);
//                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mp.setVolume(1.0f, 1.0f); // 0.1f ~ 1.0f
//                    mp.setLooping(true);
//                    mp.start();
//                    testflg = false;
//                } else {
//                    mp.stop();
//                    testflg = true;
//                }
            }
        });
    }
    boolean testflg = true;
MediaPlayer mp;

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

        final Cursor cursor = db.rawQuery("SELECT week, time, cat_type, status, id FROM alert_set_table ORDER BY time", null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                final LinearLayout callListCell = (LinearLayout)inflater.inflate(R.layout.part_call_list, llCallList, false);
                callListCell.setId(i);

                // String dbWeed = cursor.getString(0);
                final String dbTime = cursor.getString(1);
                final int dbCatType = cursor.getInt(2);
                final int dbStatus = cursor.getInt(3);
                final int dbId = cursor.getInt(4);

                switch (dbCatType) {
                    case 0:
                        ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[0]); // 名前
                        ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageBitmap(
                                getCroppedBitmap(
                                        BitmapFactory.decodeResource(
                                                this.getResources(),
                                                R.mipmap.taro,
                                                null
                                        )
                                )
                        );
                        break;
                }

                // 時間
                ((TextView)callListCell.findViewById(R.id.tvAlertTime)).setText(dbTime);

                // on:off
                Switch onOff = (Switch)callListCell.findViewById(R.id.swStatus);
                switch (dbStatus) {
                    case SET_ON:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(true);
                        setAlarm(dbId, dbTime);
                        break;
                    case SET_OFF:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(false);
                        canselAlarm(dbId);
                        break;
                }

                onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        db.execSQL("UPDATE alert_set_table SET status = ? WHERE id = ?", new Integer[]{(isChecked ? 0 : 1), dbId});

                        if (isChecked) { // off->onにしたら
                            setAlarm(dbId, dbTime);
                        } else { // on->off
                            canselAlarm(dbId);
                        }
                        checkNextCall();
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

                callListCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("test time",dbTime);
                        Log.d("test cat",Integer.toString(dbCatType));
                        Log.d("test id",Integer.toString(dbId));

                        Intent intent = new Intent(getApplicationContext(), AlermSettingActivity.class);
                        intent.putExtra("id", dbId);
                        startActivity(intent);

                    }
                });

                cursor.moveToNext();
            }
        }

        // 次に呼ばれる時間を決める
        checkNextCall();

        cursor.close();

    }

    /*
     *
     */
    public void checkNextCall(){

        final SQLiteDatabase db = MyDBHelper.getInstance(MainActivity.this).getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT week, time, cat_type, status, id FROM alert_set_table ORDER BY time", null);

        // 現在時刻を取得
        String[] nowTimes = nowTime.split(":", 0);
        Calendar nowCal = Calendar.getInstance();
        nowCal.set(2016, 0, 1, Integer.parseInt(nowTimes[0]), Integer.parseInt(nowTimes[1]), 00);

        String nearTime = "-- : --";

        boolean setTimeFlg = false;

        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {

            if (!setTimeFlg) {

                if (cursor.getInt(3) ==  SET_OFF ) {
                    cursor.moveToNext();
                    continue;
                }

                String[] setTimes = cursor.getString(1).split(":", 0);

                Calendar setTimeCal = Calendar.getInstance();
                setTimeCal.set(2016, 0, 1, Integer.parseInt(setTimes[0]), Integer.parseInt(setTimes[1]), 00);


                // 2つの日時を比較
                int diff = setTimeCal.compareTo(nowCal);
                if (diff > 0) {
                    nearTime = cursor.getString(1);
                    setTimeFlg = !setTimeFlg;
                    break;
                }
            }
            cursor.moveToNext();
        }

        // もし未来の予定がなかったら、いちばんon担っている早い時刻をセットする
        if (!setTimeFlg) {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.getInt(3) == SET_OFF) {
                        cursor.moveToNext();
                        continue;
                    }
                    nearTime = cursor.getString(1);
                    break;
                }
            }
        }
        cursor.close();
        ((TextView)findViewById(R.id.tvNextCallTime)).setText(nearTime);
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

                Log.d("test all cancel", Integer.toString(alarmId));

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
        db.execSQL("DELETE FROM alert_set_table WHERE id = ?", new Integer[] {alarmId});
        // 再描画
        canselAlarm(alarmId);
        llCallList.removeAllViews();
        showAlarmList();
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {

        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();

        final Rect rect   = new Rect(0, 0, width, height);
        final RectF rectf = new RectF(0, 0, width, height);

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawRoundRect(rectf, width / 2, height / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }


}