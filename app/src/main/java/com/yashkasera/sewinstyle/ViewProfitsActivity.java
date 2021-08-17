/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yashkasera.sewinstyle.Profits.ProfitItems;
import com.yashkasera.sewinstyle.Profits.ProfitsAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewProfitsActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    Cursor cursor=null,cursor1=null;
    Switch aSwitch;
    String Year,Month;
    TextView year,profit;
    Spinner spinner;
    Calendar calendar;
    Double pro=0.0;
    ListView listView1;int flag=0;
    ArrayList<ProfitItems> arrayList1=new ArrayList<ProfitItems>();
    String[] columns = {"PIECE", "COSTPRICE", "SELLINGPRICE", "PROFIT"}, columns1 = {"ITEM", "COSTPRICE", "SELLINGPRICE", "PROFIT"};
    int[] to={R.id.date,R.id.piece,R.id.desc,R.id.amt};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profits);
        spinner = findViewById(R.id.spinner);
        calendar = Calendar.getInstance();
        listView1 = findViewById(R.id.listView1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        year = findViewById(R.id.year);
        profit = findViewById(R.id.profit);
        year.setText("All");
        aSwitch = findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    flag = 1;
                    Message.message(context, "Showing profits of Inventory");
                }
                else {
                    flag = 0;
                    Message.message(context,"Hiding profits of inventory");
                }
                function();
            }
        });
        Year = calendar.get(Calendar.YEAR) + "";
        Month = "0";
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(year.getText().toString().equalsIgnoreCase("All")) {
                    Year = calendar.get(Calendar.YEAR) + "";
                    year.setText(Year);
                } else {
                    Year=year.getText().toString();
                    year.setText("All");
                }
                function();
            }
        });
        findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y=Integer.parseInt(Year);
                int t=calendar.get(Calendar.YEAR);
                if(y>=t)
                    Message.message(context,"Cannot add more than present");
                else
                    y++;
                Year=y+"";
                year.setText(Year);
                function();
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y=Integer.parseInt(Year);
                --y;
                Year=y+"";
                year.setText(Year);
                function();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Month=position+"";
                function();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Month = "0";
            }
        });
        toolbar();
    }
    void function() {
        pro=0.0;
        if (Month.equalsIgnoreCase("0") && year.getText().toString().equalsIgnoreCase("all")) {
            cursor = myDbAdapter.getProfits();
            arrayList1.clear();
            if(cursor!=null && cursor.getCount()!=0) {
                cursor.moveToFirst();
                do {
                    ProfitItems profitItems = new ProfitItems();
                    profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
                    profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                    profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                    profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                    pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                    arrayList1.add(profitItems);
                }
                while (cursor.moveToNext());
            }
            if (flag == 1) {
                cursor = myDbAdapter.getItemsSoldFromInventory();
                if (cursor != null && cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    do {
                        ProfitItems profitItems = new ProfitItems();
                        profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("ITEM")));
                        profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                        profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                        profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                        pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        arrayList1.add(profitItems);
                    }
                    while (cursor.moveToNext());
                }
            }
        } else if (year.getText().toString().equalsIgnoreCase("All")) {
            cursor = myDbAdapter.getProfits();
            arrayList1.clear();
            if(cursor!=null && cursor.getCount()!=0) {
                cursor.moveToFirst();
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        ProfitItems profitItems = new ProfitItems();
                        profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
                        profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                        profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                        profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                        pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        arrayList1.add(profitItems);
                    }
                }
                while (cursor.moveToNext());
            }
            if (flag == 1) {
                cursor = myDbAdapter.getItemsSoldFromInventory();
                if (cursor != null && cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    do {
                        String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                        String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                        int temp = Integer.parseInt(t);
                        if (temp == Integer.parseInt(Month)) {
                            ProfitItems profitItems = new ProfitItems();
                            profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("ITEM")));
                            profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                            profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                            profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                            pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                            arrayList1.add(profitItems);
                        }
                    }
                    while (cursor.moveToNext());
                }
            }
        }
        else if(Month.equalsIgnoreCase("0")) {
            int y = Integer.parseInt(year.getText().toString());
            cursor = myDbAdapter.getProfits();
            arrayList1.clear();
            if(cursor!=null && cursor.getCount()!=0) {
                cursor.moveToFirst();
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        ProfitItems profitItems = new ProfitItems();
                        profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
                        profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                        profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                        profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                        pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        arrayList1.add(profitItems);
                    }
                }
                while (cursor.moveToNext());
            }
            if (flag == 1) {
                cursor = myDbAdapter.getItemsSoldFromInventory();
                if (cursor != null && cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    do {
                        String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                        String t = date.substring(date.lastIndexOf("-") + 1);
                        int temp = Integer.parseInt(t);
                        if (temp == y) {
                            ProfitItems profitItems = new ProfitItems();
                            profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("ITEM")));
                            profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                            profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                            profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                            pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                            arrayList1.add(profitItems);
                        }
                    }
                    while (cursor.moveToNext());
                }
            }
        }
        else {
            int y = Integer.parseInt(year.getText().toString());
            cursor = myDbAdapter.getProfits();
            arrayList1.clear();
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month) && y == Integer.parseInt(y1)) {
                        ProfitItems profitItems = new ProfitItems();
                        profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
                        profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                        profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                        profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                        pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        arrayList1.add(profitItems);
                    }
                }
                while (cursor.moveToNext());
            }
            if (flag == 1) {
                cursor = myDbAdapter.getItemsSoldFromInventory();
                if (cursor != null && cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    do {
                        String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                        String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                        String y1 = date.substring(date.lastIndexOf("-") + 1);
                        int temp = Integer.parseInt(m1);
                        if (temp == Integer.parseInt(Month) && y == Integer.parseInt(y1)) {
                            ProfitItems profitItems = new ProfitItems();
                            profitItems.setItem(cursor.getString(cursor.getColumnIndexOrThrow("ITEM")));
                            profitItems.setCostPrice(cursor.getString(cursor.getColumnIndexOrThrow("COSTPRICE")));
                            profitItems.setSellingPrice(cursor.getString(cursor.getColumnIndexOrThrow("SELLINGPRICE")));
                            profitItems.setProfit(cursor.getString(cursor.getColumnIndexOrThrow("PROFIT")));
                            pro += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                            arrayList1.add(profitItems);
                        }
                    }
                    while (cursor.moveToNext());
                }
            }
        }
        function2();
    }
    void function2(){
        if(arrayList1.isEmpty()){
            Message.message(context,"No profits found!");
            listView1.setAdapter(null);
            profit.setText("0.0");
        }
        else {
            ProfitsAdapter profitsAdapter = new ProfitsAdapter(context, arrayList1);
            listView1.setAdapter(profitsAdapter);
            profit.setText(pro+"");
        }
    }
    void toolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profits");
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

