package com.cat.morning.goodmorningcat.test;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.cat.morning.goodmorningcat.R;

import java.util.List;
import java.util.Objects;

/**
 * Created by imarie on 16/01/31.
 */
public class TestShakeActivity extends Activity implements SensorEventListener {
    //センサーマネージャー
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    Float b_value;
    Long b_time;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_shake);

        // センサーマネージャを獲得する
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // マネージャから加速度センサーオブジェクトを取得
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
        // 加速度センサの場合、以下の処理を実行
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            StringBuilder builder = new StringBuilder();

            // 数値の単位はm/s^2
            // X軸
            float x = event.values[0];
            // Y軸
            float y = event.values[1];
            // Z軸
            float z = event.values[2];


            Float c_value = event.values[0] + event.values[1];
            Long c_time = System.currentTimeMillis();

//            これ↓エラーに成る。
//            b_value = c_value + b_value;

//            if (b_value > 1000) {
//                Float speed = Math.abs(c_value - b_value) / diff * 1000;
//                if (speed > 30){
//                    Toast.makeText(TestShakeActivity.this, "シェイク",Toast.LENGTH_LONG).show();
//                }
//                b_time = c_time;
//                b_value = c_value;
//            }
            builder.append("X : " + (b_value) + "\n");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 200msに一度SensorEventを観測するリスナを登録
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 非アクティブ時にSensorEventをとらないようにリスナの登録解除
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        // Listenerの登録解除
        mSensorManager.unregisterListener(this);
    }

}
