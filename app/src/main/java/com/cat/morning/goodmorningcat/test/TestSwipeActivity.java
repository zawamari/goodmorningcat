package com.cat.morning.goodmorningcat.test;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cat.morning.goodmorningcat.MainActivity;
import com.cat.morning.goodmorningcat.R;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by imarie on 16/01/31.
 */
public class TestSwipeActivity extends Activity {

    ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_swipe);

        pb = (ProgressBar)findViewById(R.id.ProgressBar01);

        // 音をならす
        final SoundPool mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        final int mSoundId = mSoundPool.load(getApplicationContext(), R.raw.cat1, 0);
        mSoundPool.play(
                mSoundId,// 再生したいファイルをloadした時の戻り値,
                1.0F,    // 左のスピーカーからの再生音量。(0.0〜1.0)
                1.0F,    // 右のスピーカーからの再生音量。(0.0〜1.0)
                0,       // プライオリティ（0が一番優先度が高い）
                -1,       // ループ回数（-1の場合は無限にループ、0の場合はループしない）
                1.0F     // 再生速度（0.5〜2.0：0.5倍から2倍の速度まで設定できる）
        );

        final View.OnTouchListener moving = new View.OnTouchListener() {

            private float downX;
            private float downY;

            private int downLeftMargin;
            private int downTopMargin;

            private int totalMove;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final ViewGroup.MarginLayoutParams param =
                        (ViewGroup.MarginLayoutParams)v.getLayoutParams();

                if( event.getAction() == MotionEvent.ACTION_DOWN ){

                    downX = event.getRawX();
                    downY = event.getRawY();

                    downLeftMargin = param.leftMargin;
                    downTopMargin = param.topMargin;

                    return true;
                } else if( event.getAction() == MotionEvent.ACTION_MOVE){

                    // TODO:この値だけ取得すると、タップして最初に左に移動したらマイナスの値になるので修正必要。
                    param.leftMargin = downLeftMargin + (int)(event.getRawX() - downX);
                    param.topMargin = downTopMargin + (int)(event.getRawY() - downY);

                    Log.d("test downLeftMargin", Integer.toString(downLeftMargin + (int)(event.getRawX() - downX)));
                    Log.d("test downTopMargin", Integer.toString(downTopMargin + (int) (event.getRawY() - downY)));

                    v.layout(param.leftMargin, param.topMargin, param.leftMargin + v.getWidth(), param.topMargin + v.getHeight());

                    totalMove = totalMove + (int)(event.getRawX() - downX); // 360000くらいまで行こう
                    pb.setProgress(totalMove);

                    if (totalMove > 180000) {
                        mSoundPool.stop(mSoundId);
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

    // TODO:backボタンを無効にする

}
