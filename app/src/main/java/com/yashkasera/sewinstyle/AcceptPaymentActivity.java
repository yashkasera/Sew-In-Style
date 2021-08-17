/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AcceptPaymentActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    Cursor cursor=null;
    String per="",person="",perName="",perId="",pieceId="",piece="";
    TextView profit,profitper,cusName,Piece,cp;
    TextInputLayout sp1;
    TextInputEditText sp,remarks,date;
    int mYear,mMonth,mDay;
    double C,S,P,PP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_payment);
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        per = getIntent().getStringExtra("per");
        person = getIntent().getStringExtra("person");
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
        sp1 = findViewById(R.id.sp1);
        sp = findViewById(R.id.sp);
        cp = findViewById(R.id.cp);
        Piece = findViewById(R.id.piece);
        cusName = findViewById(R.id.cusName);
        profit = findViewById(R.id.profit);
        profitper = findViewById(R.id.profitper);
        remarks = findViewById(R.id.remarks);
        date = findViewById(R.id.date);
        person = getIntent().getStringExtra("person");
        perName = getIntent().getStringExtra("perName");
        if (!TextUtils.isEmpty(perName))
            cusName.setText(perName);
        if (!TextUtils.isEmpty(piece))
            Piece.setText(piece);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(simpleDateFormat.format(new Date()));
        Calendar calendar1=Calendar.getInstance();
        mYear = calendar1.get(Calendar.YEAR);
        mMonth = calendar1.get(Calendar.MONTH);
        mDay = calendar1.get(Calendar.DAY_OF_MONTH);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog = new alertDialog(context);
                Button yes = dialog.findViewById(R.id.positive);
                Button no = dialog.findViewById(R.id.negative);
                ImageView dismiss = dialog.findViewById(R.id.dismiss);
                dialog.setTitle("Confirm Cancel", "Are you sure you want to cancel?", "Yes", "No");
                yes.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
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
                cursor = myDbAdapter.getPersons("Customer");
                if (cursor.getCount() == 0) {
                    Message.message(context, "No Customers Found!");
                } else if (cursor.getCount() == 1) {
                    Message.message(context, "One Customer found, selected");
                    String c = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    cusName.setText(c);
                    perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Piece.setText("Not Selected");
                } else {
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
                cursor = myDbAdapter.getPiecesOfUnpaid(perId);
                if (cursor.getCount() == 0) {
                    Message.message(context, "No Pieces Found!");
                } else if (cursor.getCount() == 1) {
                    Message.message(context, "One piece found, selected");
                    piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                    pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    double cp1 = cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                    cp.setText(cp1+"");
                    Piece.setText(piece);
                    function(pieceId);
                } else {
                    String[] columns = {"PIECE"};
                    int[] to = {R.id.textView};
                    SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
                    final alertDialog dialog1 = new alertDialog(context, simpleCursorAdapter, "Choose Piece", "Select the piece for which you want to accept payment");
                    final ListView listView = dialog1.listView;
                    listView.setAdapter(simpleCursorAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            cursor = (Cursor) listView.getItemAtPosition(position);
                            piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                            pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                            double cp1 = cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                            cp.setText(cp1+"");
                            Piece.setText(piece);
                            dialog1.dismiss();
                            function(pieceId);
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
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y = Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(y, m, d);
                calendar1.add(Calendar.DATE, 1);
                mYear = calendar1.get(Calendar.YEAR);
                mMonth = calendar1.get(Calendar.MONTH);
                mDay = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay);
                String month = String.valueOf(mMonth);
                if (day.length() == 1)
                    day = "0" + day;
                if (month.length() == 1)
                    month = "0" + month;
                date.setText(day + "-" + month + "-" + mYear);
            }
        });
        findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y = Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(y, m, d);
                calendar1.add(Calendar.DATE, -1);
                mYear = calendar1.get(Calendar.YEAR);
                mMonth = calendar1.get(Calendar.MONTH);
                mDay = calendar1.get(Calendar.DAY_OF_MONTH);
                String day = String.valueOf(mDay);
                String month = String.valueOf(mMonth);
                if (day.length() == 1)
                    day = "0" + day;
                if (month.length() == 1)
                    month = "0" + month;
                date.setText(day + "-" + month + "-" + mYear);
            }
        });
        findViewById(R.id.date2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = date.getText().toString();
                int d = Integer.parseInt(t.substring(0, t.indexOf("-")));
                int m = Integer.parseInt(t.substring((t.indexOf("-") + 1), t.lastIndexOf("-")));
                int y=Integer.parseInt(t.substring((t.lastIndexOf("-") + 1)));
                Calendar calendar1=Calendar.getInstance();
                calendar1.set(y,m,d);
                mYear = calendar1.get(Calendar.YEAR);
                mMonth = calendar1.get(Calendar.MONTH);
                mDay = calendar1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String day1 = String.valueOf(dayOfMonth);
                        String month1 = String.valueOf(monthOfYear+1);
                        if (day1.length() == 1)
                            day1 = "0" + day1;
                        if (month1.length() == 1)
                            month1 = "0" + month1;
                        date.setText(day1 + "-" + month1 + "-" + year);
                    }
                }, mYear, mMonth-1, mDay);
                datePickerDialog.show();
            }
        });
        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Piece.getText().toString().equalsIgnoreCase("Not Selected")) {
                    Message.message(context, "Select a piece");
                    Piece.setError("Select a piece");
                }
                else if(sp.getText().toString().length()==0||sp.getText().toString().equalsIgnoreCase("0.0")||sp.getText().toString().equalsIgnoreCase("0")){
                    sp1.setError("Selling Price cannot be Empty");
                }
                else{
                    final alertDialog dialog = new alertDialog(context);
                    dialog.setTitle("Confirm Payment","Are you sure you want to accept this payment?","Yes","No");
                    dialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean a = myDbAdapter.addProfit(perName,perId, pieceId, Piece.getText().toString(), C, S, P, date.getText().toString());
                            boolean b = myDbAdapter.updateStatus(Piece.getText().toString(),"Completed",perName,person,date.getText().toString(),pieceId,C,S,"PAID");
                            String desc=remarks.getText().toString();
                            if(TextUtils.isEmpty(desc))
                                desc="No description";
                            boolean c = myDbAdapter.addPayment("CUSTOMER",perId, perName, S, date.getText().toString(), desc,Piece.getText().toString(),pieceId);
                            if (a&&b&&c) {
                                Message.message(context,"Payment accepted successfully");
                                dialog.dismiss();
                                finish();
                            }
                            else
                                Message.message(context,"Payment could not be accepted!");
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
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                }
            }
        });
    }
    void function(String pieceId){
        cursor = myDbAdapter.getPiece(Integer.parseInt(pieceId));
        if(cursor.getCount()>0) {
            cp.setText(cursor.getString(cursor.getColumnIndexOrThrow("COST")));
            if (cp.getText().toString().length() != 0)
                C = Double.parseDouble(cp.getText().toString());
            else
                C = 0.0;
            profitper.setText("0.0%");
            profit.setText("0.0");
            sp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(Piece.getText().toString().equalsIgnoreCase("Not Selected")) {
                        Message.message(context, "Select a piece");
                        Piece.setError("Select a piece");
                    }
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(sp.getText().toString())) {
                        if (sp.getText().toString().equalsIgnoreCase("0")) {
                            sp.setText("0.0");
                            sp.selectAll();
                        } else {
                            S = Double.parseDouble(sp.getText().toString());
                            P = S - C;
                            PP = (P / C) * 100;
                            PP = Math.rint(PP);
                            profit.setText(String.valueOf(P));
                            profitper.setText(PP + "%");
                        }
                    } else
                        sp.setText("0");
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(sp.getText().toString())) {
                        if (sp.getText().toString().equalsIgnoreCase("0")) {
                            sp.setText("0.0");
                            sp.selectAll();
                        } else {
                            S = Double.parseDouble(sp.getText().toString());
                            P = S - C;
                            PP = (P / C) * 100;
                            PP = Math.rint(PP);
                            profit.setText(String.valueOf(P));
                            profitper.setText(PP + "%");
                        }
                    } else
                        sp.setText("0");
                }
            });
        }
        else if(Piece.getText().toString().equalsIgnoreCase("Not Selected")) {
            Message.message(context, "Select a piece");
            Piece.setError("Select a piece");
        }
        else
            Message.message(context,"ERROR#404, PAGE NOT FOUND");
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
