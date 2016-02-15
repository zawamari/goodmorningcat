package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.morning.goodmorningcat.test.TestFlingActivity;
import com.cat.morning.goodmorningcat.test.TestShakeActivity;
import com.cat.morning.goodmorningcat.test.TestSwipeActivity;
import com.cat.morning.goodmorningcat.test.TestTapActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int bid1 = 1;
    private static final int bid2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DB呼び出し
//        MyDBHelper dbHelper = new MyDBHelper( this );
//        SQLiteDatabase db = dbHelper.getWritableDatabase();

        LayoutInflater inflater = getLayoutInflater();

        String[] catArray = {"みけこ", "マイケル", "太郎", "ザビエル"};

        LinearLayout llCallList = (LinearLayout)findViewById(R.id.llCallList);

        for (int i = 0; i < catArray.length; i++) {
            LinearLayout callListCell = (LinearLayout)inflater.inflate(R.layout.part_call_list, llCallList, false);
            callListCell.setId(i);

            ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[i]);
//            ((TextView)callListCell.findViewById(R.id.tvAlertTime)).setText("")
            if (catArray[i].equals("みけこ")) {
                ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.mikeko);
            } else if(catArray[i].equals("マイケル")) {
                ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.michael);
            } else if (catArray[i].equals("太郎")) {
                ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.taro);
            } else {
                ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.zabier);
            }
            llCallList.addView(callListCell);

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 2016);
                calendar.set(Calendar.MONTH, 1);// 7=>8月 Calendar.MONTH は0から始まる
                calendar.set(Calendar.DATE, 15);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 40 );
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                intent.putExtra("intentId", 2);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), bid2, intent, 0);

                // アラームをセットする
                AlarmManager am = (AlarmManager)MainActivity.this.getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

                Toast.makeText(getApplicationContext(), "Set Alarm ", Toast.LENGTH_SHORT).show();



                Snackbar.make(view, "Test アラームセット完了。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // アラームの取り消し
        LinearLayout btn2 = (LinearLayout)this.findViewById(R.id.llNextCall);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent indent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), bid1, indent, 0);

                // アラームを解除する
                AlarmManager am = (AlarmManager)MainActivity.this.getSystemService(ALARM_SERVICE);
                am.cancel(pending);
                Toast.makeText(getApplicationContext(), "Cancel ALARM 1", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            return true;
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(this, CatProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AlertSettingActivity.class);
            startActivity(intent);
        } else if ( id == R.id.test_tap) {
            Intent intent = new Intent(this, TestTapActivity.class);
            startActivity(intent);
        } else if ( id == R.id.test_shake) {
            Intent intent = new Intent(this, TestShakeActivity.class);
            startActivity(intent);
        } else if ( id == R.id.test_swipe) {
            Intent intent = new Intent(this, TestSwipeActivity.class);
            startActivity(intent);
        } else if ( id == R.id.test_fling) {
            Intent intent = new Intent(this, TestFlingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}