package com.cat.morning.goodmorningcat;

import android.app.TimePickerDialog;
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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by imarie on 16/01/24.
 */
public class AlertSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_setting);


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
                new TimePickerDialog(AlertSettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
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



            }
        });

    }

}
