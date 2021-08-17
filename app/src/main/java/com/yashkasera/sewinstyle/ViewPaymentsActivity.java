/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewPaymentsActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Cursor cursor=null;
    ListView listView;
    int flag=0;
    String per="",person="",perName="",perId="",piece="",pieceId="",what="all";
    SimpleCursorAdapter simpleCursorAdapter;
    TextView total,cusName,Piece,Person,totalAmt,amtReceived,amtPaid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payments);
        pieceId = getIntent().getStringExtra("pieceId");
        piece = getIntent().getStringExtra("piece");
        perName = getIntent().getStringExtra("perName");
        person = getIntent().getStringExtra("person");
        perId = getIntent().getStringExtra("perId");
        listView = findViewById(R.id.listView);
        total = findViewById(R.id.total);
        cusName = findViewById(R.id.cusName);
        Piece = findViewById(R.id.piece);
        Person = findViewById(R.id.person);
        totalAmt = findViewById(R.id.totalAmt);
        amtPaid = findViewById(R.id.amtPaid);
        amtReceived = findViewById(R.id.amtReceived);
        if(perName!=null) {
            cusName.setText(perName);
            toolbar("Payments", perName);
        }
        else {
            cusName.setText("All");
            toolbar("Payments", "All");
        }
        if(person!=null) {
            Person.setText(person);
            toolbar("Payments", person);
        }
        else {
            Person.setText("Category");
            toolbar("Payments", "All");
        }
        if(piece!=null) {
            Piece.setText(piece);
        }
        else {
            Piece.setText("All");
        }
        function();
        findViewById(R.id.cusName1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu=new PopupMenu(context,v, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_persons,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(!item.getTitle().toString().equalsIgnoreCase("ALL")) {
                            person = item.getTitle().toString();
                            Person.setText(person);
                            perId = person(person);
                        }
                        else{
                            cusName.setText("All");
                            Piece.setText("All");
                            Person.setText("Category");
                            function();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        findViewById(R.id.piece1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieceId=piece();
            }
        });
    }
    void function(){
        String[] columns = {"DATEPAID", "NAME", "CATEGORY","PIECE","AMOUNT"};
        int[] to = new int[]{
                R.id.date,
                R.id.name,
                R.id.piece,
                R.id.desc,
                R.id.amt,
        };
        if(cusName.getText().toString().equalsIgnoreCase("All")||cusName.getText().toString().equalsIgnoreCase("")){
            cursor = myDbAdapter.getPayments("","all","");
            toolbar("Payments", "All");
            findViewById(R.id.l1).setVisibility(View.VISIBLE);
            findViewById(R.id.l2).setVisibility(View.VISIBLE);
        }
        else if(person.equalsIgnoreCase("Customer")){
            toolbar("Payments", cusName.getText().toString());
            findViewById(R.id.l1).setVisibility(View.GONE);
            findViewById(R.id.l2).setVisibility(View.GONE);
            if(Piece.getText().toString().equalsIgnoreCase("ALL")||Piece.getText().toString().equalsIgnoreCase("Not Selected")){
                cursor = myDbAdapter.getPayments(person, "perName", perId);
            }
            else{
                cursor = myDbAdapter.getPaymentPiece(person,pieceId);
            }
        }
        else{
            toolbar("Payments", cusName.getText().toString());
            findViewById(R.id.l1).setVisibility(View.GONE);
            findViewById(R.id.l2).setVisibility(View.GONE);
            cursor = myDbAdapter.getPayments(person, "perName", perId);
            cursor.moveToFirst();
            double tot=0.0;
            do {
                tot += cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
            }
            while (cursor.moveToNext());
            totalAmt.setText(tot+"");
        }
        if (cursor.getCount() == 0) {
            Message.message(context, "No Payments Found!");
            simpleCursorAdapter = new SimpleCursorAdapter(
                    this, R.layout.expense_info3,
                    null,
                    columns,
                    to,
                    0);
            listView.setAdapter(simpleCursorAdapter);
            totalAmt.setText("0.0");
        }
        else {
            total = findViewById(R.id.total);
            total.setText(cursor.getCount() + " Payments");
            simpleCursorAdapter = new SimpleCursorAdapter(
                    this, R.layout.expense_info3,
                    cursor,
                    columns,
                    to,
                    0);
            listView.setAdapter(simpleCursorAdapter);
            if (cusName.getText().toString().equalsIgnoreCase("All") || cusName.getText().toString().equalsIgnoreCase("")) {
                cursor.moveToFirst();
                double paid=0.0,received=0.0,temp;
                do {
                    temp = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                    if (cursor.getString(cursor.getColumnIndexOrThrow("CATEGORY")).equalsIgnoreCase("CUSTOMER")) {
                        received += temp;
                    } else {
                        paid += temp;
                    }
                } while (cursor.moveToNext());
                amtReceived.setText(received+"");
                amtPaid.setText(paid+"");
                totalAmt.setText((received-paid)+"");
            }else{
                cursor.moveToFirst();
                double tot=0.0;
                do {
                    tot += cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                }
                while (cursor.moveToNext());
                totalAmt.setText(tot+"");
            }
        }
    }
    String person(String person){
        Person.setText(person);
        cursor=myDbAdapter.getPersons(person);
        if(cursor.getCount()==0)
        {
            Message.message(context,"No "+person+"s Found! Displaying all payments.");
            cusName.setText("All");
            Piece.setText("All");
            Person.setText("Category");
            function();
        }
        else if(cursor.getCount()==1){
            Message.message(context,"One "+person+" found, selected!");
            perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            cusName.setText(perName);
            Piece.setText("All");
            perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            function();
        }
        else {
            String[] columns = {"NAME"};
            int[] to = {R.id.textView};
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
            final alertDialog dialog1 = new alertDialog(context, simpleCursorAdapter, "Choose "+person, "Select a "+person+" from the given list. To add a new "+person+", go back to the dashboard and add");
            final ListView listView = dialog1.listView;
            listView.setAdapter(simpleCursorAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                    cusName.setText(perName);
                    Piece.setText("All");
                    perId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    dialog1.dismiss();
                    function();
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
    String piece() {
        if(person.equalsIgnoreCase("Customer")) {
            if (cusName.getText().toString().length() == 0 || cusName.getText().toString().equalsIgnoreCase("Not Selected")) {
                cusName.setError("Add Customer first");
                Message.message(context, "Add Customer first");
            } else {
                cursor = myDbAdapter.getPiecesOf(perId);
                if (cursor.getCount() == 0) {
                    Message.message(context, "No Pieces Found!");
                } else if (cursor.getCount() == 1) {
                    Message.message(context, "One piece found, selected!");
                    piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                    pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                    Piece.setText(piece);
                    function();
                } else {
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
                            function();
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
            return pieceId;
        }
        else{
            Piece.setText("Not Valid");
        }
        return "0";
    }
    void toolbar(String title,String subtitle) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
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
                finish();
        }
        return true;
    }
}
