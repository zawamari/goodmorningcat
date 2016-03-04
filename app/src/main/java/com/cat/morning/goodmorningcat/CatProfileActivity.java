package com.cat.morning.goodmorningcat;

import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by imarie on 16/01/24.
 */
public class CatProfileActivity extends AppCompatActivity {

    private SoundPool mSoundPool;
    private int mSoundId;
    private int mSoundIdKittin;
    private int mSoundIdUnari;

    @Override
    protected void onResume() {
        super.onResume();
        // 予め音声データを読み込む
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(getApplicationContext(), R.raw.cat1, 0);
        mSoundIdKittin = mSoundPool.load(getApplicationContext(), R.raw.cat2, 0);
        mSoundIdUnari = mSoundPool.load(getApplicationContext(), R.raw.cat_unari, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile);

        findViewById(R.id.ivSpeaker1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundPool.play(
                        mSoundId,// 再生したいファイルをloadした時の戻り値,
                        1.0F,    // 左のスピーカーからの再生音量。(0.0〜1.0)
                        1.0F,    // 右のスピーカーからの再生音量。(0.0〜1.0)
                        0,       // プライオリティ（0が一番優先度が高い）
                        0,       // ループ回数（-1の場合は無限にループ、0の場合はループしない）
                        1.0F     // 再生速度（0.5〜2.0：0.5倍から2倍の速度まで設定できる）
                );
            }
        });
        findViewById(R.id.ivSpeaker2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundPool.play(mSoundIdKittin, 1.0F, 1.0F, 0, 0, 1.0F);
            }
        });
        findViewById(R.id.ivSpeaker3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundPool.play(mSoundIdUnari, 1.0F, 1.0F, 0, 0, 1.0F);
            }
        });

    }

}
