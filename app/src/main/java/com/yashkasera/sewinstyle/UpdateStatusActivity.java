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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class UpdateStatusActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    Cursor cursor;
    TextView cusName,piece,status,with,exname,extype,cost,exname1,extype1,cost1,description1,description;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextInputLayout dateExpected1;
    TextInputEditText dateExpected;
    String person="",perName="",perId="",Piece,pieceId,ExName,ExType,stat,ExPerId,desc,ExStatus1="",ExStatus2="";
    int mYear, mMonth, mDay,flag=0;
    double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Piece");
        getSupportActionBar().setSubtitle("Update Status");
        perName = getIntent().getStringExtra("perName");
        person = getIntent().getStringExtra("person");
        Piece = getIntent().getStringExtra("piece");
        pieceId = getIntent().getStringExtra("pieceId");
        perId = getIntent().getStringExtra("perId");
        cost = findViewById(R.id.cost);
        status = findViewById(R.id.status);
        extype = findViewById(R.id.extype);
        exname = findViewById(R.id.exname);
        cusName = findViewById(R.id.cusName);
        piece = findViewById(R.id.piece);
        with = findViewById(R.id.with);
        dateExpected=findViewById(R.id.dateExpected);
        dateExpected1=findViewById(R.id.dateExpected1);
        cost1=findViewById(R.id.cost1);
        extype1=findViewById(R.id.extype1);
        exname1=findViewById(R.id.exname1);
        description=findViewById(R.id.description);
        description1=findViewById(R.id.description1);
        radioGroup=findViewById(R.id.radioGroup);
        cursor = myDbAdapter.getPiece(Integer.parseInt(pieceId));
        if(cursor.getString(cursor.getColumnIndexOrThrow("STATUS")).equalsIgnoreCase("Completed"))
        {
            finish();
            Message.message(context,"Completed Pieces cannot be updated!");
        }
        cusName.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
        piece.setText(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
        status.setText(cursor.getString(cursor.getColumnIndexOrThrow("STATUS")));
        stat=status.getText().toString();
        dateExpected.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATEEXPECTED")));
        String t = dateExpected.getText().toString();
        int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
        int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
        int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
        Calendar calendar1=Calendar.getInstance();
        calendar1.set(y,m,d);
        mYear = calendar1.get(Calendar.YEAR);
        mMonth = calendar1.get(Calendar.MONTH);
        mDay = calendar1.get(Calendar.DAY_OF_MONTH);
        ExName=cursor.getString(cursor.getColumnIndexOrThrow("EXNAME"));
        ExType=cursor.getString(cursor.getColumnIndexOrThrow("EXTYPE"));
        if(status.getText().toString().equalsIgnoreCase("RECEIVED")){
            extype1.setVisibility(View.GONE);
            extype.setVisibility(View.GONE);
            cost.setVisibility(View.GONE);
            cost1.setVisibility(View.GONE);
            description1.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            cost.setText("0.0");
            extype.setText("-");
            exname.setText("ME");
            radioButton = findViewById(R.id.radioButton);
            radioButton.setChecked(true);
        }
        else if(status.getText().toString().equalsIgnoreCase("Completed")){
            cost.setVisibility(View.GONE);
            cost1.setVisibility(View.GONE);
            description1.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            extype1.setVisibility(View.VISIBLE);
            extype.setVisibility(View.VISIBLE);
            extype.setText("CUSTOMER");
            exname.setText(perName);
            cost.setText("0.0");
            radioButton = findViewById(R.id.radioButton2);
            radioButton.setChecked(true);
        }
        else{
            extype1.setVisibility(View.VISIBLE);
            extype.setVisibility(View.VISIBLE);
            cost.setVisibility(View.VISIBLE);
            cost1.setVisibility(View.VISIBLE);
            description1.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            extype.setText(ExType);
            exname.setText(ExName);
            if(ExType.equalsIgnoreCase("EMBROIDER")||ExType.equalsIgnoreCase("TAILOR")) {
                flag=1;
                ExStatus1=ExType;
                ExStatus2=ExName;
                if (ExType.equalsIgnoreCase("Embroider"))
                    cursor = myDbAdapter.getPiecesForET(ExName, "2");
                else if (ExType.equalsIgnoreCase("Tailor"))
                    cursor = myDbAdapter.getPiecesForET(ExName, "3");
                while (cursor.moveToNext()) {
                    if (cursor.getString(cursor.getColumnIndexOrThrow("PIECEID")).equalsIgnoreCase(pieceId)) {
                        cost.setText(cursor.getString(cursor.getColumnIndexOrThrow("COST")));
                    }
                }
            }
            radioButton = findViewById(R.id.radioButton1);
            radioButton.setChecked(true);
        }
        if(!ExName.equalsIgnoreCase("me"))
            with.setText(ExType + " - " + ExName);
        else
            with.setText(ExName);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=findViewById(checkedId);
                if(radioButton.getText().toString().equalsIgnoreCase("In Process")){
                    extype1.setVisibility(View.VISIBLE);
                    extype.setVisibility(View.VISIBLE);
                    cost.setVisibility(View.VISIBLE);
                    cost1.setVisibility(View.VISIBLE);
                    description1.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    cost.setText("0.0");
                    extype.setText("-");
                    exname.setText("-");
                    description.setText("-");
                }
                else if(radioButton.getText().toString().equalsIgnoreCase("Completed")){
                    cost.setVisibility(View.GONE);
                    cost1.setVisibility(View.GONE);
                    description1.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
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
                    description1.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                    cost.setText("0.0");
                    extype.setText("-");
                    exname.setText("ME");
                }
                stat=radioButton.getText().toString().toUpperCase();
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
        findViewById(R.id.cusName1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor=myDbAdapter.getPersons("Customer");
                if(cursor.getCount()==0)
                {
                    Message.message(context,"No Customers Found!");
                }
                else if(cursor.getCount()==1){
                    Message.message(context,"One Customer found, selected");
                    String c = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    cusName.setText(c);
                    perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    piece.setText("Not Selected");
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
                            String c = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                            perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                            cusName.setText(c);
                            piece.setText("Not Selected");
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
            }
        });
        findViewById(R.id.piece1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor=myDbAdapter.getPiecesOfIncomplete(perId);
                if(cursor.getCount()==0)
                {
                    Message.message(context,"No Pieces Found!");
                }
                else if(cursor.getCount()==1){
                    Message.message(context,"One piece found, selected");
                    Piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                    pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    piece.setText(Piece);
                }
                else {
                    String[] columns = {"PIECE"};
                    int[] to = {R.id.textView};
                    SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
                    final alertDialog dialog1 = new alertDialog(context, simpleCursorAdapter, "Choose Piece", "Select the piece for which you want to add this expense");
                    final ListView listView = dialog1.listView;
                    listView.setAdapter(simpleCursorAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            cursor = (Cursor) listView.getItemAtPosition(position);
                            Piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                            pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                            piece.setText(Piece);
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
                mYear = calendar1.get(Calendar.YEAR);
                mMonth = calendar1.get(Calendar.MONTH);
                mDay = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay);
                String month = String.valueOf(mMonth);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                dateExpected.setText(day+"-"+month+"-"+mYear);
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
                mYear = calendar1.get(Calendar.YEAR);
                mMonth = calendar1.get(Calendar.MONTH);
                mDay = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay);
                String month = String.valueOf(mMonth);
                if(day.length()==1)
                    day="0"+day;
                if(month.length()==1)
                    month="0"+month;
                dateExpected.setText(day+"-"+month+"-"+mYear);
            }
        });
        findViewById(R.id.date2).setOnClickListener(new View.OnClickListener() {
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
                        dateExpected.setText(day1+"-"+month1+"-"+year);
                    }
                },mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                                            cost.setText(co.getText().toString());
                                                            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                                                            ExPerId=cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                                                            ExName=cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                                                            ExName.toUpperCase();
                                                            if(ExType.equalsIgnoreCase("Embroider"))
                                                                desc="Embroidery By : "+exname.getText().toString();
                                                            else
                                                                desc="Stitching By : "+exname.getText().toString();
                                                            desc=desc.toUpperCase();
                                                            dialog2.quitDialog(v);
                                                            dialog1.quitDialog(v);
                                                            dialog.quitDialog(v);
                                                            function(ExType,ExName,cost.getText().toString(),desc);
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
                                        Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    } else
                                        Message.message(context, "No " + radioButton1.getText().toString() + "s found");
                                } else if (radioButton1.getText().toString().equalsIgnoreCase("Other")) {
                                    final alertDialog dialog1 = new alertDialog(context, "Others-Please Specify", "Please enter the name of the person you want to give this piece to", "DONE", "CANCEL");
                                    final TextInputLayout name1 = dialog1.name1;
                                    final TextInputEditText name = dialog1.name;
                                    final TextInputLayout cost1 = dialog1.cost1;
                                    final TextInputEditText cost = dialog1.cost;
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
                                                cost1.setError("No cost entered");

                                            else {
                                                ExType = "Other".toUpperCase();
                                                ExName = name.getText().toString().toUpperCase();
                                                String Cost = cost.getText().toString();
                                                if(descri.getText().toString().length()==0)
                                                    desc = "Paid to " + ExName;
                                                else
                                                    desc=descri.getText().toString();
                                                dialog1.dismiss();
                                                dialog.dismiss();
                                                function(ExType, ExName, Cost,desc);
                                            }
                                        }
                                    });
                                    dialog1.show();
                                    Window window = dialog1.getWindow();
                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                }else{
                                    dialog.quitDialog(v);
                                    function("-","Me","0.0","-");
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
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ExName=exname.getText().toString().toUpperCase();
                    ExType=extype.getText().toString().toUpperCase();
                    price = Double.parseDouble(cost.getText().toString());
                    boolean result;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
                String date=simpleDateFormat.format(new Date());
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedId);
                    stat=radioButton.getText().toString().toUpperCase();
                    String nop="";
                    perName=cusName.getText().toString();
                    cursor=myDbAdapter.getPiece(Integer.parseInt(pieceId));
                double c=cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                c+=Double.parseDouble(cost.getText().toString());
                    result = myDbAdapter.updateStatus(piece.getText().toString(),stat,ExName,ExType,dateExpected.getText().toString(),pieceId,c,0,"DUE");
                    if(!ExName.equalsIgnoreCase("ME")&&!stat.equalsIgnoreCase("COMPLETED")) {
                        myDbAdapter.addExpense(perId, perName, pieceId, piece.getText().toString(), date, description.getText().toString(), cost.getText().toString());
                    }
                    if(ExType.equalsIgnoreCase("EMBROIDER")||ExType.equalsIgnoreCase("TAILOR")) {
                        if(ExType.equalsIgnoreCase("EMBROIDER"))
                            myDbAdapter.addPieceForPerson("2", ExName,perId, perName, piece.getText().toString(), pieceId, price, date, "Due on Delivery","IN PROCESS");
                        if(ExType.equalsIgnoreCase("TAILOR"))
                            myDbAdapter.addPieceForPerson("3", ExName,perId, perName, piece.getText().toString(), pieceId, price, date, "Due on Delivery","IN PROCESS");
                        if(flag==1) {
                            if (ExStatus1.equalsIgnoreCase("EMBROIDER"))
                                myDbAdapter.updateTaskForET("2", ExStatus2, "DONE", pieceId);
                            else if (ExStatus1.equalsIgnoreCase("TAILOR"))
                                myDbAdapter.updateTaskForET("3", ExStatus2, "DONE", pieceId);
                        }
                        cursor = myDbAdapter.getPerson(ExType, ExPerId);
                        double payment=cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                        payment+=price;
                        myDbAdapter.updatePersonPayment(perId, payment, ExType);
                        nop = cursor.getString(cursor.getColumnIndexOrThrow("NOP"));
                        int no = Integer.parseInt(nop);
                        no++;
                        nop = String.valueOf(no);
                        myDbAdapter.updateNOP(ExPerId,ExType, ExName, nop);
                    }
                    if (result) {
                        Message.message(context,"Piece successfully Updated");
                        if(status.getText().toString().equalsIgnoreCase("COMPLETED")){
                            final alertDialog dialog=new alertDialog(context);
                            dialog.setTitle("Accept Payment","Piece completed successfully. Do you want to accept the payment of this piece right now?","Yes","Later");
                            dialog.positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(context,AcceptPaymentActivity.class);
                                    intent.putExtra("piece", piece.getText().toString());
                                    intent.putExtra("pieceId", pieceId);
                                    intent.putExtra("perName", perName);
                                    intent.putExtra("perId", perId);
                                    startActivity(intent);
                                }
                            });
                            dialog.negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
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
                        else{
                            finish();
                        }
                    } else
                        Message.message(context,"Unsuccessful");
            }
        });
    }
    public void function(String ExType1,String ExName1,String Cost1,String desc) {
        extype.setVisibility(View.VISIBLE);
        exname.setVisibility(View.VISIBLE);
        cost.setVisibility(View.VISIBLE);
        extype1.setVisibility(View.VISIBLE);
        exname1.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        description1.setVisibility(View.VISIBLE);
        cost1.setVisibility(View.VISIBLE);
        extype.setText(ExType1);
        exname.setText(ExName1);
        cost.setText(Cost1);
        description.setText(desc);
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
