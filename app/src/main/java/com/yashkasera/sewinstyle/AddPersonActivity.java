/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddPersonActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    String per="",person="",perName="",perId="";
    int flag=0,flag1=0;
    TextInputEditText name,mobile,address,date;
    TextInputLayout name1,mobile1,address1,date1;
    TextView title;
    String Name,Mobile,Address,Date;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        person = getIntent().getStringExtra("person");
        per = getIntent().getStringExtra("per");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle(person+"");
        title = findViewById(R.id.title);
        title.setText("Add New "+person);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        name1 = findViewById(R.id.name1);
        mobile1 = findViewById(R.id.mobile1);
        address1 = findViewById(R.id.address1);
        date1 = findViewById(R.id.date1);
        datePicker=findViewById(R.id.datePicker);
        Calendar calendar=Calendar.getInstance();
        myDbAdapter=new MyDbAdapter(this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(sdf.format(new Date()));
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
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(name.getText().toString()))
                    name1.setError("Name cannot be empty");
                else if(!TextUtils.isEmpty(name.getText().toString()))
                    name1.setError(null);
                Cursor cursor=myDbAdapter.getPersons(person);
                String nam="",temp=name.getText().toString();flag=0;
                while (cursor.moveToNext()) {
                    nam = cursor.getString(cursor.getColumnIndex("NAME"));
                    if(nam.equalsIgnoreCase(temp))
                        flag=1;
                }
                if(flag==1) {
                    name1.setError("Already Exists");
                }
                else if(flag==0)
                    name1.setError(null);

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(name.getText().toString()))
                    name1.setError("Name cannot be Empty");
                else if(flag==0)
                    name1.setError(null);
            }
        });
        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(name.getText().toString()))
                    mobile1.setError("Mobile cannot be Empty");
                else if(mobile.getText().toString().length()!=10)
                    flag1=1;
           /*     if(flag1==1)
                    mobile1.setError("Invalid mobile number");
                else if(flag1==0)
                    mobile1.setError(null);

            */
                if(count==10)
                    mobile1.setError(null);
                else
                    mobile1.setError("Invalid mobile number");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(name.getText().toString()))
                    mobile1.setError("Mobile cannot be Empty");
                else if(flag1==0)
                    mobile1.setError(null);
                if(mobile.getText().toString().length()==10)
                    mobile1.setError(null);
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result;
                if (TextUtils.isEmpty(name.getText().toString())) {
                    name1.setError("Name cannot be Empty");
                    Toast.makeText(getApplicationContext(),"Name cannot be Empty",Toast.LENGTH_LONG).show();
                }
                else if(flag==1){
                    name1.setError("User already exists");
                    Toast.makeText(getApplicationContext(),name.getText().toString()+" already exists",Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(mobile.getText())||!(TextUtils.isDigitsOnly(mobile.getText()))) {
                    mobile1.setError("Invalid Mobile Number");
                    Toast.makeText(getApplicationContext(),"Invalid Mobile Number",Toast.LENGTH_LONG).show();
                }
                else if(mobile.getText().toString().length()!=10){
                    mobile1.setError("Invalid Mobile Number");
                    Toast.makeText(getApplicationContext(),"Invalid Mobile Number",Toast.LENGTH_LONG).show();
                }
                else {
                    String temp = mobile.getText().toString();
                    long tel = Long.parseLong(temp);

                    result = myDbAdapter.insertPerson((name.getText().toString()).toUpperCase(), tel, address.getText().toString(), 0, 0, null, date.getText().toString(), person);
                    perName=name.getText().toString().toUpperCase();
                    Cursor cursor1=myDbAdapter.getPersons(person);
                    cursor1.moveToLast();
                    perId=cursor1.getString(cursor1.getColumnIndexOrThrow("_id"));
                    cursor1.close();
                    if(per.equalsIgnoreCase("2")||per.equalsIgnoreCase("3"))
                    {
                        myDbAdapter.createTableET(per,name.getText().toString());
                    }
                    if (result) {
                        Toast.makeText(context, person+ " successfuly added!", Toast.LENGTH_LONG).show();
                        if(person.equalsIgnoreCase("Customer")){
                            final alertDialog dialog=new alertDialog(context);
                            dialog.setTitle("Add measurements","New Customer added successfully. Do you want to add measurements as well?", "ADD NOW","Later");
                            Button yes = dialog.findViewById(R.id.positive);
                            Button no= dialog.findViewById(R.id.negative);
                            ImageView dismiss= dialog.findViewById(R.id.dismiss);
                            yes.setOnClickListener(new Button.OnClickListener()
                            {
                                public void onClick(View v)     {
                                    Intent intent = new Intent(context, InsertOrUpdateMeasurements.class);
                                    intent.putExtra("person", person);
                                    intent.putExtra("per", per);
                                    intent.putExtra("perName", perName);
                                    intent.putExtra("perId",perId);
                                    intent.putExtra("function", "Add Measurements");
                                    dialog.dismiss();
                                    finish();
                                    startActivity(intent);
                                }
                            });
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    dialog.quitDialog(v);
//                                    startActivity(new Intent(context,DashboardActivity.class));
                                }
                            });
                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
//                                    startActivity(new Intent(context,DashboardActivity.class));
                                    dialog.quitDialog(v);
                                }
                            });
                            dialog.show();
                            Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        }
                        else{
                            finish();
                            Intent intent=new Intent(context,DashboardActivity.class);
                            startActivity(intent);
                        }
                    } else
                        Toast.makeText(context, "Unsuccessful!", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog = new alertDialog(context);
                Button yes = dialog.findViewById(R.id.positive);
                Button no= dialog.findViewById(R.id.negative);
                ImageView dismiss= dialog.findViewById(R.id.dismiss);
                dialog.setTitle("Confirm Cancel","Are you sure you want to cancel?","Yes","No");
                yes.setOnClickListener(new Button.OnClickListener()
                {
                    public void onClick(View v)     {
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
    }
}
