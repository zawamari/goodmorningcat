package com.cat.morning.goodmorningcat.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import com.cat.morning.goodmorningcat.R;

/**
 * Created by inai_marie on 2016/02/01.
 */
public class TestFlingActivity extends Activity {

    private TextView textView_x;
    private TextView textView_y;
    // 縦横フリック値トータル
    float x_total = 0;
    float y_total = 0;

    private GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fling);

        mGestureDetector = new GestureDetector(this, mOnGestureListener);
        textView_x = (TextView)findViewById(R.id.textView1);
        textView_y = (TextView)findViewById(R.id.textView2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private final GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // velocityでフリックの距離を取得
            float xxx = x_total - velocityX;
            float yyy = y_total - velocityY;
            x_total = xxx;
            y_total = yyy;


            // textViewで表示
            textView_x.setText( "横フリック値 : " + Integer.toString((int)velocityX) );
            textView_y.setText( "縦フリック値 : " + Integer.toString((int)velocityY) );



//            textView_x.setText( "横フリック値 total: " + x_total );
//            textView_y.setText( "縦フリック値 total: " + y_total );
            return true;
        }
    };
}
