package com.cat.morning.goodmorningcat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.morning.goodmorningcat.test.TestFlingActivity;
import com.cat.morning.goodmorningcat.test.TestShakeActivity;
import com.cat.morning.goodmorningcat.test.TestSwipeActivity;
import com.cat.morning.goodmorningcat.test.TestTapActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import io.repro.android.Repro;

public class MainActivity extends AppCompatActivity {

    private static final int bid1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*
         * Repro
         */
        // repro acount
        // nekoappdevelopers@gmail.com  nekonekoneko
        // token; c5de83ed-5b81-42f3-83c8-bc92410147c0
        // ID ; y6w10q39

        // Setup Repro
        Repro.setup("c5de83ed-5b81-42f3-83c8-bc92410147c0");
        Repro.startRecording();
        Repro.setUserID("y6w10q39");
        Repro.track("app test");

        // アプリ内メッセージを出す
        Repro.disableInAppMessageOnActive();
        Repro.setup("a1e6be89-3139-4814-bf5c-494634ea7cb5");
        Repro.showInAppMessage(this);


        // push通知設定  662649025058
        Repro.setup("c5de83ed-5b81-42f3-83c8-bc92410147c0");
        Repro.enablePushNotification("662649025058");


        LayoutInflater inflater = getLayoutInflater();

        String[] catArray = {"太郎", "ザビエル", "みけこ", "マイケル" };

        LinearLayout llCallList = (LinearLayout)findViewById(R.id.llCallList);

//        ListView listView = (ListView)findViewById(R.id.lvCallList);
//        ArrayList<CallItem> data = new ArrayList<CallItem>();
//        Adapter adapter = new CallAdapter(this, data, R.layout.part_call_list);

        // DB呼び出し
        final SQLiteDatabase db = MyDBHelper.getInstance(MainActivity.this).getWritableDatabase();

        final Cursor cursor = db.rawQuery("SELECT week, time, cat_type, status, id FROM alert_set_table ORDER BY id", null);

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                final LinearLayout callListCell = (LinearLayout)inflater.inflate(R.layout.part_call_list, llCallList, false);
                callListCell.setId(i);
                final int count = i + 1;

//                Log.d("test week", cursor.getString(0));
//                Log.d("test time", cursor.getString(1));
//                Log.d("test cat_type", cursor.getString(2));
//                Log.d("test status", cursor.getString(3));
//                Log.d("test id", cursor.getString(4));
//                Log.d("test ", "---------------");
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

                callListCell.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        // タイトル非表示
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        // フルスクリーン
                        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                        dialog.setContentView(R.layout.dialog_alarm_delete);
                        // 背景を透明にする
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        // OK ボタンのリスナ
                        dialog.findViewById(R.id.bOk).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
//                                deleteAlarm(db, cursor.getInt(0));
//                                db.execSQL("DELETE FROM alert_set_table WHERE id = ?", new Integer[] {cursor.getInt(0)});
//                                callListCell.removeAllViews();

                            }
                        });
                        // Close ボタンのリスナ
                        dialog.findViewById(R.id.bNo).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return false;
                    }
                });



                cursor.moveToNext();
            }
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AlermSettingActivity.class);
                startActivityForResult(intent, 0);

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
                AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                am.cancel(pending);
                Toast.makeText(getApplicationContext(), "Cancel ALARM 1", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.llNoSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlermSettingActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("repro-notification-id")) {
            Repro.trackNotificationOpened(extras.getString("repro-notification-id"));
        }
    }

    public void deleteAlarm(SQLiteDatabase db, int alarmId) {
        db.execSQL("DELETE FROM alert_set_table WHERE id = ?", new Integer[] {alarmId});
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
            Intent intent = new Intent(this, AlermSettingActivity.class);
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

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}