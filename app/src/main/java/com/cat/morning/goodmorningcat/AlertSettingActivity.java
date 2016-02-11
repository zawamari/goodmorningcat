package com.cat.morning.goodmorningcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by imarie on 16/01/24.
 */
public class AlertSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_setting);


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
                Toast.makeText(AlertSettingActivity.this, item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

}
