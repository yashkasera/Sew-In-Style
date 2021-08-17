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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context=this;
    Cursor cursor=null;
    String perName,person,perId,piece,pieceId;
    TextInputLayout cost1,description1,date1;
    TextInputEditText cost,description,date;
    TextView CusName,Piece;
    int mYear, mMonth, mDay,mYear1, mMonth1, mDay1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        perName = getIntent().getStringExtra("perName");
        person = getIntent().getStringExtra("person");
        perId = getIntent().getStringExtra("perId");
        piece = getIntent().getStringExtra("piece");
        pieceId = getIntent().getStringExtra("pieceId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(perName);
        getSupportActionBar().setSubtitle(person);
        CusName=findViewById(R.id.CusName);
        Piece=findViewById(R.id.piece);
        CusName.setText(perName);
        Piece.setText(piece);
        cost = findViewById(R.id.cost);
        cost1 = findViewById(R.id.cost1);
        description = findViewById(R.id.description);
        description1 = findViewById(R.id.description1);
        date = findViewById(R.id.date);
        date1 = findViewById(R.id.date1);
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(sdf.format(new Date()));
        datePickerFunc();
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
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
                date.setText(day+"-"+month+"-"+mYear1);
                datePickerFunc();
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
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
                date.setText(day+"-"+month+"-"+mYear1);
                datePickerFunc();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Piece.getText().toString().equalsIgnoreCase("Not Selected")||TextUtils.isEmpty(Piece.getText().toString())) {
                        Piece.setError("Piece cannot be empty");
                        Message.message(context,"Add a piece!");
                    return;
                }
                else if(TextUtils.isEmpty(cost.getText().toString())){
                    cost1.setError("Amount cannot be empty");
                    return;
                }
                else if(CusName.getText().toString().equalsIgnoreCase("Not Selected")||TextUtils.isEmpty(CusName.getText().toString())) {
                    CusName.setError("Customer name cannot be empty");
                    Piece.setError("Add a customer!");
                    return;
                }
                else {
                    if (TextUtils.isEmpty(description.getText().toString().trim())) {
                        final alertDialog dialog = new alertDialog(context);
                        dialog.setTitle("Continue without description","The description of your expense looks empty. Are you sure you want to proceed without it?","Yes","No");
                        dialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cursor=myDbAdapter.getPiece(Integer.parseInt(pieceId));
                                double c=cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                                c+=Double.parseDouble(cost.getText().toString());
                                myDbAdapter.updateCost(Integer.parseInt(pieceId), c);
                                cursor = myDbAdapter.getPerson(person, perId);
                                double payment=cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                                payment += Double.parseDouble(cost.getText().toString());
                                myDbAdapter.updatePersonPayment(perId, payment, person);
                                cursor.close();
                                if (myDbAdapter.addExpense(perId,CusName.getText().toString(),pieceId,piece,date.getText().toString(),"-",cost.getText().toString())){
                                    Message.message(context, "Expense Added Successfully");
                                    dialog.dismiss();
                                    finish();
                                } else
                                    Message.message(context, "Expense cannot be added");
                            }
                        });
                        dialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.quitDialog(v);
                            }
                        });
                        dialog.show();
                        Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    } else {
                        Cursor cursor=myDbAdapter.getPiece(Integer.parseInt(pieceId));
                        double c=cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                        c+=Double.parseDouble(cost.getText().toString());
                        myDbAdapter.updateCost(Integer.parseInt(pieceId), c);
                        cursor = myDbAdapter.getPerson(person, perId);
                        double payment=cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                        payment += Double.parseDouble(cost.getText().toString());
                        myDbAdapter.updatePersonPayment(perId, payment, person);
                        if (myDbAdapter.addExpense(perId,CusName.getText().toString(),pieceId,Piece.getText().toString(),date.getText().toString(),description.getText().toString(),cost.getText().toString())){
                            Message.message(context, "Expense Added Successfully");
                            finish();
                        } else
                            Message.message(context, "Expense cannot be added");
                    }
                }
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    CusName.setText(c);
                    perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Piece.setText("Not Selected");
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
                            CusName.setText(c);
                            Piece.setText("Not Selected");
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
                    piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                    pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Piece.setText(piece);
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
                            piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                            pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                            Piece.setText(piece);
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
    }
    private void datePickerFunc() {
        final DatePicker datePicker = findViewById(R.id.datePicker);
        String t = date.getText().toString();
        int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
        int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
        int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
        Calendar calendar=Calendar.getInstance();
        calendar.set(y,m,d);
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
