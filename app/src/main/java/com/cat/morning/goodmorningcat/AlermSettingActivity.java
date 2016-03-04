package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by imarie on 16/01/24.
 */
public class AlermSettingActivity extends AppCompatActivity {

    private static final int bid2 = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerm_setting);


        /*
         * 猫選択
         */
        Spinner spCatType = (Spinner)findViewById(R.id.spCatType);

        // TODO: ネコのタイプはローカルDBから取得するように変更する
        ArrayAdapter<String> catTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        catTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        catTypeAdapter.add("みけこ");
        catTypeAdapter.add("マイケル");
        catTypeAdapter.add("太郎");

        spCatType.setAdapter(catTypeAdapter);

        spCatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*
         * 曜日選択
         */
        Spinner spDayOfTheWeek = (Spinner)findViewById(R.id.spDayOfTheWeek);

        ArrayAdapter<String> weekTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        weekTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        weekTypeAdapter.add("月曜日");
        weekTypeAdapter.add("火曜日");
        weekTypeAdapter.add("水曜日");
        weekTypeAdapter.add("木曜日");
        weekTypeAdapter.add("金曜日");
        weekTypeAdapter.add("土曜日");
        weekTypeAdapter.add("日曜日");

        spDayOfTheWeek.setAdapter(weekTypeAdapter);

        spDayOfTheWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
         * 時間
         */

        final EditText etTime = (EditText)findViewById(R.id.etTime);
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(AlermSettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // 時間が設定されたときの処理
                        Log.v("Time", String.format("%02d:%02d", hourOfDay, minute));
                        etTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, 13, 0, true).show();
            }
        });



        /*
         * 登録ボタン
         */
        ((TextView) findViewById(R.id.tvSet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(((LinearLayout) findViewById(R.id.llView)), "Test アラームセット完了。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // DBに登録する

                SQLiteDatabase db = MyDBHelper.getInstance(AlermSettingActivity.this).getWritableDatabase();

                String etTime = ((EditText)findViewById(R.id.etTime)).getText().toString();


                db.execSQL("INSERT INTO alert_set_table (week,time,cat_type, status) VALUES ('火曜',?, 1, 1)", new String[] {etTime});

                Cursor cs = db.rawQuery("SELECT * FROM alert_set_table ", null);

                Log.d("test count is ", Integer.toString(cs.getCount()));


                /*  アラート登録時はONの状態なので、アラートをセットする。 */
                Calendar calendar = Calendar.getInstance();
                String[] time = etTime.split(":", 0);

//                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
//                calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));

                // Calendarを使って現在の時間をミリ秒で取得
                calendar.setTimeInMillis(System.currentTimeMillis());
                // 5秒後に設定
                calendar.add(Calendar.SECOND, 5);

                Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                intent.putExtra("intentId", 2);
                PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                // アラームをセットする
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);

                Toast.makeText(getApplicationContext(), "Set Alarm ", Toast.LENGTH_SHORT).show();

                // setしたあとはホームに飛ばす
                Intent homeIntent = new Intent(AlermSettingActivity.this, MainActivity.class);
                startActivityForResult(homeIntent, 0);

            }
        });

    }

}
