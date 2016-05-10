package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cat.morning.goodmorningcat.util.AlermSettingUtils;

import java.util.Calendar;

public class AlermSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerm_setting);


        /*
         * 猫選択
         */
//        Spinner spCatType = (Spinner)findViewById(R.id.spCatType);
//
//        // TODO: ネコのタイプはローカルDBから取得するように変更する
//        ArrayAdapter<String> catTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
//        catTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        catTypeAdapter.add("みけこ");
//        catTypeAdapter.add("マイケル");
//        catTypeAdapter.add("太郎");
//
//        spCatType.setAdapter(catTypeAdapter);
//
//        spCatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Spinner spinner = (Spinner) parent;
//                // 選択されたアイテムを取得します
//                String item = (String) spinner.getSelectedItem();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        /*
         * 曜日選択
         */
//        final AlarmItem alarm;
//
//        final AlarmItem.Day[] days = {AlarmItem.Day.MONDAY, AlarmItem.Day.TUESDAY, AlarmItem.Day.WEDNESDAY, AlarmItem.Day.THURSDAY, AlarmItem.Day.FRIDAY, AlarmItem.Day.SATURDAY, AlarmItem.Day.SUNDAY};
//
//        TextView tvDayOfTheWeek = (TextView)findViewById(R.id.tvDayOfTheWeek);
//        tvDayOfTheWeek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(AlermSettingActivity.this);
//
//                alert.setTitle("Repeat ??");
//                String[] repeatDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
//
//                CharSequence[] multiListItems = new CharSequence[repeatDays.length];
//                for (int i = 0; i < multiListItems.length; i++)
//                    multiListItems[i] = repeatDays[i];
//
//                boolean[] checkedItems = new boolean[multiListItems.length];
//                for (AlarmItem.Day day : days) {
//                    checkedItems[day.ordinal()] = true;
//                }
//                alert.setMultiChoiceItems(multiListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
//
//                    @Override
//                    public void onClick(final DialogInterface dialog, int which, boolean isChecked) {
//                        AlarmItem.Day thisDay = AlarmItem.Day.values()[which];
//
//                            if (isChecked) {
//                                alarm.addDay(thisDay);
//                            } else {
//                                // Only remove the day if there are more than 1
//                                // selected
//                                if (alarm.getDays().length > 1) {
//                                    alarm.removeDay(thisDay);
//                                } else {
//                                    // If the last day was unchecked, re-check
//                                    // it
//                                    ((AlertDialog) dialog).getListView().setItemChecked(which, true);
//                                }
//                        }
//                    }
//                });
//                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//
//
//                    }
//                });
//                alert.show();
//            }
//        });

        /*
         * 時間
         */
        final TextView tvTime = (TextView)findViewById(R.id.tvTime);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AlermSettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // 時間が設定されたときの処理
                        Log.v("Time", String.format("%02d:%02d", hourOfDay, minute));
                        tvTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, 13, 0, true).show();
            }
        });



        /*
         * 登録ボタン
         */
        findViewById(R.id.tvSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // DBに登録する
                String tvTime = ((TextView) findViewById(R.id.tvTime)).getText().toString();
                SQLiteDatabase db = MyDBHelper.getInstance(AlermSettingActivity.this).getWritableDatabase();
                db.execSQL("INSERT INTO alert_set_table (week, time, vibrate, cat_type, status) VALUES ('月曜',?, 0, 1, 0)", new String[]{tvTime});
                // 曜日は今後のために残しておく

                /*  アラート登録時はONの状態なので、アラートをセットする。 */
                // セットする時刻取得
                String[] time = tvTime.split(":", 0);

                AlermSettingUtils.setAlerm(AlermSettingActivity.this, db, time);

                // setしたあとはホームに飛ばす
                Intent homeIntent = new Intent(AlermSettingActivity.this, MainActivity.class);
                startActivityForResult(homeIntent, 0);
                AlermSettingActivity.this.finish(); // Activity終了

            }
        });



        /*
         * MENU
         */
        findViewById(R.id.ivTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(AlermSettingActivity.this, MainActivity.class);
                startActivityForResult(homeIntent, 0);
                AlermSettingActivity.this.finish(); // Activity終了
            }
        });

    }

}
