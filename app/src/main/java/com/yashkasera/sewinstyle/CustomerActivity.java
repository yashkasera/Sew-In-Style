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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CustomerActivity extends AppCompatActivity {
    String person="",perName="",perId="",per="";
    long tel;
    MyDbAdapter myDbAdapter=new MyDbAdapter(this);
    Context context=this;
    Cursor cursor=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
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
        findViewById(R.id.l1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPersonInfoActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = myDbAdapter.getMeasurements(perId);
                if(cursor.getCount()==0)
                {
                    Intent intent = new Intent(context, ErrorActivity.class);
                    intent.putExtra("error","noMeasurement");
                    intent.putExtra("perName", perName);
                    intent.putExtra("person", person);
                    intent.putExtra("per", per);
                    intent.putExtra("perId", perId);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, ViewMeasurementsActivity.class);
                    intent.putExtra("perName", perName);
                    intent.putExtra("person", person);
                    intent.putExtra("per", per);
                    intent.putExtra("perId", perId);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.l3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPieceActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPiecesActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                intent.putExtra("function", "customer");
                startActivity(intent);
            }
        });
        findViewById(R.id.l5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddExpenseActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewExpensesActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                intent.putExtra("function", "customer");
                startActivity(intent);
            }
        });
        findViewById(R.id.l7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AcceptPaymentActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPaymentsActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                intent.putExtra("function", "1");
                startActivity(intent);
            }
        });
        findViewById(R.id.l9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditPersonActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeletePersonActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_cus_tai_emb);
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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_home:
                finish();
                intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.action_call:
                cursor = myDbAdapter.getPerson(person, perId);
                tel=cursor.getLong(cursor.getColumnIndexOrThrow("MOBILE"));
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tel));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            default:
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
