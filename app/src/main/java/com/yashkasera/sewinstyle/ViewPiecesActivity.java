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

public class ViewPiecesActivity extends AppCompatActivity {
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Cursor cursor=null;
    ListView listView;
    String per="",person="",perName="",perId="",piece="",pieceId="",function,orderBy;
    SimpleCursorAdapter simpleCursorAdapter;
    TextView total,cusName,Person;
    String[] columns = {"NAME", "STATUS", "PIECE", "DESCRIPTION", "COST"};
    int[] to = new int[]{R.id.name, R.id.status, R.id.piece, R.id.desc, R.id.cost};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pieces);
        person = getIntent().getStringExtra("person");
        per = getIntent().getStringExtra("per");
        perName = getIntent().getStringExtra("perName");
        perId = getIntent().getStringExtra("perId");
        function = getIntent().getStringExtra("function");
        Person = findViewById(R.id.person);
        listView = findViewById(R.id.listView);
        total = findViewById(R.id.total);
        cusName = findViewById(R.id.cusName);
        orderBy = "STATUS DESC";
        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.list_piece_all, cursor, columns, to, 0);
        if(perName!=null) {
            cusName.setText(perName);
            toolbar("Pieces", perName);
        }
        else {
            cusName.setText("All");
            toolbar("Pieces", "All");
        }
        function();
        findViewById(R.id.cusName1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, v, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_persons, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (!item.getTitle().toString().equalsIgnoreCase("ALL")) {
                            person = item.getTitle().toString();
                            Person.setText(person);
                            perId = person(person);
                        } else {
                            cusName.setText("All");
                            Person.setText("Category");
                            function();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }
    void function() {
        if (cusName.getText().toString().equalsIgnoreCase("All") || cusName.getText().toString().equalsIgnoreCase("")) {
            cursor = myDbAdapter.getPiecesSorted();
            toolbar("Pieces", "All");
            Message.message(context,"Viewing All Pieces");
            cur();
        } else if (person.equalsIgnoreCase("Customer")) {
            toolbar("Pieces", person);
            cursor = myDbAdapter.getPiecesOf(perId);
            Message.message(context,"Viewing pieces of "+perName);
            cur();
        } else {
            toolbar("Pieces", cusName.getText().toString());
            if (person.equalsIgnoreCase("Embroider"))
                cursor = myDbAdapter.getPiecesForET(cusName.getText().toString(), "2");
            if (person.equalsIgnoreCase("Tailor"))
                cursor = myDbAdapter.getPiecesForET(cusName.getText().toString(), "3");
            Message.message(context, "Viewing all pieces by " + person + " : " + cusName.getText().toString());
            cur();
        }
    }
    void cur(){
        simpleCursorAdapter.swapCursor(cursor);
        if (cursor.getCount() == 0) {
            Message.message(context, "No pieces Found!");
            simpleCursorAdapter.swapCursor(null);
            listView.setAdapter(simpleCursorAdapter);
        }
        else {
            listView.setAdapter(simpleCursorAdapter);
        }
        total.setText(cursor.getCount() + " Pieces");
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                perId = cursor.getString(cursor.getColumnIndexOrThrow("PERID"));
                pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));
                Intent intent = new Intent(context, ViewPieceInfoActivity.class);
                intent.putExtra("person", person);
                intent.putExtra("perName",perName);
                intent.putExtra("pieceId", pieceId);
                intent.putExtra("piece", piece);
                intent.putExtra("perId", perId);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                perId = cursor.getString(cursor.getColumnIndexOrThrow("PERID"));
                pieceId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                piece = cursor.getString(cursor.getColumnIndexOrThrow("PIECE"));

                final PopupMenu popupMenu=new PopupMenu(context,view, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_pieces,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Edit")){
                            Intent intent = new Intent(context, EditPieceActivity.class);
                            intent.putExtra("person", person);
                            intent.putExtra("perName",perName);
                            intent.putExtra("pieceId", pieceId);
                            intent.putExtra("piece", piece);
                            intent.putExtra("perId", perId);
                            startActivity(intent);
                        }
                        else if(item.getTitle().equals("Delete")){
                            delete(pieceId);
                        }
                        else if(item.getTitle().equals("Add Expense")){
                            Intent intent = new Intent(context, AddExpenseActivity.class);
                            intent.putExtra("person", person);
                            intent.putExtra("perName",perName);
                            intent.putExtra("pieceId", pieceId);
                            intent.putExtra("piece", piece);
                            intent.putExtra("perId", perId);
                            startActivity(intent);
                        }
                        else if(item.getTitle().equals("View Information")){
                            Intent intent = new Intent(context, ViewPieceInfoActivity.class);
                            intent.putExtra("person", person);
                            intent.putExtra("perName",perName);
                            intent.putExtra("pieceId", pieceId);
                            intent.putExtra("piece", piece);
                            intent.putExtra("perId", perId);
                            startActivity(intent);
                        }
                        else if(item.getTitle().equals("Update Status")){
                            Message.message(context, "Update Status");
                            Intent intent = new Intent(context, UpdateStatusActivity.class);
                            intent.putExtra("person", "Customer");
                            intent.putExtra("perName", perName);
                            intent.putExtra("piece", piece);
                            intent.putExtra("pieceId", pieceId);
                            intent.putExtra("perId", perId);
                            startActivity(intent);
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
                if(person!=null) {
                    if (person.equalsIgnoreCase("Customer")) {
                        Intent intent = new Intent(context, AddPieceActivity.class);
                        intent.putExtra("person", person);
                        intent.putExtra("perName", perName);
                        intent.putExtra("perId", perId);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(context, AddPieceActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Intent intent = new Intent(context, AddPieceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_pieces);
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
            case R.id.view_received:
                cursor=myDbAdapter.getPiecesByStatus("RECEIVED");
                Message.message(context,"Viewing pieces received");
                cur();
                break;
            case R.id.view_all:
                cursor=myDbAdapter.getPiecesSorted();
                cur();
                break;
            case R.id.view_inProcess:
                cursor = myDbAdapter.getPiecesByStatus("IN PROCESS");
                Message.message(context,"Viewing pieces in process");
                cur();
                break;
            case R.id.view_completed:
                Message.message(context,"Viewing completed pieces");
                cursor = myDbAdapter.getPiecesByStatus("COMPLETED");
                cur();
                break;
            case R.id.view_cus:
                cursor = myDbAdapter.getPiecesSorted();
                Message.message(context,"Showing all pieces");
                Person.setText("Customer");
                cur();
                break;
            case R.id.view_emb:
                cursor = myDbAdapter.getPiecesPer("Embroider");
                Message.message(context,"Showing active pieces");
                Person.setText("Embroider");
                cur();
                break;
            case R.id.view_tai:
                cursor = myDbAdapter.getPiecesPer("Tailor");
                Message.message(context,"Showing active pieces");
                Person.setText("Tailor");
                cur();
                break;
            case R.id.view_unpaid:
                cursor = myDbAdapter.getPiecesUnpaid();
                Message.message(context,"");
                cur();
                break;
            case R.id.add:
                if(person!=null) {
                    if (person.equalsIgnoreCase("Customer")) {
                        intent = new Intent(context, AddPieceActivity.class);
                        intent.putExtra("person", person);
                        intent.putExtra("perName", perName);
                        intent.putExtra("perId", perId);
                        startActivity(intent);
                    } else {
                        intent = new Intent(context, AddPieceActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    intent = new Intent(context, AddPieceActivity.class);
                    startActivity(intent);
                }
                break;

        }
        return true;
    }
    String person(String person){
        Person.setText(person);
        cursor=myDbAdapter.getPersons(person);
        if(cursor.getCount()==0)
        {
            Message.message(context,"No "+person+"s Found! Displaying all pieces.");
            cusName.setText("All");
            Person.setText("Category");
            function();
        }
        else if(cursor.getCount()==1){
            Message.message(context,"One "+person+" found, selected!");
            perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            cusName.setText(perName);
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
            Window window = dialog1.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        return perId;
    }
    String ExName,ExType,ExId;
    void delete(String id){
        cursor = myDbAdapter.getPiece(Integer.parseInt(id));
        ExName = cursor.getString(cursor.getColumnIndexOrThrow("EXNAME"));
        ExType = cursor.getString(cursor.getColumnIndexOrThrow("EXTYPE"));
        final alertDialog dialog = new alertDialog(context);
        dialog.setTitle("Confirm Delete","This action cannot be undone. Are you sure you still want to continue?","Yes","No");
        dialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myDbAdapter.deletePiece(pieceId)){
                    cursor = myDbAdapter.getPerson(person, perId);
                    int nop;
                    if(cursor.getCount()>0) {
                        nop = cursor.getInt(cursor.getColumnIndexOrThrow("NOP"));
                        nop--;
                        myDbAdapter.updateNOP(perId, person, perName, String.valueOf(nop));
                    }
                    if(!ExType.equalsIgnoreCase("ME")&&!ExType.equalsIgnoreCase("-")&&!ExType.equalsIgnoreCase("Other")) {
                        cursor = myDbAdapter.getPersonByName(ExName,ExType);
                        if(cursor.getCount()>0)
                            ExId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                        cursor = myDbAdapter.getPerson(ExType, ExId);
                        double amount=0.0;
                        if(cursor.getCount()>0)
                            amount = cursor.getDouble(cursor.getColumnIndexOrThrow("PAYMENT"));
                        cursor=myDbAdapter.getExpenseForPiece(pieceId);
                        if(cursor.getCount()>0) {
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
                    Message.message(context,"Piece successfully deleted.");
                    dialog.dismiss();
                }
                else
                    Message.message(context,"Piece could not be deleted!");
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
