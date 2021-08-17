/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.annotation.SuppressLint;
import android.app.SearchManager;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InventoryActivity extends AppCompatActivity {
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Context context=this;
    Cursor cursor=null;
    ListView listView;
    TextView total,tot_cp,captcha;
    double TCP,TSP,TP;
    String query;
    int random;
    Animation fab_open, fab_close, fab_clock, fab_anticlock;
    SimpleCursorAdapter simpleCursorAdapter;
    FloatingActionButton fab1, fab2, fab3, fab4, fab5;
    boolean isOpen = false;
    TextView  hint2, hint3, hint4, hint5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message.message(context,"Rotate your mobile for better viewing");
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);
        view_available();
    }
    private void onClick(final String id,View view){

    }
    private void onLongClick(final String id, View view){
        cursor = myDbAdapter.getItemFromInventory(id);
        itemId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        final PopupMenu popupMenu=new PopupMenu(context,view, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.popup_inventory,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Edit")){
                    fab3(itemId);
                }
                else if(item.getTitle().equals("Delete")){
                    fab2(itemId);
                }
                else if(item.getTitle().equals("Sell")){
                    Intent intent = new Intent(context, InventorySaleActivity.class);
                    intent.putExtra("itemId",id);
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show();
    }
    protected void fab3(String itemId){
        //babytodo
        Intent intent = new Intent(context, InventoryEditActivity.class);
        intent.putExtra("itemId",itemId);
        startActivity(intent);
    }
    protected void fab2(final String itemId){
        alertDialog dialog=new alertDialog(context);
        dialog.setTitle("Confirm Delete", "Are you sure you want to remove this item from your inventory", "Yes", "No");
        dialog.positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final alertDialog dialog1 = new alertDialog(context, "Confirm clear dues", false);
                final TextInputEditText textInputEditText = dialog1.textInputEditText;
                final TextInputLayout textInputLayout = dialog1.textInputLayout;
                dialog1.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textInputEditText.getText().toString().equalsIgnoreCase(String.valueOf(dialog1.random))) {
                            if (myDbAdapter.deleteFromInventory(itemId)) {
                                Message.message(context, "Item Successfully Deleted");
                                finish();
                                startActivity(getIntent());
                            } else {
                                Message.message(context, "Unsuccessful");
                            }
                        } else {
                            textInputLayout.setError("Invalid captcha! Try Again");
                        }
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
    String itemId;
    protected void list(String message,final Intent intent){
        cursor=myDbAdapter.getAvailableInventory();
        String[] columns = {"DATEADDED", "ITEM", "DESCRIPTION", "COSTPRICE"};
        int[] to = {R.id.t1, R.id.t2, R.id.t3, R.id.t4};
        simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_inventory, cursor, columns,to, 0);
        final alertDialog dialog=new alertDialog(context,simpleCursorAdapter,"Choose one",message);
        final ListView listView=dialog.listView;
        listView.setAdapter(simpleCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                cursor = (Cursor) listView.getItemAtPosition(position);
                itemId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_inventory);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView=null;
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        if(searchItem!=null)
        {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                function(query.toUpperCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                function(newText.toUpperCase());
                return true;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        return true;
    }
    protected void function(String inputText){
        if(inputText.length()==0){
            cursor = myDbAdapter.getInventoryByName(inputText,query);
            simpleCursorAdapter.swapCursor(cursor);
            listView.setAdapter(simpleCursorAdapter);
        }
        else{
            Cursor cursor= myDbAdapter.getInventoryByName(inputText.toUpperCase(),query);
            total.setText(cursor.getCount()+" found");
            simpleCursorAdapter.swapCursor(cursor);
            listView.setAdapter(simpleCursorAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor =(Cursor) listView.getItemAtPosition(position);
                onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                onLongClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
                return true;
            }
        });
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
            case R.id.view_all:
                view_all();
                break;
            case R.id.view_available:
                view_available();
                break;
            case R.id.view_sold:
                view_sold();
                break;
        }
        return true;
    }
    @SuppressLint("RestrictedApi")
    private void view_all(){                //all items in inventory
        setContentView(R.layout.activity_inventory_sold);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Inventory");
        getSupportActionBar().setSubtitle("All Items");
        query="";
        listView = findViewById(R.id.listView);
        TextView tcp,tsp,tp;
        tcp = findViewById(R.id.tcp);
        tsp = findViewById(R.id.tsp);
        tp = findViewById(R.id.tp);
        final ListView listView = findViewById(R.id.listView);
        String[] columns = {"DATEADDED", "ITEM", "COSTPRICE", "DESCRIPTION", "DATESOLD", "SOLDTO", "SELLINGPRICE", "PROFIT"};
        cursor = myDbAdapter.getInventory();
        if (cursor.getCount() == 0) {
            Message.message(context, "No Items found!");
            finish();
        } else {
            int[] to = new int[]{R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5, R.id.a6, R.id.a7, R.id.a8};
            simpleCursorAdapter = new SimpleCursorAdapter(
                    this, R.layout.inventory_info,
                    cursor,
                    columns,
                    to,
                    0);
            listView.setAdapter(simpleCursorAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")), view);
                }
            });
            cursor.moveToFirst();
            do {
                TCP += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
                TSP += cursor.getDouble(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
                TP += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
            }
            while (cursor.moveToNext());
            tcp.setText(String.valueOf(TCP));
            tsp.setText(String.valueOf(TSP));
            tp.setText(String.valueOf(TP));
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    onLongClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
                    return true;
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
                }
            });
            fab();
        }
    }
    @SuppressLint("RestrictedApi")
    private void view_available(){          //available items in inventory
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Inventory");
        getSupportActionBar().setSubtitle("Available Items");
        query="AND SELLINGPRICE = '-'";
        total = findViewById(R.id.total);
        tot_cp = findViewById(R.id.tot_cp);
        fab();
        listView = findViewById(R.id.listView);
        cursor=myDbAdapter.getAvailableInventory();
        if(cursor.getCount()==0){
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
            findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearlayout1).setVisibility(View.INVISIBLE);
            findViewById(R.id.add_now).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(InventoryActivity.this,AddToInventoryActivity.class));
                }
            });
        }
        else {
            cursor=myDbAdapter.getAvailableInventory();
            findViewById(R.id.empty).setVisibility(View.INVISIBLE);
            String[] columns = {"DATEADDED", "ITEM", "DESCRIPTION", "COSTPRICE"};
            int[] to = {R.id.t1, R.id.t2, R.id.t3, R.id.t4};
            simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_inventory, cursor, columns,to, 0);
            total.setText(cursor.getCount()+"");
            listView.setAdapter(simpleCursorAdapter);
            double cp=0.0;
            do{
                cp += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
            }
            while (cursor.moveToNext());
            tot_cp.setText(cp+"");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor = (Cursor) listView.getItemAtPosition(position);
                onLongClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
                return true;
            }
        });
    }
    @SuppressLint("RestrictedApi")
    public void fab() {
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab5 = findViewById(R.id.fab5);
        hint2 = findViewById(R.id.hint2);
        hint3 = findViewById(R.id.hint3);
        hint4 = findViewById(R.id.hint4);
        hint5 = findViewById(R.id.hint5);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        fab4.setVisibility(View.INVISIBLE);
        fab5.setVisibility(View.INVISIBLE);
        hint2.setVisibility(View.INVISIBLE);
        hint3.setVisibility(View.INVISIBLE);
        hint4.setVisibility(View.INVISIBLE);
        hint5.setVisibility(View.INVISIBLE);
        fab1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);
                    fab4.setVisibility(View.VISIBLE);
                    fab5.setVisibility(View.VISIBLE);
                    hint2.setVisibility(View.VISIBLE);
                    hint3.setVisibility(View.VISIBLE);
                    hint4.setVisibility(View.VISIBLE);
                    hint5.setVisibility(View.VISIBLE);
                    fab2.startAnimation(fab_close);
                    fab4.startAnimation(fab_close);
                    fab3.startAnimation(fab_close);
                    fab5.startAnimation(fab_close);
                    hint2.startAnimation(fab_close);
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
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,AddToInventoryActivity.class));
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InventorySaleActivity.class);
                if (cursor.getCount() != 0) {
                    list("Select an item to sell",intent);
                }
                else
                    Message.message(context,"No items found in inventory!");
            }
        });
    }
    @SuppressLint("RestrictedApi")
    public void hide_all() {
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        fab4.setVisibility(View.INVISIBLE);
        fab5.setVisibility(View.INVISIBLE);
        hint2.setVisibility(View.INVISIBLE);
        hint3.setVisibility(View.INVISIBLE);
        hint4.setVisibility(View.INVISIBLE);
        hint5.setVisibility(View.INVISIBLE);
        fab2.startAnimation(fab_open);
        fab4.startAnimation(fab_open);
        fab3.startAnimation(fab_open);
        fab5.startAnimation(fab_open);
        hint2.startAnimation(fab_open);
        hint4.startAnimation(fab_open);
        hint3.startAnimation(fab_open);
        hint5.startAnimation(fab_open);
        fab1.startAnimation(fab_clock);
        isOpen = true;
    }
    @SuppressLint("RestrictedApi")
    private void view_sold(){
        setContentView(R.layout.activity_inventory_sold);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Inventory");
        getSupportActionBar().setSubtitle("Items Sold");
        query="AND SELLINGPRICE <> '-'";
        fab();
        listView = findViewById(R.id.listView);
        TextView tcp,tsp,tp;
        tcp = findViewById(R.id.tcp);
        tsp = findViewById(R.id.tsp);
        tp = findViewById(R.id.tp);
        final ListView listView = findViewById(R.id.listView);
        String[] columns = {"DATEADDED", "ITEM", "COSTPRICE", "DESCRIPTION", "DATESOLD", "SOLDTO", "SELLINGPRICE", "PROFIT"};
        cursor = myDbAdapter.getItemsSoldFromInventory();
        if(cursor.getCount()==0){
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
            findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.linearlayout1).setVisibility(View.INVISIBLE);
            findViewById(R.id.add_now).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(InventoryActivity.this,AddToInventoryActivity.class));
                }
            });
        }
        else {
            int[] to = new int[]{R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5, R.id.a6, R.id.a7, R.id.a8};
            simpleCursorAdapter = new SimpleCursorAdapter(
                    this, R.layout.inventory_info,
                    cursor,
                    columns,
                    to,
                    0);
            listView.setAdapter(simpleCursorAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")), view);
                }
            });
            cursor.moveToFirst();
            do {
                TCP += cursor.getDouble(cursor.getColumnIndexOrThrow("COSTPRICE"));
                TSP += cursor.getDouble(cursor.getColumnIndexOrThrow("SELLINGPRICE"));
                TP += cursor.getDouble(cursor.getColumnIndexOrThrow("PROFIT"));
            }
            while (cursor.moveToNext());
            tcp.setText(String.valueOf(TCP));
            tsp.setText(String.valueOf(TSP));
            tp.setText(String.valueOf(TP));
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    onLongClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
                    return true;
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cursor = (Cursor) listView.getItemAtPosition(position);
                    onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")),view);
                }
            });
        }
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
    public void onPause(){
        super.onPause();
        hide_all();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
