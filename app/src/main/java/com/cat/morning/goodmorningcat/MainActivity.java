package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.morning.goodmorningcat.test.TestFlingActivity;
import com.cat.morning.goodmorningcat.test.TestShakeActivity;
import com.cat.morning.goodmorningcat.test.TestSwipeActivity;
import com.cat.morning.goodmorningcat.test.TestTapActivity;

import java.util.ArrayList;
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


        LayoutInflater inflater = getLayoutInflater();

        String[] catArray = {"太郎", "ザビエル", "みけこ", "マイケル" };

        LinearLayout llCallList = (LinearLayout)findViewById(R.id.llCallList);

//        ListView listView = (ListView)findViewById(R.id.lvCallList);
//        ArrayList<CallItem> data = new ArrayList<CallItem>();
//        Adapter adapter = new CallAdapter(this, data, R.layout.part_call_list);

        // DB呼び出し
        final SQLiteDatabase db = MyDBHelper.getInstance(MainActivity.this).getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT week, time, cat_type, status, id FROM alert_set_table ORDER BY id", null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                LinearLayout callListCell = (LinearLayout)inflater.inflate(R.layout.part_call_list, llCallList, false);
                callListCell.setId(i);
                final int count = i + 1;

                Log.d("test week", cursor.getString(0));
                Log.d("test time", cursor.getString(1));
                Log.d("test cat_type", cursor.getString(2));
                Log.d("test status", cursor.getString(3));
                Log.d("test id", cursor.getString(4));
                Log.d("test ", "---------------");
                switch (cursor.getInt(2)) {
                    case 0:
                        // 名前
                        ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[0]);
                        // 画像
                        ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.taro);
                        break;
                    case 1:
                        // 名前
                        ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[1]);
                        ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.zabier);
                        break;
                    case 2:
                        // 名前
                        ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[2]);
                        ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.mikeko);
                        break;
                    case 3:
                        // 名前
                        ((TextView)callListCell.findViewById(R.id.tvCatStaff)).setText(catArray[3]);
                        ((ImageView)callListCell.findViewById(R.id.ivCatImage)).setImageResource(R.mipmap.michael);
                        break;
                }


                // 時間
                ((TextView)callListCell.findViewById(R.id.tvAlertTime)).setText(cursor.getString(1));

                // on:off
                Switch onOff = (Switch)callListCell.findViewById(R.id.swStatus);
                switch (cursor.getInt(3)) {
                    case 0:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(true);
                        break;
                    case 1:
                        ((Switch)callListCell.findViewById(R.id.swStatus)).setChecked(false);
                        break;
                }

                onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        db.execSQL("UPDATE alert_set_table SET status = ? WHERE id = ?", new Integer[] {(isChecked ? 0 : 1), count});
                    }
                });
                llCallList.addView(callListCell);




                cursor.moveToNext();
            }
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


                SQLiteDatabase db = MyDBHelper.getInstance(MainActivity.this).getWritableDatabase();

                db.execSQL("INSERT INTO alert_set_table (week,time,cat_type, status) VALUES ('火曜','08:30', 1, 1)");



                Cursor cs = db.rawQuery("SELECT * FROM alert_set_table ", null);

                Log.d("test count is ", Integer.toString(cs.getCount()));

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