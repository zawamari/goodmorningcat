package com.cat.morning.goodmorningcat.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.cat.morning.goodmorningcat.MainActivity;
import com.cat.morning.goodmorningcat.MyDBHelper;
import com.cat.morning.goodmorningcat.R;

import java.util.Timer;
import java.util.TimerTask;

public class TestSwipeActivity extends Activity {

    private ProgressBar pb;
    private MediaPlayer mp;
    private int dbId;
    private Vibrator vib;
    private long pattern[] = {100, 2000, 1000 }; // 0.1秒待って2秒振動,1秒停止
    private static final int ON = 0;
    private static final int OFF = 1;

    private int isVibrate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_swipe);

        Intent intent = getIntent();
        Log.d("test dbId", Integer.toString(intent.getIntExtra("dbId", 0)));

        dbId = intent.getIntExtra("dbId", 0);

        // スクリーンロックを解除する.権限が必要
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStart() {
        super.onStart();


        final SQLiteDatabase db = MyDBHelper.getInstance(TestSwipeActivity.this).getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT manner, vibrate, cat_type, status FROM alert_set_table WHERE id = ?", new String[]{Integer.toString(dbId)});

        if (cursor.moveToFirst()) {

            int isManner = cursor.getInt(0);
            isVibrate = cursor.getInt(1);



            // 音を鳴らす
            if (isManner == ON && mp == null) {
                mp = MediaPlayer.create(this, R.raw.cat1);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setVolume(1.0f, 1.0f); // 0.1f ~ 1.0f
                mp.setLooping(true);
                mp.start();
            }

            AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

            int maxVol = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int vol = manager.getStreamVolume(AudioManager.STREAM_MUSIC);


            // バイブレーション

            if (isVibrate == ON) {
                vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(pattern, 0); // 設定したパターンを繰り返す
            }

            pb = (ProgressBar) findViewById(R.id.ProgressBar01);

            final View.OnTouchListener moving = new View.OnTouchListener() {

                private float downX;
                private float downY;

                private int downLeftMargin;
                private int downTopMargin;

                private int totalMove;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    final ViewGroup.MarginLayoutParams param =
                            (ViewGroup.MarginLayoutParams) v.getLayoutParams();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                        downX = event.getRawX();
                        downY = event.getRawY();

                        downLeftMargin = param.leftMargin;
                        downTopMargin = param.topMargin;

                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                        // TODO:この値だけ取得すると、タップして最初に左に移動したらマイナスの値になるので修正必要。
                        param.leftMargin = downLeftMargin + (int) (event.getRawX() - downX);
                        param.topMargin = downTopMargin + (int) (event.getRawY() - downY);

                        Log.d("test downLeftMargin", Integer.toString(downLeftMargin + (int) (event.getRawX() - downX)));
                        Log.d("test downTopMargin", Integer.toString(downTopMargin + (int) (event.getRawY() - downY)));

                        v.layout(param.leftMargin, param.topMargin, param.leftMargin + v.getWidth(), param.topMargin + v.getHeight());

                        totalMove = totalMove + (int) (event.getRawX() - downX); // 360000くらいまで行こう
                        pb.setProgress(totalMove);

                        if (totalMove > 180000) {
//                        mp.stop();
//                        mp.release();
                            findViewById(R.id.tvClear).setVisibility(View.VISIBLE);

                            TimerTask task = new TimerTask() {
                                public void run() {
                                    Intent intent = new Intent(TestSwipeActivity.this, MainActivity.class);
                                    startActivityForResult(intent, 0);
                                    TestSwipeActivity.this.finish();
                                }
                            };

                            Timer timer = new Timer();
                            timer.schedule(task, 1000); // 1秒後にTOPに戻る

                        }

                        Log.d("test TotalMove", Integer.toString(totalMove));

                        return true;
                    }

                    return false;
                }
            };

            findViewById(R.id.ivHand).setOnTouchListener(moving);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndRelaese();
    }

    private void stopAndRelaese() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        if (isVibrate == ON) vib.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        alarmNowText = (TextView) findViewById(R.id.alarm_now_time);
//        handler.sendEmptyMessage(WHAT);
        // mam.stopAlarm();
    }


    // TODO:backボタンを無効にする?

}
