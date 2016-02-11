package com.cat.morning.goodmorningcat.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.cat.morning.goodmorningcat.MainActivity;
import com.cat.morning.goodmorningcat.R;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by imarie on 16/01/31.
 */
public class TestShakeActivity extends Activity implements SensorEventListener {
    private SensorManager manager;
    private TextView values;

    private int totalCount;
    private ProgressBar pb;

        /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_shake);

        values = (TextView)findViewById(R.id.value_id);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        pb = (ProgressBar)findViewById(R.id.ProgressBar01);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        // Listenerの登録解除
        manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Listenerの登録
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            String str = "加速度センサー値:"
            + "\nX軸:" + event.values[SensorManager.DATA_X]
            + "\nY軸:" + event.values[SensorManager.DATA_Y]
            + "\nZ軸:" + event.values[SensorManager.DATA_Z];
            values.setText(str);
        }

        if ((int)event.values[SensorManager.DATA_X] > 0) { // 0以上だったら加算する
            totalCount += (int) event.values[SensorManager.DATA_X];
            pb.setProgress(totalCount);

            if (totalCount > 1500) {
                manager.unregisterListener(this);
                findViewById(R.id.tvClear).setVisibility(View.VISIBLE);

                TimerTask task = new TimerTask() {
                    public void run() {
                        Intent intent = new Intent(TestShakeActivity.this, MainActivity.class);
                        startActivityForResult(intent, 0);
                        TestShakeActivity.this.finish();
                    }
                };

                Timer timer = new Timer();
                timer.schedule(task, 3000); // 1秒後にTOPに戻る

            }
        }
        Log.d("test", Integer.toString(totalCount));
    }
}
