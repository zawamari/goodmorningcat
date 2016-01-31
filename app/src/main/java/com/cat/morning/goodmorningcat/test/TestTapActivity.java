package com.cat.morning.goodmorningcat.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cat.morning.goodmorningcat.MainActivity;
import com.cat.morning.goodmorningcat.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by inai_marie on 2016/01/26.
 */
public class TestTapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tap);

        final TextView tvCounter = (TextView)findViewById(R.id.tvCounter);

        ((Button)findViewById(R.id.btTap)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = Integer.parseInt(tvCounter.getText().toString());
                tvCounter.setText(Integer.toString(count + 1));


                if (count > 20) {
                    ((TextView) findViewById(R.id.tvClear)).setVisibility(View.VISIBLE);
                    ((ImageView) findViewById(R.id.ivClearImage)).setVisibility(View.VISIBLE);

                    TimerTask task = new TimerTask() {
                        public void run() {
                            Intent intent = new Intent(TestTapActivity.this, MainActivity.class);
                            startActivityForResult(intent, 0);
                            TestTapActivity.this.finish();
                        }
                    };

                    Timer timer = new Timer();
                    timer.schedule(task, 1000); // 1秒後にTOPに戻る
                }

            }
        });





    }
}
