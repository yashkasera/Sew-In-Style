/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ErrorActivity extends AppCompatActivity {
    Context context=this;
    String error="",button="",person="",per,perId,perName;
    TextView err;
    Button but;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Toolbar toolbar = findViewById(R.id.toolbar);
        perId=getIntent().getStringExtra("perId");
        person = getIntent().getStringExtra("person");
        per = getIntent().getStringExtra("per");
        error = getIntent().getStringExtra("error");
        perName = getIntent().getStringExtra("perName");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Error");
        err = findViewById(R.id.error);
        but = findViewById(R.id.button);
        if(error.equalsIgnoreCase("noPerson"))
        {
            err.setText("No "+person+"s Found!");
            but.setText("ADD NOW");
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(context, AddPersonActivity.class);
                    intent.putExtra("person", person);
                    intent.putExtra("per", per);
                    finish();
                    startActivity(intent);
                }
            });
        }
        if(error.equalsIgnoreCase("noMeasurement"))
        {
            err.setText("No Measurements Found!");
            but.setText("ADD NOW");
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,InsertOrUpdateMeasurements.class);
                    intent.putExtra("function", "Add Measurements");
                    intent.putExtra("perName", perName);
                    intent.putExtra("person", person);
                    intent.putExtra("per", per);
                    intent.putExtra("perId", perId);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_any);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_home:
                finish();
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }

}
