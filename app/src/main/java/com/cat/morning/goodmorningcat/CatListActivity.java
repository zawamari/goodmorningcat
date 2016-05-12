package com.cat.morning.goodmorningcat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CatListActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);

        findViewById(R.id.ivTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatListActivity.this, MainActivity.class);
                startActivity(intent);
                CatListActivity.this.finish();
            }
        });

        findViewById(R.id.ivTimeSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatListActivity.this, AlermSettingActivity.class);
                startActivity(intent);
                CatListActivity.this.finish();
            }
        });

//        findViewById(R.id.ivCatList).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CatListActivity.this, CatProfileActivity.class);
//                startActivity(intent);
//                CatListActivity.this.finish();
//            }
//        });

        findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatListActivity.this, SettingActivity.class);
                startActivity(intent);
                CatListActivity.this.finish();

            }
        });

    }
}
