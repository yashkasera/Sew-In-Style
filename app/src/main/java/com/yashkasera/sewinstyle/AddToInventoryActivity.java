/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddToInventoryActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter=new MyDbAdapter(this);
    Context context=this;
    TextInputLayout date1,item1,description1,cost1;
    TextInputEditText date,item,description,cost;
    int mYear, mMonth, mDay,mYear1, mMonth1, mDay1,flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_inventory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Inventory");
        date=findViewById(R.id.date);
        date1=findViewById(R.id.date1);
        item=findViewById(R.id.item);
        item1=findViewById(R.id.item1);
        description=findViewById(R.id.description);
        description1=findViewById(R.id.description1);
        cost=findViewById(R.id.cost);
        cost1=findViewById(R.id.cost1);
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(sdf.format(new Date()));
        datePickerFunc();
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                calendar1.add(Calendar.DATE,1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                date.setText(day+"-"+month+"-"+mYear1);
                datePickerFunc();
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                calendar1.add(Calendar.DATE,-1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                date.setText(day+"-"+month+"-"+mYear1);
                datePickerFunc();
            }
        });
        item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(item.getText().toString())) {
                    item1.setError("Item name cannot be empty");
                    flag = 1;
                } else {
                    flag = 0;
                }
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(item.getText().toString())||TextUtils.isEmpty(cost.getText().toString())) {
                    if(TextUtils.isEmpty(item.getText().toString())) {
                        cost1.setError("Cost Price cannot be empty");
                    }
                    if(TextUtils.isEmpty(item.getText().toString())) {
                        item1.setError("Item name cannot be empty");
                    }
                    return;
                }
                else {
                    if (TextUtils.isEmpty(description.getText().toString())) {
                        alertDialog dialog = new alertDialog(context);
                        dialog.setTitle("Continue without description","The description of your piece looks empty. Are you sure you want to proceed without it?","Yes","No");
                       dialog.positive.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                                        description.setText("-");
                                        String it = item.getText().toString();
                                        it.toUpperCase();
                                        it.trim();
                                        boolean a = myDbAdapter.addToInventory(it, Double.parseDouble(cost.getText().toString()), date.getText().toString(), description.getText().toString());
                                        if (a) {
                                            Message.message(context, "New Item Added Successfully");
                                            finish();
                                        } else
                                            Message.message(context, "New Item cannot be added");
                                    }
                                });
                       dialog.show();
                        Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    } else {
                        boolean a = myDbAdapter.addToInventory(item.getText().toString(), Double.parseDouble(cost.getText().toString()), date.getText().toString(), description.getText().toString());
                        if (a) {
                            Message.message(context, "New Item Added Successfully");
                            finish();
                        } else
                            Message.message(context, "New Item cannot be added");
                    }
                }
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(item.getText().toString())) {
                    cost1.setError("Cost Price cannot be empty");
                    flag=1;
                }
                else
                    flag=0;
            }
        });
    }

    private void datePickerFunc() {
        final DatePicker datePicker = findViewById(R.id.datePicker);
        String t = date.getText().toString();
        int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
        int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
        int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
        Calendar calendar=Calendar.getInstance();
        calendar.set(y,m,d);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String day = String.valueOf(datePicker.getDayOfMonth());
                String month = String.valueOf(datePicker.getMonth() + 1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                date.setText(day+"-"+month+"-"+datePicker.getYear());
            }
        });
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
