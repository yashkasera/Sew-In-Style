/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class InsertOrUpdateMeasurements extends AppCompatActivity {
    String person = "", perName = "", perId = "", function = "";
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    int flag = 0;
    double m1, m2, m3, m4, m5, m6, m7, m8, m9, m10;
    TextInputEditText neck, shoulder, chest, armhole, sleeve, mohri, waist, hips, length, bottomlength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_or_update_measurements);
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        person = getIntent().getStringExtra("person");
        function = getIntent().getStringExtra("function");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(perName);
        getSupportActionBar().setSubtitle(function);
        neck = findViewById(R.id.neck);
        shoulder = findViewById(R.id.shoulder);
        chest = findViewById(R.id.chest);
        armhole = findViewById(R.id.armhole);
        sleeve = findViewById(R.id.sleeve);
        mohri = findViewById(R.id.mohri);
        waist = findViewById(R.id.waist);
        hips = findViewById(R.id.hips);
        length = findViewById(R.id.length);
        bottomlength = findViewById(R.id.bottomlength);
        if (function.equalsIgnoreCase("Add Measurements")) {
            findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m1 = Double.parseDouble(neck.getText().toString());
                    m2 = Double.parseDouble(shoulder.getText().toString());
                    m3 = Double.parseDouble(chest.getText().toString());
                    m4 = Double.parseDouble(armhole.getText().toString());
                    m5 = Double.parseDouble(sleeve.getText().toString());
                    m6 = Double.parseDouble(mohri.getText().toString());
                    m7 = Double.parseDouble(waist.getText().toString());
                    m8 = Double.parseDouble(hips.getText().toString());
                    m9 = Double.parseDouble(length.getText().toString());
                    m10 = Double.parseDouble(bottomlength.getText().toString());
                    boolean a = myDbAdapter.insertMeasurements(perId, perName, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);
                    if (a) {
                        Message.message(context, "Measurements added successfully.");
                        Intent intent = new Intent(context, CustomerActivity.class);
                        intent.putExtra("perName", perName);
                        intent.putExtra("perId", perId);
                        intent.putExtra("person", person);
                        finish();
                        startActivity(intent);
                    } else
                        Message.message(context, "Measurements could not be added.");
                }
            });
        } else {
            Cursor cursor = myDbAdapter.getMeasurements(perId);
            String name1 = "";
            neck.setText(cursor.getString(cursor.getColumnIndex("NECK")));
            shoulder.setText(cursor.getString(cursor.getColumnIndex("SHOULDER")));
            chest.setText(cursor.getString(cursor.getColumnIndex("CHEST")));
            armhole.setText(cursor.getString(cursor.getColumnIndex("ARMHOLE")));
            sleeve.setText(cursor.getString(cursor.getColumnIndex("SLEEVE")));
            mohri.setText(cursor.getString(cursor.getColumnIndex("MOHRI")));
            waist.setText(cursor.getString(cursor.getColumnIndex("WAIST")));
            hips.setText(cursor.getString(cursor.getColumnIndex("HIPS")));
            length.setText(cursor.getString(cursor.getColumnIndex("LENGTH")));
            bottomlength.setText(cursor.getString(cursor.getColumnIndex("BOTTOMLENGTH")));
            neck.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        neck.setText("0");
                    }
                    flag = 1;
                }
            });
            shoulder.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        shoulder.setText("0");
                    }
                    flag = 1;
                }
            });
            chest.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        chest.setText("0");
                    }flag = 1;
                }
            });
            armhole.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        armhole.setText("0");
                    }flag = 1;
                }
            });
            sleeve.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        sleeve.setText("0");
                    }flag = 1;
                }
            });
            mohri.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        mohri.setText("0");
                    }flag = 1;
                }
            });
            waist.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        waist.setText("0");
                    }flag = 1;
                }
            });
            hips.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        hips.setText("0");
                    }flag = 1;
                }
            });
            length.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        length.setText("0");
                    }flag = 1;
                }
            });
            bottomlength.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0||s.equals(null)|| TextUtils.isEmpty(s.toString())){
                        bottomlength.setText("0");
                    }flag = 1;
                }
            });
            findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag != 0) {
                        final alertDialog dialog = new alertDialog(context);
                        dialog.setTitle("Confirm Update", "All previous data will be lost. Are you sure you still want to continue? ", "Update", "Cancel");
                        Button yes = dialog.positive;
                        Button no = dialog.negative;
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                m1 = Double.parseDouble(neck.getText().toString());
                                m2 = Double.parseDouble(shoulder.getText().toString());
                                m3 = Double.parseDouble(chest.getText().toString());
                                m4 = Double.parseDouble(armhole.getText().toString());
                                m5 = Double.parseDouble(sleeve.getText().toString());
                                m6 = Double.parseDouble(mohri.getText().toString());
                                m7 = Double.parseDouble(waist.getText().toString());
                                m8 = Double.parseDouble(hips.getText().toString());
                                m9 = Double.parseDouble(length.getText().toString());
                                m10 = Double.parseDouble(bottomlength.getText().toString());
                                boolean a = myDbAdapter.updateMeasurements(perId, perName, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10);
                                if (a) {
                                    Message.message(context, "Measurements updated successfully");
                                    finish();
                                    Intent intent = new Intent(context, CustomerActivity.class);
                                    intent.putExtra("perName", perName);
                                    intent.putExtra("perId", perId);
                                    intent.putExtra("person", person);
                                    finish();
                                    startActivity(intent);
                                } else
                                    Message.message(context, "Unsuccessful");
                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.quitDialog(v);
                            }
                        });
                        dialog.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.quitDialog(v);
                            }
                        });
                        dialog.show();
                        Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    } else {
                        Message.message(context, "No change observed");
                        finish();
                    }
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(myDbAdapter!=null)
        myDbAdapter.close();
    }
}
