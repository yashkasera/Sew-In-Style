/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewMeasurementsActivity extends AppCompatActivity {
    String person="",perName="",perId="",measurements="";
    MyDbAdapter myDbAdapter=new MyDbAdapter(this);
    String[] t=new String[11];
    String[] t2={"", "NECK","SHOULDER", "CHEST", "ARMHOLE", "SLEEVE", "MOHRI", "WAIST", "HIPS", "LENGTH", "BOTTOMLENGTH"};
    TextView neck,shoulder,chest,armhole,sleeve,mohri,waist,hips,length,bottomlength;
    Context context=this;
    Cursor cursor=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_measurements);
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        Message.message(context,perId);
        person=getIntent().getStringExtra("person");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(perName);
        getSupportActionBar().setSubtitle(person);
        neck=findViewById(R.id.neck);
        shoulder=findViewById(R.id.shoulder);
        chest=findViewById(R.id.chest);
        armhole=findViewById(R.id.armhole);
        sleeve=findViewById(R.id.sleeve);
        mohri=findViewById(R.id.mohri);
        waist=findViewById(R.id.waist);
        hips=findViewById(R.id.hips);
        length=findViewById(R.id.length);
        bottomlength=findViewById(R.id.bottomlength);
        Cursor cursor=myDbAdapter.getMeasurements(perId);
        if(cursor.getCount()==0)
        {
            Message.message(context,"No Measurements Found!");
            finish();
        }
        else {
            t[1] = cursor.getString(cursor.getColumnIndexOrThrow("NECK"));
            t[2] = cursor.getString(cursor.getColumnIndexOrThrow("SHOULDER"));
            t[3] = cursor.getString(cursor.getColumnIndexOrThrow("CHEST"));
            t[4] = cursor.getString(cursor.getColumnIndexOrThrow("ARMHOLE"));
            t[5] = cursor.getString(cursor.getColumnIndexOrThrow("SLEEVE"));
            t[6] = cursor.getString(cursor.getColumnIndexOrThrow("MOHRI"));
            t[7] = cursor.getString(cursor.getColumnIndexOrThrow("WAIST"));
            t[8] = cursor.getString(cursor.getColumnIndexOrThrow("HIPS"));
            t[9] = cursor.getString(cursor.getColumnIndexOrThrow("LENGTH"));
            t[10] = cursor.getString(cursor.getColumnIndexOrThrow("BOTTOMLENGTH"));
            neck.setText(t[1]);
            shoulder.setText(t[2]);
            chest.setText(t[3]);
            armhole.setText(t[4]);
            sleeve.setText(t[5]);
            mohri.setText(t[6]);
            waist.setText(t[7]);
            hips.setText(t[8]);
            length.setText(t[9]);
            bottomlength.setText(t[10]);
            findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    measurements += perName + "\n";
                    for (int i = 1; i <= 10; i++) {
                        if (!t[i].equalsIgnoreCase("0"))
                            measurements += t2[i] + " = " + t[i] + "\n";

                    }
                    measurements = measurements.trim();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, measurements);
                    intent.setPackage("com.whatsapp");
                    startActivity(intent);
                }
            });
            findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(context, InsertOrUpdateMeasurements.class);
                    intent.putExtra("person", person);
                    intent.putExtra("perName", perName);
                    intent.putExtra("perId", perId);
                    intent.putExtra("function", "Edit Measurements");
                    finish();
                    startActivity(intent);
                }
            });
        }
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
}
