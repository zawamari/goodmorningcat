package com.cat.morning.goodmorningcat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(this, CatProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
