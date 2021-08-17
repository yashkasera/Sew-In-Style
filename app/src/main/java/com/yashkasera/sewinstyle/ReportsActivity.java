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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
public class ReportsActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Cursor cursor=null;
    String per="",person="",perName="",perId="",piece="", pieceId = "";
    Spinner spinner;
    Calendar calendar;
    TextView year,net_pieces,net_sale,net_paid,net_profit,net_received,net_complete,net_pending,profit_pieces,profit_inventory,available,available_cost,sold,sold_cost,tailors,customers,embroiders;
    String Year,Month, netPaid = "", netPieces = "", netSale = "", netProfit = "", netReceived = "", netComplete = "", netPending = "", profitPieces = "", profitInventory = "", Available = "", availableCost = "", Sold = "", soldCost = "", Tailors = "",Customers="", Embroiders = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        person = getIntent().getStringExtra("person");
        per = getIntent().getStringExtra("per");
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        spinner = findViewById(R.id.spinner);
        calendar = Calendar.getInstance();
        net_pieces=findViewById(R.id.net_pieces);
        net_sale=findViewById(R.id.net_sale);
        net_paid=findViewById(R.id.net_paid);
        net_profit=findViewById(R.id.net_profit);
        net_received = findViewById(R.id.net_received);
        net_complete = findViewById(R.id.net_complete);
        net_pending = findViewById(R.id.net_pending);
        profit_inventory = findViewById(R.id.profit_inventory);
        profit_pieces = findViewById(R.id.profit_pieces);
        available = findViewById(R.id.available);
        available_cost = findViewById(R.id.available_cost);
        customers = findViewById(R.id.customers);
        embroiders = findViewById(R.id.embroiders);
        tailors = findViewById(R.id.tailors);
        sold = findViewById(R.id.sold);
        sold_cost = findViewById(R.id.sold_cost);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        year = findViewById(R.id.year);
        year.setText("All");
        Year = calendar.get(Calendar.YEAR) + "";
        Month = "0";
        toolbar("Reports","");
        function();
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
        findViewById(R.id.profit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewProfitsActivity.class));
            }
        });
    }
    void function(){
        if(Month.equalsIgnoreCase("0")&&year.getText().toString().equalsIgnoreCase("all")){
            findViewById(R.id.l_avai).setVisibility(View.VISIBLE);
            findViewById(R.id.l_sold).setVisibility(View.VISIBLE);
            findViewById(R.id.card).setVisibility(View.VISIBLE);
            cursor = myDbAdapter.getPieces();
            if(cursor.getCount()==0){
                netPieces = cursor.getCount() + "";
                netSale = "0.0";
                netPending = "0";
                netComplete =  "";
            }
            else {
                netPieces = cursor.getCount() + "";
                cursor.moveToFirst();
                double sale = 0.0;
                int complete = 0, pending = 0;
                do {
                    if (cursor.getString(cursor.getColumnIndexOrThrow("STATUS")).equalsIgnoreCase("COMPLETED"))
                        complete++;
                    else
                        pending++;
                    sale += cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                }
                while (cursor.moveToNext());
                netSale = sale + "";
                netPending = pending + "";
                netComplete = complete + "";
            }
            cursor=myDbAdapter.getProfits();
            if(cursor.getCount()==0){
                profitPieces = "0.0";
            }
            else {
                double profit = 0.0;
                cursor.moveToFirst();
                do {
                    profit += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                }
                while (cursor.moveToNext());
                profitPieces = profit + "";
            }
            cursor=myDbAdapter.getAllExpense();
            cursor.moveToFirst();
            if(cursor.getCount()==0){
                netPaid = "0.0";
            }
            else {
                double paid = 0.0;
                do {
                    double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                    paid += amt;
                }
                while (cursor.moveToNext());
                netPaid = paid + "";
            }
            cursor=myDbAdapter.getPayments("CUSTOMER","person","");
            cursor.moveToFirst();
            if(cursor.getCount()==0){
                netReceived = "0.0";
            }
            else {
                double received = 0.0;
                do {
                    double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                    received += amt;
                }
                while (cursor.moveToNext());
                netReceived = received + "";
            }
            cursor = myDbAdapter.getInventory();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitInventory = "0.0";
                Sold = "0";
                soldCost = "0.0";
                Available = "0";
                availableCost = "0.0";
            }
            else{
                int s=0,a=0;
                double sc=0.0,ac=0.0,p=0.0;
                do {
                    String t = cursor.getString(cursor.getColumnIndex("SELLINGPRICE"));
                    if(t.equalsIgnoreCase("-")){
                        a++;
                    }
                    else{
                        s++;
                        sc+=cursor.getDouble(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
                        p += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                    }
                    ac += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
                }
                while (cursor.moveToNext());
                profitInventory = p + "";
                Sold = s + "";
                soldCost = sc + "";
                Available = a + "";
                availableCost = ac + "";
            }
            netProfit=(Double.parseDouble(profitInventory)+Double.parseDouble(profitPieces))+"";
            cursor = myDbAdapter.getPersons("Customer");
            Customers=cursor.getCount()+"";

            cursor = myDbAdapter.getPersons("Embroider");
            Embroiders = cursor.getCount() + "";

            cursor = myDbAdapter.getPersons("Tailor");
            Tailors = cursor.getCount() + "";
            setText();
        }
        else if(year.getText().toString().equalsIgnoreCase("All")) {
            findViewById(R.id.l_avai).setVisibility(View.GONE);
            findViewById(R.id.l_sold).setVisibility(View.GONE);
            findViewById(R.id.card).setVisibility(View.GONE);
            cursor = myDbAdapter.getPieces();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netPieces = cursor.getCount() + "";
                netSale = "0.0";
                netPending = "0";
                netComplete = "0";
            } else {
                int nop = 0, complete = 0, pending = 0;
                double sale = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        nop++;
                        if (cursor.getString(cursor.getColumnIndexOrThrow("STATUS")).equalsIgnoreCase("COMPLETED"))
                            complete++;
                        else
                            pending++;
                        sale += cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                    }
                }
                while (cursor.moveToNext());
                netPieces = nop + "";
                netSale = sale + "";
                netPending = pending + "";
                netComplete = complete + "";
            }
            cursor = myDbAdapter.getProfits();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitPieces = "0.0";
            } else {
                double profit = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        profit += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                    }
                }
                while (cursor.moveToNext());
                profitPieces = profit + "";
            }
            cursor = myDbAdapter.getAllExpense();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netPaid = "0.0";
            } else {
                double paid = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                        paid += amt;
                    }
                }
                while (cursor.moveToNext());
                netPaid = paid + "";
            }
            cursor = myDbAdapter.getPayments("CUSTOMER", "person", "");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netReceived = "0.0";
            } else {
                double received = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEPAID"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                        received += amt;
                    }
                }
                while (cursor.moveToNext());
                netReceived = received + "";
            }
            cursor = myDbAdapter.getInventory();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitInventory = "0.0";
                Sold = "0";
                soldCost = "0.0";
                Available = "0";
                availableCost = "0.0";
            } else {
                int s = 0, a = 0;
                double sc = 0.0, ac = 0.0, p = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        String t2 = cursor.getString(cursor.getColumnIndex("SELLINGPRICE"));
                        if (t2.equalsIgnoreCase("-")) {
                            a++;
                        } else {
                            s++;
                            sc += cursor.getDouble(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
                            p += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        }
                        ac += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
                    }
                }
                while (cursor.moveToNext());
                profitInventory = p + "";
                Sold = s + "";
                soldCost = sc + "";
                Available = a + "";
                availableCost = ac + "";
            }
            netProfit = (Double.parseDouble(profitInventory) + Double.parseDouble(profitPieces)) + "";
            cursor = myDbAdapter.getPersons("Customer");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Customers = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Customers = a + "";
            }
            cursor = myDbAdapter.getPersons("Embroider");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Embroiders = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Embroiders = a + "";
            }
            cursor = myDbAdapter.getPersons("Tailor");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Tailors = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    int temp = Integer.parseInt(t);
                    if (temp == Integer.parseInt(Month)) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Tailors = a + "";
            }

            setText();
        }

        else if(Month.equalsIgnoreCase("0")) {
            int y = Integer.parseInt(year.getText().toString());
            findViewById(R.id.l_avai).setVisibility(View.GONE);
            findViewById(R.id.l_sold).setVisibility(View.GONE);
            findViewById(R.id.card).setVisibility(View.GONE);
            cursor = myDbAdapter.getPieces();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netPieces = cursor.getCount() + "";
                netSale = "0.0";
                netPending = "0";
                netComplete = "0";
            } else {
                int nop = 0, complete = 0, pending = 0;
                double sale = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        nop++;
                        if (cursor.getString(cursor.getColumnIndexOrThrow("STATUS")).equalsIgnoreCase("COMPLETED"))
                            complete++;
                        else
                            pending++;
                        sale += cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                    }
                }
                while (cursor.moveToNext());
                netPieces = nop + "";
                netSale = sale + "";
                netPending = pending + "";
                netComplete = complete + "";
            }
            cursor = myDbAdapter.getProfits();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitPieces = "0.0";
            } else {
                double profit = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        profit += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                    }
                }
                while (cursor.moveToNext());
                profitPieces = profit + "";
            }
            cursor = myDbAdapter.getAllExpense();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netPaid = "0.0";
            } else {
                double paid = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                        paid += amt;
                    }
                }
                while (cursor.moveToNext());
                netPaid = paid + "";
            }
            cursor = myDbAdapter.getPayments("CUSTOMER", "person", "");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netReceived = "0.0";
            } else {
                double received = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEPAID"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                        received += amt;
                    }
                }
                while (cursor.moveToNext());
                netReceived = received + "";
            }
            cursor = myDbAdapter.getInventory();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitInventory = "0.0";
                Sold = "0";
                soldCost = "0.0";
                Available = "0";
                availableCost = "0.0";
            } else {
                int s = 0, a = 0;
                double sc = 0.0, ac = 0.0, p = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        String t2 = cursor.getString(cursor.getColumnIndex("SELLINGPRICE"));
                        if (t2.equalsIgnoreCase("-")) {
                            a++;
                        } else {
                            s++;
                            sc += cursor.getDouble(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
                            p += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        }
                        ac += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
                    }
                }
                while (cursor.moveToNext());
                profitInventory = p + "";
                Sold = s + "";
                soldCost = sc + "";
                Available = a + "";
                availableCost = ac + "";
            }
            netProfit = (Double.parseDouble(profitInventory) + Double.parseDouble(profitPieces)) + "";

            cursor = myDbAdapter.getPersons("Customer");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Customers = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Customers = a + "";
            }
            cursor = myDbAdapter.getPersons("Embroider");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Embroiders = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Embroiders = a + "";
            }
            cursor = myDbAdapter.getPersons("Tailor");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Tailors = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String t = date.substring(date.lastIndexOf("-")+1);
                    int temp = Integer.parseInt(t);
                    if (temp == y) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Tailors = a + "";
            }


            setText();
        }


        else{
            int y = Integer.parseInt(year.getText().toString());
            findViewById(R.id.l_avai).setVisibility(View.GONE);
            findViewById(R.id.l_sold).setVisibility(View.GONE);
            findViewById(R.id.card).setVisibility(View.GONE);
            cursor=myDbAdapter.getPieces();
            cursor.moveToFirst();
            if(cursor.getCount()==0){
                netPieces = cursor.getCount() + "";
                netSale = "0.0";
                netPending = "0";
                netComplete =  "0";
            }
            else {
                int nop = 0, complete = 0, pending = 0;
                double sale = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        nop++;
                        if (cursor.getString(cursor.getColumnIndexOrThrow("STATUS")).equalsIgnoreCase("COMPLETED"))
                            complete++;
                        else
                            pending++;
                        sale += cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
                    }
                }
                while (cursor.moveToNext());
                netPieces = nop + "";
                netSale = sale + "";
                netPending = pending + "";
                netComplete = complete + "";
            }
            cursor=myDbAdapter.getProfits();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitPieces = "0.0";
            }
            else {
                double profit = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        profit += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                    }
                }
                while (cursor.moveToNext());
                profitPieces = profit + "";
            }
            cursor=myDbAdapter.getAllExpense();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netPaid = "0.0";
            }
            else {
                double paid = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                        paid += amt;
                    }
                }
                while (cursor.moveToNext());
                netPaid = paid + "";
            }
            cursor=myDbAdapter.getPayments("CUSTOMER","person","");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                netReceived = "0.0";
            }
            else {
                double received = 0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEPAID"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                        received += amt;
                    }
                }
                while (cursor.moveToNext());
                netReceived = received + "";
            }
            cursor = myDbAdapter.getInventory();
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                profitInventory = "0.0";
                Sold = "0";
                soldCost = "0.0";
                Available = "0";
                availableCost = "0.0";
            }
            else{
                int s=0,a=0;
                double sc=0.0,ac=0.0,p=0.0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        String t2 = cursor.getString(cursor.getColumnIndex("SELLINGPRICE"));
                        if (t2.equalsIgnoreCase("-")) {
                            a++;
                        } else {
                            s++;
                            sc += cursor.getDouble(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
                            p += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
                        }
                        ac += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
                    }
                }
                while (cursor.moveToNext());
                profitInventory = p + "";
                Sold = s + "";
                soldCost = sc + "";
                Available = a + "";
                availableCost = ac + "";
            }
            netProfit=(Double.parseDouble(profitInventory)+Double.parseDouble(profitPieces))+"";

            cursor = myDbAdapter.getPersons("Customer");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Customers = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Customers = a + "";
            }
            cursor = myDbAdapter.getPersons("Embroider");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Embroiders = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Embroiders = a + "";
            }
            cursor = myDbAdapter.getPersons("Tailor");
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Tailors = "0";
            }
            else {
                int a = 0;
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
                    String m1 = date.substring((date.indexOf("-") + 1), date.lastIndexOf("-"));
                    String y1 = date.substring(date.lastIndexOf("-") + 1);
                    int temp = Integer.parseInt(m1);
                    if (temp == Integer.parseInt(Month)&&y==Integer.parseInt(y1)) {
                        a++;
                    }
                }
                while (cursor.moveToNext());
                Tailors = a + "";
            }

            setText();



        }
    }
    void setText(){
        net_paid.setText(netPaid);
        net_pieces.setText(netPieces);
        net_sale.setText(netSale);
        net_profit.setText(netProfit);
        net_received.setText(netReceived);
        net_complete.setText(netComplete);
        net_pending.setText(netPending);
        profit_pieces.setText(profitPieces);
        profit_inventory.setText(profitInventory);
        sold.setText(Sold);
        available.setText(Available);
        sold_cost.setText(soldCost);
        available_cost.setText(availableCost);
        customers.setText(Customers);
        embroiders.setText(Embroiders);
        tailors.setText(Tailors);
    }
    void toolbar(String title,String subtitle){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
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
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
