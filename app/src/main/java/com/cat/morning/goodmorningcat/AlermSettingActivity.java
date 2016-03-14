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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class AlermSettingActivity extends AppCompatActivity {

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
                Snackbar.make(findViewById(R.id.llView), "Test アラームセット完了。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // DBに登録する
                setData();
                SQLiteDatabase db = MyDBHelper.getInstance(AlermSettingActivity.this).getWritableDatabase();

                String etTime = ((EditText)findViewById(R.id.etTime)).getText().toString();


                db.execSQL("INSERT INTO alert_set_table (week, time, vibrate, cat_type, status) VALUES ('月曜',?, 0, 1, 0)", new String[]{etTime});

                Cursor cs = db.rawQuery("SELECT * FROM alert_set_table ", null);
                cs.moveToLast();
                int requestCode = cs.getInt(0); // pendingIntent登録用requestCode
Log.d("test requestCode is ", Integer.toString(requestCode));
                cs.close();

                /*  アラート登録時はONの状態なので、アラートをセットする。 */
                // セットする時刻取得
                String[] time = etTime.split(":", 0);

                // アラーム時間設定
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                // 設定した時刻をカレンダーに設定
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                // 過去だったら明日にする
                if(cal.getTimeInMillis() < System.currentTimeMillis()){
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }


                Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                intent.putExtra("intentId", 2);

                // もし新規登録だったら、DBに登録されている数の次の数をrequestCodeにする. 登録済みのやつの編集なら、そのIDをrequestCodeに使用する
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, 0);

                // アラームをセットする
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                // setしたあとはホームに飛ばす
                Intent homeIntent = new Intent(AlermSettingActivity.this, MainActivity.class);
                startActivityForResult(homeIntent, 0);
                AlermSettingActivity.this.finish(); // Activity終了

            }
        });

    }

    private void setData() {

//        ContentValues cv = new ContentValues();
//
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = null;
//            oos = new ObjectOutputStream(bos);
//            oos.writeObject(alarm.getDays());
//            byte[] buff = bos.toByteArray();
//
//            cv.put("week", buff);
//
//        } catch (Exception e){
//        }
//        cv.put("time", alarm.getAlarmTimeString());
//        cv.put("vibrate", alarm.getDifficulty().ordinal());
//        cv.put("cat_type", alarm.getAlarmTonePath());
//        cv.put("status", alarm.getVibrate());
//
//        return getDatabase().insert(ALARM_TABLE, null, cv);
    }

}
