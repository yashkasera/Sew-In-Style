/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

public class CusTaiEmbActivity extends AppCompatActivity {
    public static final String TAG = "CusTaiEmbctivity";
    Context context = this;
    MyDbAdapter myDbAdapter = new MyDbAdapter(this);
    Cursor cursor=null;
    ListView listView;String[] columns={"NAME"};
    int[] to = {R.id.textView};
    String per="",person="",perName="";
    TextView total;
    SimpleCursorAdapter simpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_tai_emb);
        person = getIntent().getStringExtra("person");
        per = getIntent().getStringExtra("per");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(person);
        total = findViewById(R.id.total);
        listView = findViewById(R.id.listView);
        simpleCursorAdapter = new SimpleCursorAdapter(context, R.layout.list_person, cursor, columns, to, 0);
        listView.setAdapter(simpleCursorAdapter);
        cursor = myDbAdapter.getPersons(person);
        total.setText(cursor.getCount()+" "+person+"s");
        if(cursor.getCount()==1)
            total.setText(cursor.getCount()+" "+person);
        function("");
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPersonActivity.class);
                intent.putExtra("person", person);
                intent.putExtra("per", per);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
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
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_sort_name:
                cursor=myDbAdapter.getPersonSorted(person,"NAME");
                simpleCursorAdapter.swapCursor(cursor);
                Message.message(context,"Sorted by Name");
                break;
            case R.id.action_sort_nop:
                cursor=myDbAdapter.getPersonSorted(person,"NOP");
                simpleCursorAdapter.swapCursor(cursor);
                Message.message(context,"Sorted by number of pieces");
                break;
            case R.id.edit:
                editActionMenu();
                break;
            case R.id.delete:
                deleteActionMenu();
                break;
            default:
                break;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor =(Cursor) listView.getItemAtPosition(position);
                onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
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
        return true;
    }
    protected void function(String inputText){
        if(inputText.length()==0){
            cursor = myDbAdapter.getPersons(person);
            simpleCursorAdapter.swapCursor(cursor);
            listView.setAdapter(simpleCursorAdapter);
        }
        else{
            Cursor cursor= myDbAdapter.getPersonByName(inputText.toUpperCase(), person);
            total.setText(cursor.getCount()+" found");
            simpleCursorAdapter.swapCursor(cursor);
            listView.setAdapter(simpleCursorAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor =(Cursor) listView.getItemAtPosition(position);
                onClick(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
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
    public void onClick(String id){
        cursor = myDbAdapter.getPerson(person, id);
        String perName=cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
        if(person.equalsIgnoreCase("Customer")) {
            Intent intent = new Intent(context, CustomerActivity.class);
            intent.putExtra("perName", perName);
            intent.putExtra("person", person);
            intent.putExtra("per", per);
            intent.putExtra("perId", id);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, EmbTaiActivity.class);
            intent.putExtra("perName", perName);
            intent.putExtra("person", person);
            intent.putExtra("per", per);
            intent.putExtra("perId", id);
            startActivity(intent);
        }
        Message.message(context,perName);
    }
    public void onLongClick(final String id, View view){
        cursor = myDbAdapter.getPerson(person, id);
        final String perName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
        final PopupMenu popupMenu=new PopupMenu(context,view,Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.cet_popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Message.message(context,item.getTitle()+"");
                if(item.getTitle().equals("Call")){
                    Message.message(context,"Call "+perName);
                    call(cursor.getLong(cursor.getColumnIndexOrThrow("MOBILE")));
                }
                else if(item.getTitle().equals("Edit")){
                    edit(id);
                }
                else if(item.getTitle().equals("Delete")){
                    delete(id);
                }
                else if(item.getTitle().equals("Information")){
                    Intent intent = new Intent(context, ViewPersonInfoActivity.class);
                    intent.putExtra("perName", perName);
                    intent.putExtra("person", person);
                    intent.putExtra("per", per);
                    intent.putExtra("perId", id);
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show();
    }
    protected void edit(String perId){
        Intent intent = new Intent(context, EditPersonActivity.class);
        intent.putExtra("person", person);
        intent.putExtra("per", "per");
        intent.putExtra("perId", perId);
        startActivity(intent);
    }
    protected void delete(String perId){
        Intent intent = new Intent(context, DeletePersonActivity.class);
        intent.putExtra("person", person);
        intent.putExtra("per", "per");
        intent.putExtra("perId", perId);
        startActivity(intent);
    }
    protected void call(long tel){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tel));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    protected void editActionMenu(){
        Message.message(context,"ERROR#404 - Page Under Construction");
        //TODO
    }
    protected void deleteActionMenu(){
        Message.message(context,"ERROR#404 - Page Under Construction");
        //TODO
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
