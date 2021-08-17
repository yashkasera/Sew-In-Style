/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditPersonActivity extends AppCompatActivity {
    String person="",perName="",perId="";int flag=0,flag1=0;
    TextView oName,oMobile,oAddress,odateAdded;
    TextInputEditText name,mobile,address;
    TextInputLayout name1,mobile1,address1;
    Context context=this;
    Cursor cursor=null;
    MyDbAdapter myDbAdapter=new MyDbAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);
        person=getIntent().getStringExtra("person");
        perName=getIntent().getStringExtra("perName");
        perId=getIntent().getStringExtra("perId");
        cursor = myDbAdapter.getPerson(person, perId);
        if(cursor.getCount()>0) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Update " + person);
            oName = findViewById(R.id.oName);
            oAddress = findViewById(R.id.oAddress);
            oMobile = findViewById(R.id.oMobile);
            odateAdded = findViewById(R.id.odateAdded);
            name = findViewById(R.id.name);
            name1 = findViewById(R.id.name1);
            name.setText(perName);
            oName.setText(perName);
            mobile = findViewById(R.id.mobile);
            address = findViewById(R.id.address);
            mobile1 = findViewById(R.id.mobile1);
            address1 = findViewById(R.id.address1);
            oName.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            name.setText(cursor.getString(cursor.getColumnIndex("NAME")));
            oMobile.setText(cursor.getString(cursor.getColumnIndex("MOBILE")));
            mobile.setText(cursor.getString(cursor.getColumnIndex("MOBILE")));
            oAddress.setText(cursor.getString(cursor.getColumnIndex("ADDRESS")));
            odateAdded.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED")));
            address.setText(cursor.getString(cursor.getColumnIndex("ADDRESS")));
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
                    if(flag1==1)
                        mobile1.setError("Invalid mobile number");
                    else if(flag1==0)
                        mobile1.setError(null);
                    if(count==10)
                        mobile1.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(name.getText().toString()))
                        mobile1.setError("Mobile cannot be Empty");
                    else if(flag1==0)
                        mobile1.setError(null);
                    if(mobile.getText().toString().length()==10)
                        mobile1.setError(null);
                    if (!oMobile.getText().toString().equalsIgnoreCase(s.toString()))
                        flag = 1;
                }
            });
            address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!oAddress.getText().toString().equalsIgnoreCase(s.toString()))
                        flag = 1;
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
                    String nam="",temp=name.getText().toString();flag1=0;
                    while (cursor.moveToNext()) {
                        nam = cursor.getString(cursor.getColumnIndex("NAME"));
                        if(nam.equalsIgnoreCase(temp))
                            flag1=1;
                    }
                    if(flag1==1&&!(name.getText().toString().equalsIgnoreCase(oName.getText().toString()))) {
                        name1.setError("Already Exists");
                    }
                    else if(flag1==0)
                        name1.setError(null);

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(name.getText().toString()))
                        name1.setError("Name cannot be Empty");
                    else if(flag1==0)
                        name1.setError(null);
                    if (!oName.getText().toString().equalsIgnoreCase(s.toString()))
                        flag = 1;
                }
            });
            Button update = findViewById(R.id.update);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag != 0) {
                        final alertDialog dialog = new alertDialog(context);
                        Button yes = dialog.findViewById(R.id.positive);
                        Button no= dialog.findViewById(R.id.negative);
                        ImageView dismiss= dialog.findViewById(R.id.dismiss);
                        dialog.setTitle("Update", "All previous data will be lost. Are you sure you still want to continue?", "Yes", "No");
                        yes.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View v) {
                                boolean a = myDbAdapter.updateInfo(perId, name.getText().toString(), Long.parseLong(mobile.getText().toString()), address.getText().toString(), person);
                                if (a) {
                                    Message.message(context, "Details updated successfully");
                                    finish();
                                    Intent intent=new Intent(context,CusTaiEmbActivity.class);
                                    intent.putExtra("person",person);
                                    intent.putExtra("perName", perName);
                                    intent.putExtra("id",perId);
                                    startActivity(intent);
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
                        Toast.makeText(context, "No change observed", Toast.LENGTH_LONG).show();
                        finish();
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
        else{
            finish();
     //       Message.message(context,"No record found!");
        }
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
    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
