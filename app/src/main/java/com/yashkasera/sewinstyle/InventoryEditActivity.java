/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class InventoryEditActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    String itemId="",Item,costPrice,sellingPrice,DateAdded,DateSold,Description,Buyer;
    TextInputEditText item,cp,sp,dateAdded,dateSold,desc,buyer;
    TextInputLayout item1,cp1,sp1,dateAdded1,dateSold1,desc1,buyer1;
    Cursor cursor;
    int flag=0,flag1=0;
    double P,CP,SP;
    int mYear, mMonth, mDay,mYear1, mMonth1, mDay1;
    String day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Inventory");
        getSupportActionBar().setSubtitle("Edit Item");
        itemId = getIntent().getStringExtra("itemId");
        item = findViewById(R.id.item);
        item1 = findViewById(R.id.item1);
        cp = findViewById(R.id.cp);
        cp1 = findViewById(R.id.cp1);
        sp = findViewById(R.id.sp);
        sp1 = findViewById(R.id.sp1);
        dateAdded = findViewById(R.id.dateAdded);
        dateAdded1 = findViewById(R.id.dateAdded1);
        dateSold = findViewById(R.id.dateSold);
        dateSold1 = findViewById(R.id.dateSold1);
        desc = findViewById(R.id.description);
        desc1 = findViewById(R.id.description1);
        buyer = findViewById(R.id.buyer);
        buyer1 = findViewById(R.id.buyer1);
        cursor = myDbAdapter.getItemFromInventory(itemId);
        Message.message(context,itemId);
        Item = cursor.getString(cursor.getColumnIndexOrThrow("ITEM"));
        costPrice = cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE"));
        sellingPrice = cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
        DateAdded = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
        DateSold = cursor.getString(cursor.getColumnIndexOrThrow("DATESOLD"));
        Description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
        Buyer = cursor.getString(cursor.getColumnIndexOrThrow("SOLDTO"));
        if(sellingPrice.equalsIgnoreCase("-")){
            sp.setVisibility(View.GONE);
            buyer.setVisibility(View.GONE);
            buyer1.setVisibility(View.GONE);
            dateSold.setVisibility(View.GONE);
            dateSold1.setVisibility(View.GONE);
            findViewById(R.id.date2).setVisibility(View.GONE);
            flag1=1;
        }
        item.setText(Item);
        cp.setText(costPrice);
        sp.setText(sellingPrice);
        dateAdded.setText(DateAdded);
        dateSold.setText(DateSold);
        desc.setText(Description);
        buyer.setText(Buyer);
        item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Item.equalsIgnoreCase(s.toString()))
                    flag=1;
                if(s.toString().length()==0)
                    item1.setError("Cannot be empty!");
                else
                    item1.setError(null);
            }
        });
        cp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Item.equalsIgnoreCase(s.toString()))
                    flag=1;
                if(s.toString().length()==0)
                    cp1.setError("Cannot be empty!");
                else
                    cp1.setError(null);
            }
        });
        sp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Item.equalsIgnoreCase(s.toString()))
                    flag=1;
                if(s.toString().length()==0)
                    sp.setError("Cannot be empty!");
                else
                    sp.setError(null);
            }
        });
        buyer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Item.equalsIgnoreCase(s.toString()))
                    flag=1;
                if(s.toString().length()==0)
                    buyer1.setError("Cannot be empty!");
                else
                    buyer1.setError(null);
            }
        });

        findViewById(R.id.date1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=dateAdded.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar=Calendar.getInstance();
                calendar.set(y,m,d);
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDay = dayOfMonth;
                        mYear = year;
                        mMonth = monthOfYear;
                        day = String.valueOf(dayOfMonth);
                        month = String.valueOf(monthOfYear);
                        if(day.length()==1)
                            day="0"+day;
                        if(month.length()==1)
                            month="0"+month;
                        dateAdded.setText(day+"-"+month+"-"+year);
                    }
                },mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        findViewById(R.id.date2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=dateSold.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar=Calendar.getInstance();
                calendar.set(y,m,d);
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDay = dayOfMonth;
                        mYear = year;
                        mMonth = monthOfYear;
                        day = String.valueOf(dayOfMonth);
                        month = String.valueOf(monthOfYear);
                        if(day.length()==1)
                            day="0"+day;
                        if(month.length()==1)
                            month="0"+month;
                        dateSold.setText(day+"-"+month+"-"+year);
                    }
                },mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getText().toString().length()==0){
                    item1.setError("Item Name cannot be empty");
                }
                else if(cp.getText().toString().length()==0){
                    cp1.setError("Cost Price cannot be empty");
                }
                else if(sp.getText().toString().length()==0){
                    sp1.setError("Selling Price cannot be empty");
                }
                else if(buyer.getText().toString().length()==0){
                    buyer1.setError("Cost Price cannot be empty");
                }
                else if(flag==0){
                    Message.message(context,"No changed observed!");
                    finish();
                }
                else{
                    if(flag1==1){               //NOT SOLD
                        if (myDbAdapter.editItemInventory(itemId, item.getText().toString(), cp.getText().toString(), dateAdded.getText().toString(), desc.getText().toString())) {
                            Message.message(context,"Item updated successfully");
                            finish();
//                            startActivity(new Intent(context,DashboardActivity.class));
                        }
                        else{
                            Message.message(context,"Item could not be updated!");
                        }
                    }
                    else{
                        double profit=Double.parseDouble(sp.getText().toString())-Double.parseDouble(cp.getText().toString());
                        if (myDbAdapter.editItemInventorySold(itemId,item.getText().toString(),cp.getText().toString(),dateAdded.getText().toString(),buyer.getText().toString(),sp.getText().toString(),dateSold.getText().toString(),desc.getText().toString(),profit+"")) {
                            Message.message(context,"Item updated successfully");
                            finish();
//                            startActivity(new Intent(context,DashboardActivity.class));
                        }
                        else{
                            Message.message(context,"Item could not be updated!");
                        }
                    }
                }
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog = new alertDialog(context);
                dialog.setTitle("Confirm Return","Are you sure you want to return this item back to the inventory?","Yes","No");
                dialog.findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myDbAdapter.editItemInventorySold(itemId, item.getText().toString(), cp.getText().toString(), dateAdded.getText().toString(),"-", "-", "-", desc.getText().toString(), "-")) {
                            Message.message(context,"Item returned to inventory");
                            dialog.dismiss();
                            finish();
                            startActivity(new Intent(context,DashboardActivity.class));
                        }
                        else{
                            Message.message(context,"Item could not be returned to inventory");
                        }
                    }
                });
                dialog.findViewById(R.id.negative).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
    @Override
    public void onBackPressed(){
        final alertDialog dialog = new alertDialog(context);
        Button yes = dialog.findViewById(R.id.positive);
        Button no= dialog.findViewById(R.id.negative);
        ImageView dismiss= dialog.findViewById(R.id.dismiss);
        dialog.setTitle("Confirm Cancel","Are you sure you want to cancel?","Yes","No");
        yes.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)     {
                dialog.quitDialog(v);
                InventoryEditActivity.super.onBackPressed();
                finish();
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
}
