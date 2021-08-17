/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class InventorySaleActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    Cursor cursor;
    String itemId,day,month;
    int mYear, mMonth, mDay,mYear1, mMonth1, mDay1,PieceId, selectedId;
    TextView item,cost,profit,profitper,name;
    TextInputEditText date,sp;
    TextInputLayout date1,sp1;
    double C,S,P,PP;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_sale);
        itemId = getIntent().getStringExtra("itemId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Inventory");
        item = findViewById(R.id.item);
        name = findViewById(R.id.name);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        date = findViewById(R.id.date);
        cost = findViewById(R.id.cost);
        sp = findViewById(R.id.sellingPrice);
        date = findViewById(R.id.date);
        profit = findViewById(R.id.profit);
        profitper = findViewById(R.id.profitper);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        cursor = myDbAdapter.getItemFromInventory(itemId);
        cost.setText(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
        item.setText(cursor.getString(cursor.getColumnIndexOrThrow("ITEM")));

        date.setText(sdf.format(new Date()));
        findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDay = dayOfMonth;
                        mYear = year;
                        mMonth = monthOfYear + 1;
                        day = String.valueOf(dayOfMonth);
                        month = String.valueOf(monthOfYear + 1);
                        if (day.length() == 1)
                            day = "0" + day;
                        if (month.length() == 1)
                            month = "0" + month;
                        date.setText(day + "-" + month + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y = Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(y, m, d);
                calendar1.add(Calendar.DATE, 1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if (day.length() == 1)
                    day = "0" + day;
                if (month.length() == 1)
                    month = "0" + month;
                date.setText(day + "-" + month + "-" + mYear1);
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y = Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(y, m, d);
                calendar1.add(Calendar.DATE, -1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if (day.length() == 1)
                    day = "0" + day;
                if (month.length() == 1)
                    month = "0" + month;
                date.setText(day + "-" + month + "-" + mYear1);
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog = new alertDialog(context);
                Button yes = dialog.findViewById(R.id.positive);
                Button no = dialog.findViewById(R.id.negative);
                ImageView dismiss = dialog.findViewById(R.id.dismiss);
                dialog.setTitle("Confirm Cancel", "Are you sure you want to cancel?", "Yes", "No");
                yes.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                        dialog.quitDialog(v);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.quitDialog(v);
                    }
                });
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.quitDialog(v);
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
        C=Double.parseDouble(cost.getText().toString());
        sp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(sp.getText().toString())) {
                    if(sp.getText().toString().equalsIgnoreCase("0")) {
                        sp.setText("0.0");
                        sp.selectAll();
                    }
                    else {
                        S = Double.parseDouble(sp.getText().toString());
                        P = S - C;
                        PP = (P / C) * 100;
                        PP = Math.rint(PP);
                        profit.setText(String.valueOf(P));
                        profitper.setText(PP + "%");
                    }
                }
                else
                    sp.setText("0");
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(sp.getText().toString())) {
                    if(sp.getText().toString().equalsIgnoreCase("0")) {
                        sp.setText("0.0");
                        sp.selectAll();
                    }
                    else {
                        S = Double.parseDouble(sp.getText().toString());
                        P = S - C;
                        PP = (P / C) * 100;
                        PP = Math.rint(PP);
                        profit.setText(String.valueOf(P));
                        profitper.setText(PP + "%");
                    }
                }
                else
                    sp.setText("0");
            }
        });
        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog=new alertDialog(context,'a');
                final RadioGroup radioGroup=dialog.radioGroup;
                selectedId=radioGroup.getCheckedRadioButtonId();
                final TextInputLayout nam1 = dialog.findViewById(R.id.name1);
                final TextInputEditText nam = dialog.findViewById(R.id.name);
                radioButton = dialog.findViewById(selectedId);
                nam1.setVisibility(View.GONE);
                nam.setVisibility(View.GONE);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        radioButton=dialog.findViewById(checkedId);
                        if(radioButton.getText().equals("Other")){
                            nam1.setVisibility(View.VISIBLE);
                            nam.setVisibility(View.VISIBLE);
                        }
                        else {
                            nam1.setVisibility(View.GONE);
                            nam.setVisibility(View.GONE);
                        }
                    }
                });
                dialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedId=radioGroup.getCheckedRadioButtonId();
                        radioButton = dialog.findViewById(selectedId);
                        if(selectedId>0) {
                            final String b = radioButton.getText().toString();
                            if(b.equalsIgnoreCase("Other")){
                                if(nam.getText().toString().length()==0)
                                    nam1.setError("Name cannot be empty");
                                else {
                                    name.setText(b + " - " + nam.getText().toString());
                                    dialog.dismiss();
                                }
                            }
                            else{
                                cursor=myDbAdapter.getPersons("Customer");
                                String[] columns={"NAME"};
                                int[] to = {R.id.textView};
                                SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
                                final alertDialog dialog1=new alertDialog(context,simpleCursorAdapter,"Choose Customer","Select the customer who wants to buy this piece");
                                final ListView listView=dialog1.listView;
                                listView.setAdapter(simpleCursorAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        cursor = (Cursor) listView.getItemAtPosition(position);
                                        String c = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                                        name.setText(b+" - "+c);
                                        dialog1.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                                dialog1.show();
                                Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            }
                        }
                        else{
                            Message.message(context,"Please choose a buyer");
                        }
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(sp.getText()) || sp.getText().toString().equalsIgnoreCase("0.0")) {
                    sp1.setError("Selling Price cannot be 0.0");
                } else if (name.getText().toString().equals("-")) {
                    Message.message(context, "Please specify buyer");
                } else {
                    String buyer = name.getText().toString().substring((name.getText().toString().indexOf("-") + 1)).trim();
                    if (myDbAdapter.onSoldFromInventory(itemId, sp.getText().toString(), date.getText().toString(), profit.getText().toString(), buyer)) {
                        Message.message(context, "Item successfully sold");
                        finish();
                    }
                    else {
                        Message.message(context, "Unsuccessful!");
                    }
                }
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
        if(cursor!=null)
            cursor.close();
    }
}
