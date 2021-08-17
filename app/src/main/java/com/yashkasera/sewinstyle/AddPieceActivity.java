/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddPieceActivity extends AppCompatActivity {
    String person="",perName="",perId="",day,month,year,day1,month1,year1,stat="",desc="",ExType="",ExName="",ExPerId,PieceId,Piece;
    MyDbAdapter myDbAdapter=new MyDbAdapter(this);
    Context context=this;
    double price;
    TextInputLayout dateAdded1,dateExpected1,description1,piece1;
    TextInputEditText dateAdded,dateExpected,description,piece;
    TextView exname,extype,cost,exname1,extype1,cost1,cusName;
    ImageButton date1,date2;
    int mYear, mMonth, mDay,mYear1, mMonth1, mDay1;
    Button save,change;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Cursor cursor;
    int selectedId,flag=0,flag1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_piece);
        person="Customer";
        perId = getIntent().getStringExtra("perId");
        perName = getIntent().getStringExtra("perName");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Piece");
        getSupportActionBar().setSubtitle(person);
        dateAdded=findViewById(R.id.dateAdded);
        dateAdded1 = findViewById(R.id.dateAdded1);
        dateExpected=findViewById(R.id.dateExpected);
        dateExpected1=findViewById(R.id.dateExpected1);
        piece=findViewById(R.id.piece);
        piece1=findViewById(R.id.piece1);
        cusName=findViewById(R.id.cusName);
        if (perName != null) {
            if(perName.length()==0)
                cusName.setText("Not Selected");
            else{
                cusName.setText(perName);
            }
        }
        else{
            cusName.setText("Not Selected");
        }
        description=findViewById(R.id.description);
        description1=findViewById(R.id.description1);
        cost=findViewById(R.id.cost);
        cost1=findViewById(R.id.cost1);
        exname=findViewById(R.id.exname);
        exname1=findViewById(R.id.exname1);
        extype=findViewById(R.id.extype);
        extype1=findViewById(R.id.extype1);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        save = findViewById(R.id.save);
        change = findViewById(R.id.change);
        extype1.setVisibility(View.GONE);
        extype.setVisibility(View.GONE);
        cost.setVisibility(View.GONE);
        cost1.setVisibility(View.GONE);
        radioGroup = findViewById(R.id.radioGroup);
        exname.setText("ME");
        extype.setText("ME");
        cost.setText("0.0");
        findViewById(R.id.cusName1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=findViewById(checkedId);
                if(radioButton.getText().toString().equalsIgnoreCase("In Process")){
                    extype1.setVisibility(View.VISIBLE);
                    extype.setVisibility(View.VISIBLE);
                    cost.setVisibility(View.VISIBLE);
                    cost1.setVisibility(View.VISIBLE);
                    cost.setText("0.0");
                    extype.setText("-");
                    exname.setText("-");
                    flag=1;
                }
                else if(radioButton.getText().toString().equalsIgnoreCase("Completed")){
                    cost.setVisibility(View.GONE);
                    cost1.setVisibility(View.GONE);
                    extype1.setVisibility(View.VISIBLE);
                    extype.setVisibility(View.VISIBLE);
                    extype.setText("CUSTOMER");
                    exname.setText(perName);
                    cost.setText("0.0");
                }
                else{
                    extype1.setVisibility(View.GONE);
                    extype.setVisibility(View.GONE);
                    cost.setVisibility(View.GONE);
                    cost1.setVisibility(View.GONE);
                    cost.setText("0.0");
                    extype.setText("-");
                    exname.setText("ME");
                    flag=0;

                }
                stat=radioButton.getText().toString().toUpperCase();
            }
        });
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dateAdded.setText(sdf.format(new Date()));
        final Calendar calendar=Calendar.getInstance();
        calendar.set(mYear,mMonth,mDay);
        calendar.add(Calendar.DATE,7);
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
        day1 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month1 = String.valueOf(calendar.get(Calendar.MONTH)+1);
        year1 = String.valueOf(calendar.get(Calendar.YEAR));
        if(day.length()==1)
            day="0"+day;
        if(month.length()==1)
            month="0"+month;
        dateExpected.setText(day+"-"+month+"-"+year);
        dateDifferenceCounter();
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDay=dayOfMonth;
                        mYear=year;
                        mMonth=monthOfYear+1;
                        day = String.valueOf(dayOfMonth);
                        month = String.valueOf(monthOfYear + 1);
                        if(day.length()==1)
                            day="0"+day;
                        if(month.length()==1)
                            month="0"+month;
                        dateAdded.setText(day+"-"+month+"-"+year);
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(mYear,mMonth,mDay);
                        calendar.add(Calendar.DATE,7);
                        day1 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                        month1 = String.valueOf(calendar.get(Calendar.MONTH));
                        year1 = String.valueOf(calendar.get(Calendar.YEAR));
                        if(day1.length()==1)
                            day1="0"+day1;
                        if(month1.length()==1)
                            month1="0"+month1;
                        dateExpected.setText(day1+"-"+month1+"-"+year1);
                    }
                },mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        final Calendar c1 = Calendar.getInstance();
        mYear1 = c1.get(Calendar.YEAR);
        mMonth1 = c1.get(Calendar.MONTH)+1;
        mDay1 = c1.get(Calendar.DAY_OF_MONTH);
        c1.set(mYear1,mMonth1,mDay1);
        c1.add(Calendar.DATE,7);
        mYear1 = c1.get(Calendar.YEAR);
        mMonth1 = c1.get(Calendar.MONTH)+1;
        mDay1 = c1.get(Calendar.DAY_OF_MONTH);
        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day1 = String.valueOf(dayOfMonth);
                        String month1 = String.valueOf(monthOfYear+1);
                        if(day1.length()==1)
                            day1="0"+day1;
                        if(month1.length()==1)
                            month1="0"+month1;
                        final Calendar c1 = Calendar.getInstance();
                        c1.set(year,monthOfYear,dayOfMonth);
                        c1.add(Calendar.DATE,7);
                        mYear1 = c1.get(Calendar.YEAR);
                        mMonth1 = c1.get(Calendar.MONTH);
                        mDay1 = c1.get(Calendar.DAY_OF_MONTH);
                        dateExpected.setText(day1+"-"+month1+"-"+year);
                        dateDifferenceCounter();

                    }
                },mYear1, mMonth1, mDay1);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = dateExpected.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                calendar1.add(Calendar.DATE,1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                dateExpected.setText(day+"-"+month+"-"+mYear1);
                dateDifferenceCounter();
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = dateExpected.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                calendar1.add(Calendar.DATE,-1);
                mYear1 = calendar1.get(Calendar.YEAR);
                mMonth1 = calendar1.get(Calendar.MONTH);
                mDay1 = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay1);
                String month = String.valueOf(mMonth1);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                dateExpected.setText(day+"-"+month+"-"+mYear1);
                dateDifferenceCounter();
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

                if(description.getText().toString().length()!=0)
                    description.setText(description.getText().toString().toUpperCase());
                if(TextUtils.isEmpty(cusName.getText().toString())||cusName.getText().toString().equalsIgnoreCase("Not Selected")) {
                    cusName.setError("Cannot be Empty");
                    Message.message(context,"Select a customer");
                }
                else if(TextUtils.isEmpty(piece.getText().toString())||piece.getText().toString().equalsIgnoreCase("Not Selected"))
                    piece1.setError("Piece cannot be empty");
                else if(flag==1&&flag1!=1){
                        Message.message(context, "One or more fields are empty");
                        exname.setError("Required");
                        extype.setError("Reqiured");
                }
                else{
                    ExName=exname.getText().toString().toUpperCase();
                    ExType=extype.getText().toString().toUpperCase();
                    price = Double.parseDouble(cost.getText().toString());
                    boolean result;
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedId);
                    stat=radioButton.getText().toString().toUpperCase();
                    String nop="";
                    perName=cusName.getText().toString();
                    result = myDbAdapter.addPiece(perId,perName,(piece.getText().toString()).toUpperCase(),Double.parseDouble(cost.getText().toString()), 0.0, description.getText().toString(), stat, ExName,ExType,dateAdded.getText().toString(), dateExpected.getText().toString(),"DUE");
                    cursor=myDbAdapter.getPieces();
                    cursor.moveToLast();
                    PieceId=cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    if(!ExName.equalsIgnoreCase("ME")&&!stat.equalsIgnoreCase("COMPLETED")) {
                        myDbAdapter.addExpense(perId, perName, PieceId, piece.getText().toString(), dateAdded.getText().toString(), desc, cost.getText().toString());
                    }
                    if(ExType.equalsIgnoreCase("EMBROIDER")||ExType.equalsIgnoreCase("TAILOR")) {
                        if(ExType.equalsIgnoreCase("EMBROIDER"))
                            myDbAdapter.addPieceForPerson("2", ExName,perId, perName, piece.getText().toString(), PieceId, price, dateAdded.getText().toString(), "Due on Delivery","IN PROCESS");
                        if(ExType.equalsIgnoreCase("TAILOR"))
                            myDbAdapter.addPieceForPerson("3", ExName,perId, perName, piece.getText().toString(), PieceId, price, dateAdded.getText().toString(), "Due on Delivery","IN PROCESS");
                        Cursor cursor = myDbAdapter.getPerson(ExType, ExPerId);

                        double payment=cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                        payment+=price;
                        myDbAdapter.updatePersonPayment(perId, payment, ExType);
                        cursor.close();
                    }
                    Cursor cursor=myDbAdapter.getPerson(person,perId);
                    nop=cursor.getString(cursor.getColumnIndexOrThrow("NOP"));
                    int no=Integer.parseInt(nop);
                    no++;
                    nop=String.valueOf(no);
                    myDbAdapter.updateNOP(perId,person,perName,nop);
                    if(ExType.equalsIgnoreCase("EMBROIDER")||ExType.equalsIgnoreCase("TAILOR")) {
                        cursor = myDbAdapter.getPerson(ExType,ExPerId);
                        nop = cursor.getString(cursor.getColumnIndexOrThrow("NOP"));
                        no = Integer.parseInt(nop);
                        no++;
                        nop = String.valueOf(no);
                        myDbAdapter.updateNOP(ExPerId,ExType, ExName, nop);
                    }
                    if (result) {
                        Toast.makeText(AddPieceActivity.this, "Successful", Toast.LENGTH_LONG).show();
                        finish();
                    } else
                        Toast.makeText(AddPieceActivity.this, "UnSuccessful", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cusName.setError(null);
                if(stat.equalsIgnoreCase("IN PROCESS")) {
                    byte t = 0;
                    final alertDialog dialog = new alertDialog(context, t);
                    dialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedId = dialog.radioGroup.getCheckedRadioButtonId();
                            final RadioButton radioButton1 = dialog.findViewById(selectedId);
                            if (selectedId > 0) {
                                if (radioButton1.getText().toString().equalsIgnoreCase("Embroider") || radioButton1.getText().toString().equalsIgnoreCase("Tailor")) {
                                    final Cursor cursor = myDbAdapter.getPersons(radioButton1.getText().toString());
                                    if (cursor.getCount() != 0) {
                                        String[] columns = {"NAME"};
                                        int[] to = {R.id.textView};
                                        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
                                        final alertDialog dialog1 = new alertDialog(context, simpleCursorAdapter, "Choose " + radioButton1.getText().toString(), "Select a " + radioButton1.getText().toString() + " from the list to whom you want to give this piece to.");
                                        final ListView listView=dialog1.listView;
                                     //   listView.setAdapter(simpleCursorAdapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                                ExType=radioButton1.getText().toString().toUpperCase();
                                                final alertDialog dialog2;
                                                if(ExType.equalsIgnoreCase("Embroider"))
                                                    dialog2 = new alertDialog(context, "Enter Cost", "Enter cost for Embroidery");
                                                else
                                                    dialog2 = new alertDialog(context, "Enter Cost", "Enter cost for Stitching");
                                                final TextInputLayout co1 = dialog2.textInputLayout;
                                                final TextInputEditText co = dialog2.textInputEditText;
                                                dialog2.positive.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if(TextUtils.isEmpty(co.getText()))
                                                            co1.setError("Cost cannot be empty");
                                                        else{
                                                            flag1=1;
                                                            cost.setText(co.getText().toString());
                                                            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                                            ExPerId=cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                                                            ExName=cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                                                            ExName.toUpperCase();
                                                            if(ExType.equalsIgnoreCase("Embroider"))
                                                                desc="Embroidery :"+co.getText().toString();
                                                            else
                                                                desc="Stitching :"+co.getText().toString();
                                                            desc=desc.toUpperCase();
                                                            function(ExType,ExName,cost.getText().toString());
                                                            dialog2.quitDialog(v);
                                                            dialog1.quitDialog(v);
                                                            dialog.quitDialog(v);
                                                        }
                                                    }
                                                });
                                                dialog2.negative.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog2.dismiss();
                                                    }
                                                });
                                                dialog2.dismiss.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog2.dismiss();
                                                    }
                                                });
                                                dialog2.show();
                                                Window window = dialog2.getWindow();
                                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                            }
                                        });
                                        dialog1.show();
                                        Window window = dialog.getWindow();
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    } else
                                        Message.message(context, "No " + radioButton1.getText().toString() + "s found");
                                } else if (radioButton1.getText().toString().equalsIgnoreCase("Other")) {
                                    final alertDialog dialog1 = new alertDialog(context, "Others-Please Specify", "Please enter the name of the person you want to give this piece to", "DONE", "CANCEL");
                                    final TextInputLayout name1 = dialog1.name1;
                                    final TextInputEditText name = dialog1.name;

                                    final TextInputLayout cost1 = dialog1.cost1;
                                    final TextInputEditText cost = dialog1.cost;

                                    final TextInputLayout descri1 = dialog1.description1;
                                    final TextInputEditText descri = dialog1.description;
                                    name1.setHint("Enter Name");
                                    name1.setErrorEnabled(true);
                                    Button positive = dialog1.positive;
                                    positive.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (TextUtils.isEmpty(name.getText().toString()))
                                                name1.setError("No Name entered");
                                            else if (TextUtils.isEmpty(cost.getText().toString()))
                                                cost1.setError("No Name entered");

                                            else {
                                                flag1=1;
                                                ExType = "Other".toUpperCase();
                                                ExName = name.getText().toString().toUpperCase();
                                                String Cost = cost.getText().toString();
                                                if (TextUtils.isEmpty(description.getText().toString()))
                                                    desc = "Paid to " + ExName;
                                                dialog1.quitDialog(v);
                                                dialog.dismiss();
                                                function(ExType, ExName, Cost);
                                            }
                                        }
                                    });
                                    Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                }else{
                                    dialog.quitDialog(v);
                                    function("-","Me","0.0");
                                }
                            } else
                                Message.message(context, "Select a category");
                        }
                    });
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                }
                else{
                    Message.message(context,"With can only be specified when piece is in process");
                }
            }
        });
    }
    String customer(){
        cursor=myDbAdapter.getPersons("Customer");
        if(cursor.getCount()==0)
        {
            Message.message(context,"No Customers Found!");
        }
        else if(cursor.getCount()==1){
            Message.message(context,"One Customer found, selected");
            perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            cusName.setText(perName);
            perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        }
        else {
            String[] columns = {"NAME"};
            int[] to = {R.id.textView};
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
            final alertDialog dialog1 = new alertDialog(context, simpleCursorAdapter, "Choose Customer", "Select the customer for whom you want to add this expense");
            final ListView listView = dialog1.listView;
            listView.setAdapter(simpleCursorAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    cusName.setText(perName);
                    perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    dialog1.dismiss();
                }
            });
            dialog1.dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                }
            });
            dialog1.show();
            Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        return perId;
    }
    public void dateDifferenceCounter(){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = simpleDateFormat.parse(dateAdded.getText().toString());
            Date date2 = simpleDateFormat.parse(dateExpected.getText().toString());
            Long difference =  (date2.getTime() - date1.getTime());
            Long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);
            if(differenceDates>=0) {
                TextView noDays=findViewById(R.id.noDays);
                noDays.setText(dayDifference);
                dateExpected1.setError(null);
            }
            else{
                dateExpected1.setError("Expected date cannot be before date added.");
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return;
    }
    public void function(String ExType1,String ExName1,String Cost1){
        extype.setVisibility(View.VISIBLE);
        exname.setVisibility(View.VISIBLE);
        cost.setVisibility(View.VISIBLE);
        extype1.setVisibility(View.VISIBLE);
        exname1.setVisibility(View.VISIBLE);
        cost1.setVisibility(View.VISIBLE);
        extype.setText(ExType1);
        exname.setText(ExName1);
        cost.setText(Cost1);
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
                finish();
                AddPieceActivity.super.onBackPressed();
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
