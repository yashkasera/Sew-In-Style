/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewPersonInfoActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    Cursor cursor=null;
    String per="",person="",perName="",perId="";
    TextView name,category,mobile,address,payment,paymode,nop,date,received,inProcess,completed,received1,inProcess1,completed1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_person_info);
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        per = getIntent().getStringExtra("per");
        person=getIntent().getStringExtra("person");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(perName);
        getSupportActionBar().setSubtitle(person);
        cursor=myDbAdapter.getPerson(person,perId);
        name=findViewById(R.id.name);
        category=findViewById(R.id.category);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        nop = findViewById(R.id.nop);
        payment = findViewById(R.id.payment);
        paymode = findViewById(R.id.pay_mode);
        date = findViewById(R.id.date);
        received = findViewById(R.id.received);
        inProcess = findViewById(R.id.inProcess);
        completed = findViewById(R.id.completed);
        received1 = findViewById(R.id.received1);
        inProcess1 = findViewById(R.id.inProcess1);
        completed1 = findViewById(R.id.completed1);
        name.setText(perName);
        category.setText(person);
        mobile.setText(cursor.getString(cursor.getColumnIndexOrThrow("MOBILE")));
        address.setText(cursor.getString(cursor.getColumnIndex("ADDRESS")));
        double pay = cursor.getDouble(cursor.getColumnIndex("PAYMENT"));
        payment.setText(String.valueOf(Math.abs(pay)));
        date.setText(cursor.getString(cursor.getColumnIndex("DATEADDED")));
        nop.setText(cursor.getString(cursor.getColumnIndex("NOP")));
        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.message(context,"Call "+perName);
                call(cursor.getLong(cursor.getColumnIndexOrThrow("MOBILE")));
            }
        });
        if(person.equalsIgnoreCase("Customer"))
            function(pay);
        else{
            if(pay==0.0)
                paymode.setText("All Payments Cleared");
            else if(pay>0)
                paymode.setText("Due On Delivery");
            else
                paymode.setText("Paid");
            received.setVisibility(View.GONE);
            received1.setVisibility(View.GONE);
            inProcess.setVisibility(View.GONE);
            inProcess1.setVisibility(View.GONE);
            completed.setVisibility(View.GONE);
            completed1.setVisibility(View.GONE);
        }
    }
    private void function(double pay){
        if(pay==0.0)
            paymode.setText("All Payments Cleared");
        else if(pay>0)
            paymode.setText("Paid");
        else
            paymode.setText("Due On Delivery");
        int a=0,b=0,c=0;
        Cursor cursor = myDbAdapter.getPiecesOf(perId);
        if(cursor.getCount()!=0) {
            do {
                String status=cursor.getString(cursor.getColumnIndexOrThrow("STATUS"));
                if (status.equalsIgnoreCase("Received"))
                    a++;
                else if(status.equalsIgnoreCase("In Process"))
                    b++;
                else{
                    c++;
                }
            }
            while (cursor.moveToNext());
        }
        received.setText(String.valueOf(a));
        inProcess.setText(String.valueOf(b));
        completed.setText(String.valueOf(c));
    }
    protected void edit(String id){
        Intent intent = new Intent(context, EditPersonActivity.class);
        intent.putExtra("person", person);
        intent.putExtra("per", "per");
        intent.putExtra("perName", perName);
        intent.putExtra("perId", perId);
        finish();
        startActivity(intent);
    }
    protected void delete(String id){
        Intent intent = new Intent(context, DeletePersonActivity.class);
        intent.putExtra("person", person);
        intent.putExtra("per", "per");
        intent.putExtra("perId", perId);
        intent.putExtra("perName", perName);
        finish();
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_person_info);
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
            case R.id.edit:
                edit(perId);
                break;
            case R.id.delete:
                delete(perId);
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
    protected void call(long tel){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tel));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
