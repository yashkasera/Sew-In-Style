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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GivePaymentActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Cursor cursor=null;
    String person="",perName="",perId="",per="",pieceId="",piece="";
    TextView Piece,embTai,amount;
    TextInputEditText remarks,date;
    int mYear,mMonth,mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_payment);
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
        per = getIntent().getStringExtra("per");
        person = getIntent().getStringExtra("person");
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        pieceId=getIntent().getStringExtra("pieceId");
        piece = getIntent().getStringExtra("piece");
        amount = findViewById(R.id.amount);
        Piece = findViewById(R.id.piece);
        remarks = findViewById(R.id.remarks);
        date = findViewById(R.id.date);
        embTai = findViewById(R.id.embTai);
        if (!TextUtils.isEmpty(perName))
            embTai.setText(perName);
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
        findViewById(R.id.embTai1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = myDbAdapter.getPersons(person);
                if (cursor.getCount() == 0) {
                    Message.message(context, "No "+person+"s Found!");
                } else if (cursor.getCount() == 1) {
                    Message.message(context, "One "+person+" found, selected");
                    String c = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    embTai.setText(c);
                    perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Piece.setText("Not Selected");
                } else {
                    String[] columns = {"NAME"};
                    int[] to = {R.id.textView};
                    SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
                    final alertDialog dialog1 = new alertDialog(context, simpleCursorAdapter, "Choose "+person, "Select the "+person+" for whom you want to add this expense");
                    final ListView listView = dialog1.listView;
                    listView.setAdapter(simpleCursorAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            cursor = (Cursor) listView.getItemAtPosition(position);
                            String c = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                            perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                            embTai.setText(c);
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
                cursor = myDbAdapter.getPiecesForETUnpaid(perName,per);
                if (cursor.getCount() == 0) {
                    Message.message(context, "No unpaid Pieces Found!");
                } else if (cursor.getCount() == 1) {
                    Message.message(context, "One piece found, selected");
                    piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                    pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Piece.setText(piece);
                    cursor = myDbAdapter.getPersonTableById(perName, per,pieceId);
                    amount.setText(cursor.getString(cursor.getColumnIndexOrThrow("COST")));
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
                            Piece.setText(piece);
                            dialog1.dismiss();
                            cursor = myDbAdapter.getPersonTableById(perName, per,pieceId);
                            if(cursor.getCount()==0){
                                finish();
                                Message.message(context,"Piece could not be found");
                            }
                            else{
                                amount.setText(cursor.getString(cursor.getColumnIndexOrThrow("COST")));
                            }
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
                if(remarks.getText().toString().length()==0)
                    remarks.setText("For Piece "+piece);
                if(embTai.getText().toString().equalsIgnoreCase("Not Selected")) {
                    Message.message(context, "Select a "+person);
                    Piece.setError("Select a "+person);
                }
                else if(Piece.getText().toString().equalsIgnoreCase("Not Selected")) {
                    Message.message(context, "Select a piece");
                    Piece.setError("Select a piece");
                }
                else{
                    final alertDialog dialog = new alertDialog(context);
                    dialog.setTitle("Confirm Payment","Are you sure you want to give Rs."+amount.getText().toString()+" as payment?","Yes","No");
                    dialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean a = myDbAdapter.addPayment(person,perId, perName, Double.parseDouble(amount.getText().toString()), date.getText().toString(), remarks.getText().toString(),Piece.getText().toString(),pieceId);
                            boolean b = myDbAdapter.updatePayStatusPerTable(pieceId, Double.parseDouble(amount.getText().toString()), perName, per);
                            cursor = myDbAdapter.getPerson(person, perId);
                            double payment=cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                            payment-=Double.parseDouble(amount.getText().toString());
                            //TODO update piece cost and expense if amount is changed
                            //todo
                            boolean c = myDbAdapter.updatePersonPayment(perId, payment, person);
                            if(a&&b&&c){
                                Message.message(context,"Successful!");
                                finish();
                            }
                            else
                                Message.message(context,"Failed!");
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
