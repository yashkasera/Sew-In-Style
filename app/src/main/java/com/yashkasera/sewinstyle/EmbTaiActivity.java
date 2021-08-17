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
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmbTaiActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Cursor cursor=null;
    String person="",perName="",perId="",per="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emb_tai);
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
                Intent intent = new Intent(context, GivePaymentActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = myDbAdapter.getPiecesForET(perName,per);
                if(cursor.getCount()==0){
                    Message.message(context,"No pieces found");
                }
                else {
                    Intent intent = new Intent(context, ViewPiecesActivity.class);
                    intent.putExtra("perName", perName);
                    intent.putExtra("person", person);
                    intent.putExtra("per", per);
                    intent.putExtra("perId", perId);
                    intent.putExtra("function", "ET");
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.l4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPaymentsActivity.class);
                intent.putExtra("perName", perName);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        findViewById(R.id.l5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog=new alertDialog(context,"Give Advance","Enter amount you want to give in advance");
                final TextInputLayout textInputLayout = dialog.textInputLayout;
                final TextInputEditText textInputEditText = dialog.textInputEditText;
                textInputLayout.setHint("Advance Amount");
                dialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(textInputEditText.getText().toString().length()==0){
                            textInputLayout.setError("Amount cannot be empty");
                        }
                        else{
                            final String pay=textInputEditText.getText().toString();
                            final alertDialog dialog1 = new alertDialog(context);
                            dialog1.setTitle("Confirm Payment", "Are you sure you want to give " + perName + " Rs." + pay + " as advance?", "Yes", "No");
                            dialog1.positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    double paytm = Double.parseDouble(pay);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String date = simpleDateFormat.format(new Date());
                                    myDbAdapter.addPayment(person,perId, perName, paytm, date, "As Advance", null, null);
                                    Cursor cursor=myDbAdapter.getPerson(person,perId);
                                    double a=cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                                    a=a-paytm;
                                    if (myDbAdapter.updatePersonPayment(perId, a, person)) {
                                        Message.message(context, "Successful");
                                        dialog1.dismiss();
                                        dialog.dismiss();
                                    }
                                    else{
                                        Message.message(context, "Unsuccessful");
                                    }
                                }
                            });
                            dialog1.negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                            Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
        findViewById(R.id.l6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog = new alertDialog(context);
                dialog.setTitle("Confirm clear dues", "Are you sure you want to clear all dues? This action cannot be undone", "Yes", "No");
                dialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final alertDialog dialog1=new alertDialog(context,"Confirm clear dues",false);
                        final TextInputEditText textInputEditText = dialog1.textInputEditText;
                        final TextInputLayout textInputLayout = dialog1.textInputLayout;
                        dialog1.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(textInputEditText.getText().toString().equalsIgnoreCase(String.valueOf(dialog1.random))){
                                    cursor = myDbAdapter.getPerson(person, perId);
                                    double paytm = cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String date = simpleDateFormat.format(new Date());
                                    myDbAdapter.addPayment(person,perId, perName, paytm, date, "Dues Cleared", null, null);
                                    boolean c = myDbAdapter.updatePersonPayment(perId, 0.0, person);
                                    myDbAdapter.updatePayStatusPerTableForAll(perName, per);
                                    if(c){
                                        dialog1.dismiss();
                                        dialog.dismiss();
                                        Message.message(context,"All dues cleared");
                                    }
                                    else
                                        Message.message(context,"Dues could not be cleared");
                                }
                                else{
                                    textInputLayout.setError("Invalid captcha! Try Again");
                                }
                            }
                        });
                        dialog1.show();
                        Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    }
                });
                dialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.dismiss.setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.l7).setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.l8).setOnClickListener(new View.OnClickListener() {
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
