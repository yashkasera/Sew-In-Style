/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewPieceInfoActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    Cursor cursor;
    String pieceId = "", piece = "", perName = "", person = "", perId = "", ExName, ExType, ExId;
    FloatingActionButton fab, fab1, fab2, fab3, fab4, fab5;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView Piece, cusName, cost, status, dateAdded, dateExpected, description, with, price, dateExpected1, price1, pay_stat1, pay_stat;
    TextView hint, hint2, hint3, hint4, hint5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_piece_info);
        pieceId = getIntent().getStringExtra("pieceId");
        piece = getIntent().getStringExtra("piece");
        perName = getIntent().getStringExtra("perName");
        person = getIntent().getStringExtra("person");
        perId = getIntent().getStringExtra("perId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Piece");
        getSupportActionBar().setSubtitle("Details");
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);
        Piece = findViewById(R.id.piece);
        cusName = findViewById(R.id.cusName);
        cost = findViewById(R.id.cost);
        status = findViewById(R.id.status);
        dateAdded = findViewById(R.id.dateAdded);
        dateExpected = findViewById(R.id.dateExpected);
        description = findViewById(R.id.description);
        with = findViewById(R.id.with);
        price = findViewById(R.id.price);
        dateExpected1 = findViewById(R.id.dateExpected1);
        price1 = findViewById(R.id.price1);
        pay_stat = findViewById(R.id.pay_stat);
        pay_stat1 = findViewById(R.id.pay_stat1);
        cursor = myDbAdapter.getPiece(Integer.parseInt(pieceId));
        Piece.setText(cursor.getString(cursor.getColumnIndexOrThrow("PIECE")));
        cusName.setText(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
        cost.setText(cursor.getString(cursor.getColumnIndexOrThrow("COST")));
        status.setText(cursor.getString(cursor.getColumnIndexOrThrow("STATUS")));
        dateAdded.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED")));
        dateExpected.setText(cursor.getString(cursor.getColumnIndexOrThrow("DATEEXPECTED")));
        description.setText(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
        ExType = cursor.getString(cursor.getColumnIndexOrThrow("EXTYPE"));
        ExName = cursor.getString(cursor.getColumnIndexOrThrow("EXNAME"));
        pay_stat.setText(cursor.getString(cursor.getColumnIndexOrThrow("PAYSTATUS")));
        with.setText(ExType + " - " + ExName);
        if (status.getText().toString().equalsIgnoreCase("COMPLETED")) {
            price.setText(cursor.getString(cursor.getColumnIndexOrThrow("PRICE")));
            pay_stat.setText(cursor.getString(cursor.getColumnIndexOrThrow("PAYSTATUS")));
            dateExpected1.setText("Date Completed");
            price1.setVisibility(View.VISIBLE);
        } else {
            price.setVisibility(View.GONE);
            price1.setVisibility(View.GONE);
            pay_stat.setVisibility(View.GONE);
            pay_stat1.setVisibility(View.GONE);
        }
        fab();
    }

    boolean isOpen = false;

    @SuppressLint("RestrictedApi")
    public void fab() {
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab5 = findViewById(R.id.fab5);
        hint = findViewById(R.id.hint);
        hint2 = findViewById(R.id.hint2);
        hint3 = findViewById(R.id.hint3);
        hint4 = findViewById(R.id.hint4);
        hint5 = findViewById(R.id.hint5);
        fab.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        fab4.setVisibility(View.INVISIBLE);
        fab5.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.INVISIBLE);
        hint2.setVisibility(View.INVISIBLE);
        hint3.setVisibility(View.INVISIBLE);
        hint4.setVisibility(View.INVISIBLE);
        hint5.setVisibility(View.INVISIBLE);
        if (cursor != null) {
            cursor.close();
        }
        fab1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fab.setVisibility(View.VISIBLE);
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);
                    fab4.setVisibility(View.VISIBLE);
                    fab5.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.VISIBLE);
                    hint2.setVisibility(View.VISIBLE);
                    hint3.setVisibility(View.VISIBLE);
                    hint4.setVisibility(View.VISIBLE);
                    hint5.setVisibility(View.VISIBLE);
                    fab2.startAnimation(fab_close);
                    fab.startAnimation(fab_close);
                    fab4.startAnimation(fab_close);
                    fab3.startAnimation(fab_close);
                    fab5.startAnimation(fab_close);
                    hint2.startAnimation(fab_close);
                    hint.startAnimation(fab_close);
                    hint4.startAnimation(fab_close);
                    hint3.startAnimation(fab_close);
                    hint5.startAnimation(fab_close);
                    fab1.setVisibility(View.INVISIBLE);
                    fab1.startAnimation(fab_anticlock);
                    isOpen = false;
                } else {
                    hide_all();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_all();
                Intent intent = new Intent(context, EditPieceActivity.class);
                intent.putExtra("person", person);
                intent.putExtra("perName",perName);
                intent.putExtra("pieceId", pieceId);
                intent.putExtra("piece", piece);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {                //DELETE
            @Override
            public void onClick(View v) {               //DELETE
                hide_all();
                delete();
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       //View Expense
                hide_all();
                Message.message(context, "View Expenses");
                Intent intent = new Intent(context, ViewExpensesActivity.class);
                intent.putExtra("person", "Customer");
                intent.putExtra("perName", cusName.getText().toString());
                intent.putExtra("piece", Piece.getText().toString());
                intent.putExtra("pieceId", pieceId);
                intent.putExtra("per", "1");
                intent.putExtra("function", "piece");
                startActivity(intent);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {               //Update Status
                hide_all();
                if (!status.getText().toString().equalsIgnoreCase("Completed")) {
                    Message.message(context, "Update Status");
                    Intent intent = new Intent(context, UpdateStatusActivity.class);
                    intent.putExtra("person", "Customer");
                    intent.putExtra("perName", cusName.getText().toString());
                    intent.putExtra("piece", Piece.getText().toString());
                    intent.putExtra("pieceId", pieceId);
                    intent.putExtra("perId", perId);
                    startActivity(intent);
                } else {
                    Message.message(context, "Completed pieces cannot be updated!");
                }
            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //Add Expense
                hide_all();
                if (!status.getText().toString().equalsIgnoreCase("Completed")) {
                    Message.message(context, "Add Expense");
                    Intent intent = new Intent(context, AddExpenseActivity.class);
                    intent.putExtra("person", "Customer");
                    intent.putExtra("perName", cusName.getText().toString());
                    intent.putExtra("piece", Piece.getText().toString());
                    intent.putExtra("pieceId", pieceId);
                    intent.putExtra("perId", perId);
                    startActivity(intent);
                } else {
                    Message.message(context, "Expenses cannot be added to completed pieces!");
                }
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void hide_all() {
        fab1.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        fab4.setVisibility(View.INVISIBLE);
        fab5.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.INVISIBLE);
        hint2.setVisibility(View.INVISIBLE);
        hint3.setVisibility(View.INVISIBLE);
        hint4.setVisibility(View.INVISIBLE);
        hint5.setVisibility(View.INVISIBLE);
        fab2.startAnimation(fab_open);
        fab.startAnimation(fab_open);
        fab4.startAnimation(fab_open);
        fab3.startAnimation(fab_open);
        fab5.startAnimation(fab_open);
        hint2.startAnimation(fab_open);
        hint.startAnimation(fab_open);
        hint4.startAnimation(fab_open);
        hint3.startAnimation(fab_open);
        hint5.startAnimation(fab_open);
        fab1.startAnimation(fab_clock);
        isOpen = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    void delete() {
        final alertDialog dialog = new alertDialog(context);
        dialog.setTitle("Confirm Delete", "This action cannot be undone. Are you sure you still want to continue?", "Yes", "No");
        dialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDbAdapter.deletePiece(pieceId)) {
                    cursor = myDbAdapter.getPerson(person, perId);
                    int nop = cursor.getInt(cursor.getColumnIndexOrThrow("NOP"));
                    nop--;
                    myDbAdapter.updateNOP(perId, person, perName, String.valueOf(nop));
                    if (!ExType.equalsIgnoreCase("ME") && !ExType.equalsIgnoreCase("-")) {
                        cursor = myDbAdapter.getPersonByName(ExName, ExType);
                        ExId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        cursor = myDbAdapter.getPerson(ExType, ExId);
                        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                        cursor = myDbAdapter.getExpenseForPiece(pieceId);
                        if (cursor.getCount() > 0) {
                            do {
                                amount -= cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                            }
                            while (cursor.moveToNext());
                            myDbAdapter.updatePayment(ExType, ExId, amount);
                        }
                        cursor = myDbAdapter.getPerson(ExType, ExId);
                        nop = cursor.getInt(cursor.getColumnIndexOrThrow("NOP"));
                        nop--;
                        myDbAdapter.updateNOP("", ExType, ExName, String.valueOf(nop));
                        myDbAdapter.deletePieceFromPersonTable(pieceId, ExName, ExType);
                    }
                    myDbAdapter.deleteExpenseForPiece(pieceId);
                    Message.message(context, "Piece successfully deleted.");
                    dialog.dismiss();
                    finish();
                } else
                    Message.message(context, "Piece could not be deleted!");
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
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myDbAdapter != null)
            myDbAdapter.close();
        if (cursor != null)
            cursor.close();
        hide_all();
    }

    @Override
    public void onPause() {
        super.onPause();
        hide_all();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        hide_all();
        startActivity(getIntent());
    }
}
