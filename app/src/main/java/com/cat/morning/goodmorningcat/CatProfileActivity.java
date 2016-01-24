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

    @Override
    protected void onResume() {
        super.onResume();
        // 予め音声データを読み込む
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(getApplicationContext(), R.raw.cat1, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile);

        ((ImageView)findViewById(R.id.ivSpeaker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
            }
        });
    }

}
