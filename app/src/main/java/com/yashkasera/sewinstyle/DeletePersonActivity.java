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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class DeletePersonActivity extends AppCompatActivity {
    String person="",perName="",perId="";
    Context context=this;
    Cursor cursor=null;
    int flag=0;
    TextView name,mobile,address,nop,payment,date,paymode,captcha;
    TextInputLayout cap1;
    TextInputEditText cap;
    int random;
    MyDbAdapter myDbAdapter=new MyDbAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_person);
        person=getIntent().getStringExtra("person");
        perName=getIntent().getStringExtra("perName");
        perId=getIntent().getStringExtra("perId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delete " + person);
        cursor = myDbAdapter.getPerson(person, perId);
        if(cursor.getCount()>0) {
            name = findViewById(R.id.name);
            address = findViewById(R.id.address);
            mobile = findViewById(R.id.mobile);
            nop = findViewById(R.id.nop);
            payment = findViewById(R.id.payment);
            date = findViewById(R.id.date);
            paymode = findViewById(R.id.pay_mode);
            captcha = findViewById(R.id.captcha);
            name.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            mobile.setText(cursor.getString(cursor.getColumnIndexOrThrow("MOBILE")));
            address.setText(cursor.getString(cursor.getColumnIndexOrThrow("ADDRESS")));
            date.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED")));
            paymode.setText(cursor.getString(cursor.getColumnIndexOrThrow("PAYMODE")));
            payment.setText(cursor.getString(cursor.getColumnIndexOrThrow("PAYMENT")));
            nop.setText(cursor.getString(cursor.getColumnIndexOrThrow("NOP")));
            cap1 = findViewById(R.id.cap1);
            cap = findViewById(R.id.cap);
            captcha.setText(captcha()+"");
            findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cap.getText().toString().equalsIgnoreCase(random+"")) {
                        final alertDialog dialog = new alertDialog(context);
                        Button yes = dialog.findViewById(R.id.positive);
                        Button no= dialog.findViewById(R.id.negative);
                        ImageView dismiss= dialog.findViewById(R.id.dismiss);
                        dialog.setTitle("Confirm Delete", "This action cannot be undone. Are you sure you still want to continue?", "Yes", "No");
                        yes.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                boolean a = myDbAdapter.deletePerson(perId, person, perName);
                                if (a) {
                                    Message.message(context, person+" deleted successfully");
                                    finish();
                                    startActivity(new Intent(context,DashboardActivity.class));
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
                        dismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.quitDialog(v);
                            }
                        });
                        dialog.show();
                        Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    } else {
                        cap1.setError("Invalid Captcha");
                        Toast.makeText(context, "Invalid Captcha", Toast.LENGTH_LONG).show();
                        captcha.setText(captcha()+"");
                    }
                }
            });
            findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    int captcha(){
        Random rand = new Random();
        random=rand.nextInt(9999);
        while(random<999)
            random=rand.nextInt(9999);
        return random;
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
