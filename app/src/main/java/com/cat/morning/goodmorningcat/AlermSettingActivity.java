package com.cat.morning.goodmorningcat;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cat.morning.goodmorningcat.util.AlermSettingUtils;

public class AlermSettingActivity extends AppCompatActivity {

    private static final int NO_SETTING = 99;

    private int id;
    private String time;
    private int volume = NO_SETTING;
    private int manner = NO_SETTING;
    private int vibrate = NO_SETTING;
    private int status = NO_SETTING;
    private boolean updateAlarm = false;

    private String spinnerVolumeValue = "";
    private int setVolume = 3;
    private int setVibrator = 0;
    private int setManner = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerm_setting);

        Intent intent = getIntent();
        if (intent.getIntExtra("id", 0) != 0) {
            id = intent.getIntExtra("id", 0);
            updateAlarm = true;

            final SQLiteDatabase db = MyDBHelper.getInstance(AlermSettingActivity.this).getWritableDatabase();
            final Cursor cursor = db.rawQuery("SELECT time, volume, manner, vibrate, status, cat_type FROM alert_set_table WHERE id = ?", new String[]{Integer.toString(id)});
            if (cursor.moveToFirst()) {
                time = cursor.getString(0);
                volume = cursor.getInt(1);
                manner = cursor.getInt(2);
                vibrate = cursor.getInt(3);
                status = cursor.getInt(4);
            }
        }

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
        if (time != null) {
            tvTime.setText(time);
        }

        /*
         * 音量 TODO: とりあえず簡単に実装。
         */
        final Spinner spVolum = (Spinner)findViewById(R.id.spVolum);

        ArrayAdapter<String> volumeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        volumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        volumeAdapter.add("小さい");
        volumeAdapter.add("普通");
        volumeAdapter.add("大きい");

        spVolum.setAdapter(volumeAdapter);

        if (volume != NO_SETTING) {
            if (volume == 0) {
                spVolum.setSelection(volume);
                setVolume = 0;
            } else if (volume == 3) {
                spVolum.setSelection(1);
                setVolume = 3;
            } else {
                spVolum.setSelection(2);
                setVolume = 6;
            }
        } else {
            spVolum.setSelection(1);
        }
        spVolum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                spinnerVolumeValue = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        /*
         * バイブレーション
         */
        final Switch swVibrator = (Switch)findViewById(R.id.swVibrator);

        if (vibrate != NO_SETTING) {
            switch (vibrate) {
                case 0: //on
                    swVibrator.setChecked(true);
                    break;
                case 1:
                    swVibrator.setChecked(false);
                    setVibrator = 1;
                    break;
            }
        }

        swVibrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setVibrator = 0;
                } else {
                    setVibrator = 1;
                }
            }
        });


        /*
         * マナーモード
         */
        final Switch swManner = (Switch)findViewById(R.id.swManner);

        if (manner != NO_SETTING) {
            switch (manner) {
                case 0: //on
                    swManner.setChecked(true);
                    setManner = 0;
                    break;
                case 1:
                    swManner.setChecked(false);
                    setManner = 1;
                    break;
            }
        }

        swManner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setManner = 0;
                } else {
                    setManner = 1;
                }
            }
        });


        /*
         * 登録ボタン
         */
        findViewById(R.id.tvSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tvTime = ((TextView) findViewById(R.id.tvTime)).getText().toString();
                if (tvTime.equals("-- : --")) {
                    final Dialog dialog = new Dialog(AlermSettingActivity.this);
                    // タイトル非表示
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                    // フルスクリーン
                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                    dialog.setContentView(R.layout.dialog_cannot_regist);

                    // OK ボタンのリスナ
                    dialog.findViewById(R.id.bOk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {

                    // DBに登録する
                    SQLiteDatabase db = MyDBHelper.getInstance(AlermSettingActivity.this).getWritableDatabase();

                    if (spinnerVolumeValue != "") {
                        switch (spinnerVolumeValue) {
                            case "小さい": //on
                                setVolume = 0;
                                break;
                            case "普通":
                                setVolume = 3;
                                break;
                            case "大きい":
                                setVolume = 6;
                                break;
                        }
                    }

                    Log.d("test update time", tvTime);
                    Log.d("test update volume", Integer.toString(setVolume));
                    Log.d("test update vibrate", Integer.toString(setVibrator));
                    Log.d("test update manner", Integer.toString(setManner));

                    if (updateAlarm) {
                        db.execSQL("UPDATE alert_set_table SET time = ?, volume = ?, vibrate = ?, manner = ?, status = ? WHERE id = ?",
                                new String[]{tvTime, Integer.toString(setVolume), Integer.toString(setVibrator), Integer.toString(setManner), Integer.toString(status), Integer.toString(id)});
                    } else {
                        db.execSQL("INSERT INTO alert_set_table (week, time, volume, vibrate, manner) VALUES ('月曜',?, ?, ?, ?)",
                                new String[]{tvTime, Integer.toString(setVolume), Integer.toString(setVibrator), Integer.toString(setManner)});
                    }
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

            }
        });

        /*
         * Clearボタン
         */
        findViewById(R.id.tvClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTime.setText("-- : --");
                spVolum.setSelection(1);
                swVibrator.setChecked(true);
                swManner.setChecked(true);
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

        findViewById(R.id.ivTimeSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlermSettingActivity.this, AlermSettingActivity.class);
                startActivity(intent);
                AlermSettingActivity.this.finish();
            }
        });

        findViewById(R.id.ivCatList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlermSettingActivity.this, CatListActivity.class);
                startActivity(intent);
                AlermSettingActivity.this.finish();
            }
        });

        findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlermSettingActivity.this, SettingActivity.class);
                startActivity(intent);
                AlermSettingActivity.this.finish();
            }
        });
    }

}
