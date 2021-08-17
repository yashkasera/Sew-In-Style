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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewExpensesActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context = this;
    String person,perName,Piece,pieceId,perId,function;
    TextView cusName,piece,total,totalAmt;
    ListView listView;
    Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        perName = getIntent().getStringExtra("perName");
        person = getIntent().getStringExtra("person");
        perId = getIntent().getStringExtra("perId");
        Piece = getIntent().getStringExtra("piece");
        pieceId = getIntent().getStringExtra("pieceId");
        function(getIntent().getStringExtra("function"));
    }
    void function(final String what){
        Message.message(context,"Rotate your screen for better viewing");
        if(what.equalsIgnoreCase("all")){
            setContentView(R.layout.activity_view_expenses_all);
            toolbar("Expenses","All");
            perName="";
            Piece="";
            listView = findViewById(R.id.listView);
            cursor=myDbAdapter.getPieces();
            String[] columns = {"DATEADDED", "NAME", "PIECE", "DESCRIPTION", "AMOUNT"};
            cursor = myDbAdapter.getAllExpense();
            if(cursor.getCount()==0)
                Message.message(context,"No Expenses Found!");
            else {
                int[] to = new int[]{R.id.date, R.id.name, R.id.piece, R.id.desc, R.id.amt};
                simpleCursorAdapter = new SimpleCursorAdapter(
                        this, R.layout.expense_info3,
                        cursor,
                        columns,
                        to,
                        0);
                total = findViewById(R.id.total);
                totalAmt = findViewById(R.id.totalAmt);
                total.setText(cursor.getCount() + " Expenses");
                double amt = 0.0;
                do {
                    amt += Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("AMOUNT")));
                }
                while (cursor.moveToNext());
                totalAmt.setText(amt + "");
                function2(simpleCursorAdapter);
            }
        }
        else if(what.equalsIgnoreCase("piece")){
            setContentView(R.layout.activity_view_expenses);
            toolbar(perName,Piece);
            cusName = findViewById(R.id.cusName);
            piece = findViewById(R.id.piece);
            cusName.setText(perName);
            piece.setText(Piece);
            listView = findViewById(R.id.listView);
            if(cusName.getText().toString().length()==0||cusName.getText().toString().equalsIgnoreCase("Not Selected"))
            {
                cusName.setText("Not Selected");
                cusName.setError("Add Customer first");
                Message.message(context, "Add Customer first");
            }
            else if(piece.getText().toString().length()==0||piece.getText().toString().equalsIgnoreCase("Not Selected")) {
                piece.setText("Not Selected");
                piece.setError("Add Piece");
                Message.message(context, "Add Piece");
            }
            findViewById(R.id.cusName1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perId=customer(what);
                    piece.setText("Not Selected");
                }
            });
            findViewById(R.id.piece1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pieceId=piece(what);
                }
            });
            cursor = myDbAdapter.getPieceExpense(pieceId);
            if (cursor.getCount() == 0) {
                Message.message(context,"No Expenses found");
            }
            else{
                String[] columns = {"DATEADDED", "DESCRIPTION", "AMOUNT"};
                int[] to = new int[]{R.id.date, R.id.desc, R.id.amt};
                simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.expense_info, cursor, columns, to, 0);
                    total = findViewById(R.id.total);
                    totalAmt = findViewById(R.id.totalAmt);
                    total.setText(cursor.getCount()+" Expenses");
                double amt=0.0;
                do {
                    amt += Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("AMOUNT")));
                }
                while (cursor.moveToNext());
                totalAmt.setText(amt+"");
                    function2(simpleCursorAdapter);
            }
        }
        else if(what.equalsIgnoreCase("customer")){
            setContentView(R.layout.activity_view_expenses_cus);
            toolbar(perName,"Customer");
            cusName = findViewById(R.id.cusName);
            cusName.setText(perName);
            Piece="";
            findViewById(R.id.cusName1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    perId=customer(what);
                }
            });
            if(cusName.getText().toString().length()==0||cusName.getText().toString().equalsIgnoreCase("Not Selected"))
            {
                cusName.setError("Add Customer first");
                Message.message(context, "Add Customer first");
            }
            else {
                listView = findViewById(R.id.listView);
                if (perName.length() == 0)
                    cusName.setText("Not Selected");
                else
                    cusName.setText(perName);
                cursor = myDbAdapter.getCustomerExpense(perId);
                if (cursor.getCount() == 0) {
                    Message.message(context, "No Expenses found");
                } else {
                    String[] columns = {"DATEADDED", "PIECE", "DESCRIPTION", "AMOUNT"};
                    int[] to = new int[]{R.id.date, R.id.piece, R.id.desc, R.id.amt};
                    simpleCursorAdapter = new SimpleCursorAdapter(
                            this, R.layout.expense_info2,
                            cursor,
                            columns,
                            to,
                            0);
                    total = findViewById(R.id.total);
                    totalAmt = findViewById(R.id.totalAmt);
                    total.setText(cursor.getCount()+" Expenses");
                    double amt=0.0;
                    do {
                        amt += Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("AMOUNT")));
                    }
                    while (cursor.moveToNext());
                    totalAmt.setText(amt+"");
                    function2(simpleCursorAdapter);
                }

            }
        }
        else{
            Message.message(context,"ERROR#404! PAGE NOT FOUND");
        }
    }
    void function2(SimpleCursorAdapter simpleCursorAdapter){
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                perId = cursor.getString(cursor.getColumnIndexOrThrow("PERID"));
                perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                pieceId = cursor.getString(cursor.getColumnIndexOrThrow("PIECEID"));
                Piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                function("piece");
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                pieceId = cursor.getString(cursor.getColumnIndexOrThrow("PIECEID"));
                final String expenseId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                final PopupMenu popupMenu=new PopupMenu(context,view, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_expenses,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Message.message(context,item.getTitle()+"");
                        if(item.getTitle().equals("Edit")){
                            edit(expenseId,pieceId);
                        }
                        else if(item.getTitle().equals("Delete")){
                            delete(expenseId);
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }
    void toolbar(String title,String subtitle){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddExpenseActivity.class);
                intent.putExtra("person", person);
                intent.putExtra("perName", perName);
                intent.putExtra("perId", perId);
                intent.putExtra("piece", Piece);
                intent.putExtra("pieceId", pieceId);
                finish();
                startActivity(intent);
            }
        });
    }
    String customer(final String what){
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
            function(what);
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
                    function(what);
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
    String piece(final String what){
        if(cusName.getText().toString().length()==0||cusName.getText().toString().equalsIgnoreCase("Not Selected"))
        {
            cusName.setError("Add Customer first");
            Message.message(context, "Add Customer first");
        }
        else {
            cursor = myDbAdapter.getPiecesOf(perId);
            if(cursor.getCount()==0)
            {
                Message.message(context,"No Pieces Found!");
            }
            else if(cursor.getCount()==1){
                Message.message(context,"One piece found, selected");
                Piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                piece.setText(Piece);
                function(what);
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
                        function(what);
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
        return pieceId;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_expenses);
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
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_home:
                finish();
                intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.view_customer:
                function = "customer";
                function(function);
                break;
            case R.id.view_piece:
                function = "piece";
                function(function);
                break;
            case R.id.view_all:
                function = "all";
                function(function);
            case R.id.add:
                intent = new Intent(context, AddExpenseActivity.class);
                intent.putExtra("person", person);
                intent.putExtra("perName", perName);
                intent.putExtra("perId", perId);
                intent.putExtra("piece", Piece);
                intent.putExtra("pieceId", pieceId);
                finish();
                startActivity(intent);
                break;
        }
        return true;
    }
    double old1,new1;
    String amt,date,desc;
    void edit(final String id, final String pieceId){
        cursor=myDbAdapter.getExpense(id);
        old1=cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
         amt = cursor.getString(cursor.getColumnIndexOrThrow("AMOUNT"));
         date = cursor.getString(cursor.getColumnIndexOrThrow("DATEADDED"));
         desc = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
        final alertDialog dialog = new alertDialog(context, amt, desc, date);
        dialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog1 = new alertDialog(context);
                dialog1.setTitle("Confirm Update", "All previous data will be lost. Are you sure you still want to continue? ", "Update", "Cancel");
                Button yes = dialog1.positive;
                Button no = dialog1.negative;
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amt = dialog.cost.getText().toString();
                        if (amt.length() == 0) {
                            dialog.cost1.setError("Amount cannot be zero");
                        } else {
                            date = dialog.date.getText().toString();
                            desc = dialog.description.getText().toString();
                            new1 = Double.parseDouble(amt);
                            cursor = myDbAdapter.getPiece(Integer.parseInt(pieceId));
                            double cost = cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                            cost -= old1;
                            cost += new1;
                            myDbAdapter.updateCost(Integer.parseInt(pieceId), cost);
                            if (myDbAdapter.updateExpense(id, date, desc, amt)) {
                                Message.message(context, "Expense Updated successfully");
                                dialog.dismiss();
                                dialog1.dismiss();
                                finish();
                                startActivity(getIntent());
                            } else {
                                Message.message(context, "Expense failed to update");
                            }
                        }
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                Window window = dialog1.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    void delete(final String id){
        cursor = myDbAdapter.getExpense(id);
        pieceId = cursor.getString(cursor.getColumnIndexOrThrow("PIECEID"));
        final alertDialog dialog = new alertDialog(context);
        Button yes = dialog.findViewById(R.id.positive);
        Button no= dialog.findViewById(R.id.negative);
        ImageView dismiss= dialog.findViewById(R.id.dismiss);
        dialog.setTitle("Confirm Delete", "This action cannot be undone. Are you sure you still want to continue?", "Yes", "No");
        yes.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                cursor = myDbAdapter.getPiece(Integer.parseInt(pieceId));
                double amt = cursor.getDouble(cursor.getColumnIndexOrThrow("COST"));
                cursor = myDbAdapter.getExpense(id);
                amt-=cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
                myDbAdapter.updateCost(Integer.parseInt(pieceId), amt);
                boolean a = myDbAdapter.deleteExpense(id);
                if (a) {
                    Message.message(context, "Expense deleted successfully");
                    finish();
                    startActivity(getIntent());
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
