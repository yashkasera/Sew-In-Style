/*
 * Copyright (c) 2020. Yash Kasera
 * All Rights Reserved
 */

package com.yashkasera.sewinstyle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbAdapter extends SQLiteOpenHelper {
    public static final String DB_NAME="SewInStyle.db";
    public MyDbAdapter(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CUSTOMERS(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(25),MOBILE INTEGER,ADDRESS VARCHAR(255), NOP INTEGER, PAYMENT DECIMAL,PAYMODE VARCHAR(15),DATEADDED DATE)");
        db.execSQL("CREATE TABLE EMBROIDERS(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(25),MOBILE INTEGER,ADDRESS VARCHAR(255), NOP INTEGER, PAYMENT DECIMAL,PAYMODE VARCHAR(15),DATEADDED DATE)");
        db.execSQL("CREATE TABLE TAILORS(_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(25),MOBILE INTEGER,ADDRESS VARCHAR(255), NOP INTEGER, PAYMENT DECIMAL,PAYMODE VARCHAR(15),DATEADDED DATE)");
        db.execSQL("CREATE TABLE PIECES(_id INTEGER PRIMARY KEY AUTOINCREMENT,PERID INTEGER,NAME VARCHAR(25), PIECE VARCHAR(25),COST DECIMAL, PRICE DECIMAL, DESCRIPTION VARCHAR(255), STATUS VARCHAR(15),EXNAME VARCHAR(25),EXTYPE VARCHAR(25),DATEADDED DATE,DATEEXPECTED DATE, PAYSTATUS VARCHAR(15));");
        db.execSQL("CREATE TABLE MEASUREMENTS(_id INTEGER PRIMARY KEY AUTOINCREMENT,PERID INTEGER, NAME VARCHAR(25), NECK DECIMAL, SHOULDER DECIMAL, CHEST DECIMAL,ARMHOLE DECIMAL,SLEEVE DECIMAL,MOHRI DECIMAL,WAIST DECIMAL,HIPS DECIMAL,LENGTH DECIMAL,BOTTOMLENGTH DECIMAL);");
        db.execSQL("CREATE TABLE PAYMENTS(_id INTEGER PRIMARY KEY AUTOINCREMENT,PERID INTEGER,NAME VARCHAR(25),PIECE VARCHAR(15),PIECEID INTEGER, AMOUNT DECIMAL,DATEPAID DATE,DESCRIPTION VARCHAR(255),CATEGORY VARCHAR(15));");
        db.execSQL("CREATE TABLE EXPENSES(_id INTEGER PRIMARY KEY AUTOINCREMENT,PERID INTEGER, NAME VARCHAR(25),PIECEID INTEGER, PIECE VARCHAR(25), DATEADDED DATE,DESCRIPTION VARCHAR(50),AMOUNT DECIMAL);");
        db.execSQL("CREATE TABLE PROFITS(_id INTEGER PRIMARY KEY AUTOINCREMENT,PERID INTEGER, CUSTOMER VARCHAR(25),PIECEID INTEGER,PIECE VARCHAR(25),COSTPRICE DECIMAL,SELLINGPRICE DECIMAL, PROFIT DECIMAL,DATE DATE)");
        db.execSQL("CREATE TABLE INVENTORY(_id INTEGER PRIMARY KEY AUTOINCREMENT, ITEM VARCHAR(25),COSTPRICE DECIMAL,SELLINGPRICE DECIMAL,DATEADDED DATE,DATESOLD DATE,DESCRIPTION VARCHAR(50),PROFIT DECIMAL,SOLDTO VARCHAR(25));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CUSTOMERS");
        db.execSQL("DROP TABLE IF EXISTS EMBROIDERS");
        db.execSQL("DROP TABLE IF EXISTS TAILORS");
        db.execSQL("DROP TABLE IF EXISTS PIECES");
        db.execSQL("DROP TABLE IF EXISTS MEASUREMENTS");
        db.execSQL("DROP TABLE IF EXISTS PAYMENTS");
        db.execSQL("DROP TABLE IF EXISTS EXPENSES");
        db.execSQL("DROP TABLE IF EXISTS PROFITS");
        db.execSQL("DROP TABLE IF EXISTS INVENTORY");
    }
    public boolean insertPerson(String name,long mobile,String address, int nop,double payment,String paymode,String date,String category) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",name.toUpperCase());
        contentValues.put("MOBILE",mobile);
        contentValues.put("ADDRESS",address);
        contentValues.put("NOP",nop);
        contentValues.put("PAYMENT",payment);
        contentValues.put("PAYMODE",paymode);
        contentValues.put("DATEADDED",date);
        long result=0;
        if(category.equalsIgnoreCase("customer"))
            result=db.insert("CUSTOMERS",null,contentValues);
        else if(category.equalsIgnoreCase("embroider"))
            result=db.insert("EMBROIDERS",null,contentValues);
        else if(category.equalsIgnoreCase("tailor"))
            result=db.insert("TAILORS",null,contentValues);
        db.close();
        return result != -1;
    }
    public Cursor getPersons(String person){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=null;
        if(person.equalsIgnoreCase("customer"))
            cursor=db.rawQuery("SELECT * FROM CUSTOMERS ORDER BY NAME",null);
        else if(person.equalsIgnoreCase("embroider"))
            cursor=db.rawQuery("SELECT * FROM EMBROIDERS ORDER BY NAME",null);
        else if(person.equalsIgnoreCase("tailor"))
            cursor=db.rawQuery("SELECT * FROM TAILORS ORDER BY NAME",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getPerson(String person,String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=null;
        if(person.equalsIgnoreCase("customer"))
            cursor=db.rawQuery("SELECT * FROM CUSTOMERS WHERE _id='"+id+"'",null);
        else if(person.equalsIgnoreCase("embroider"))
            cursor=db.rawQuery("SELECT * FROM EMBROIDERS WHERE _id='"+id+"'",null);
        else if(person.equalsIgnoreCase("tailor"))
            cursor=db.rawQuery("SELECT * FROM TAILORS WHERE _id='"+id+"'",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getPersonByName(String inputText,String category) throws SQLException {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=null;
        if (inputText == null  ||  inputText.length () == 0) {
            if(category.equalsIgnoreCase("customer")) {
                cursor = db.query("CUSTOMERS", new String[]{"_id", "NAME", "MOBILE"}, null, null, null, null, "NAME");
            }
            else if(category.equalsIgnoreCase("embroider")) {
                cursor = db.query("EMBROIDERS", new String[]{"_id", "NAME", "MOBILE"}, null, null, null, null, "NAME");
            }
            else if(category.equalsIgnoreCase("tailor")) {
                cursor = db.query("TAILORS", new String[]{"_id", "NAME", "MOBILE"}, null, null, null, null, "NAME");
            }
        }
        else{
            if(category.equalsIgnoreCase("customer")) {
                cursor = db.query("CUSTOMERS", new String[]{"_id", "NAME", "MOBILE","NOP"}, "NAME like '%" + inputText + "%'", null, null, null, "NAME");
            }
            else if(category.equalsIgnoreCase("embroider")) {
                cursor = db.query("EMBROIDERS", new String[]{"_id", "NAME", "MOBILE","NOP"}, "NAME like '%" + inputText + "%'", null, null, null, "NAME");
            }
            else if(category.equalsIgnoreCase("tailor")) {
                cursor = db.query("TAILORS", new String[]{"_id", "NAME", "MOBILE","NOP"}, "NAME like '%" + inputText + "%'", null, null, null, "NAME");
            }
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPersonSorted(String person, String sort) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (person.equalsIgnoreCase("customer"))
            cursor = db.rawQuery("SELECT * FROM CUSTOMERS ORDER BY " + sort.toUpperCase(), null);
        else if (person.equalsIgnoreCase("embroider"))
            cursor = db.rawQuery("SELECT * FROM EMBROIDERS ORDER BY " + sort.toUpperCase(), null);
        else if (person.equalsIgnoreCase("tailor"))
            cursor = db.rawQuery("SELECT * FROM TAILORS ORDER BY " + sort.toUpperCase(), null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
    public boolean updateInfo(String id,String NewName,long mobile,String address,String person){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",NewName.toUpperCase());
        contentValues.put("MOBILE",mobile);
        contentValues.put("ADDRESS",address);
        int result=0;
        if(person.equalsIgnoreCase("customer")) {
            result = db.update("CUSTOMERS", contentValues, "_id='"+id+"'", null);
        }
        else if(person.equalsIgnoreCase("embroider")) {
            result = db.update("EMBROIDERS", contentValues, "_id='"+id+"'", null);
        }
        else if(person.equalsIgnoreCase("tailor")) {
            result = db.update("TAILORS", contentValues, "_id='"+id+"'", null);
        }
        db.close();
        return result > 0;
    }
    public boolean updatePersonPayment(String id,double amt,String person){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PAYMENT",amt);
        int result=0;
        if(person.equalsIgnoreCase("customer")) {
            result = db.update("CUSTOMERS", contentValues, "_id='"+id+"'", null);
        }
        else if(person.equalsIgnoreCase("embroider")) {
            result = db.update("EMBROIDERS", contentValues, "_id='"+id+"'", null);
        }
        else if(person.equalsIgnoreCase("tailor")) {
            result = db.update("TAILORS", contentValues, "_id='"+id+"'", null);
        }
        db.close();
        return result > 0;
    }
    public boolean deletePerson(String id,String category,String name){
        SQLiteDatabase db=this.getWritableDatabase();
        int result=0,result2=0;
        if(category.equalsIgnoreCase("customer")) {
            result = db.delete("CUSTOMERS", "_id = '"+id+"'", null);
            result2 = db.delete("MEASUREMENTS", "_id = '"+id+"'", null);
        }
        if(category.equalsIgnoreCase("embroider")) {
            result = db.delete("EMBROIDERS", "_id = '"+id+"'", null);
            name=name.toUpperCase();
            name=name.trim();
            name=name.replace(" ","");
            name+="2";
            db.execSQL("DROP TABLE IF EXISTS "+name);
        }
        if(category.equalsIgnoreCase("TAILOR")) {
            result = db.delete("TAILORS", "_id = '"+id+"'", null);
            name=name.toUpperCase();
            name=name.trim();
            name=name.replace(" ","");
            name+="3";
            db.execSQL("DROP TABLE IF EXISTS "+name);
        }

        db.close();
        return result > 0;
    }
    public boolean addPiece(String perId,String CusName,String piece,double cost,double price,String desc,String status,String ExName,String ExType,String DateAdded,String DateExpected,String pay_stat){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PERID",perId);
        contentValues.put("NAME",CusName.toUpperCase());
        contentValues.put("PIECE",piece.toUpperCase());
        contentValues.put("COST",cost);
        contentValues.put("PRICE",price);
        contentValues.put("DESCRIPTION",desc);
        contentValues.put("STATUS",status);
        contentValues.put("EXNAME",ExName);
        contentValues.put("EXTYPE",ExType);
        contentValues.put("DATEADDED",DateAdded);
        contentValues.put("DATEEXPECTED",DateExpected);
        contentValues.put("PAYSTATUS",pay_stat);
        long result=db.insert("PIECES",null,contentValues);
        db.close();
        return result != -1;
    }

    public boolean updateNOP(String id,String person,String name,String newNOP)
    {
        person+="s";
        person=person.toUpperCase();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NOP",newNOP);
        int result;
        if(id.length()>0)
            result=db.update(person,contentValues,"_id='"+id+"'",null);
        else
            result=db.update(person,contentValues,"NAME='"+name+"'",null);
        return result > 0;
    }
    public boolean updateCost(int id,double amount){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("COST",amount);
        int result=db.update("PIECES",contentValues,"_id='"+id+"'",null);
        return result > 0;
    }
    public boolean updatePiece(String pieceId,String piece,String DESCRIPTION,String PRICE ){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PIECE", piece);
        contentValues.put("DESCRIPTION", DESCRIPTION);
        contentValues.put("PRICE", PRICE);
        int result=db.update("PIECES",contentValues,"_id='"+pieceId+"'",null);
        return result > 0;
    }
    public Cursor getPieces(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesSorted(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES ORDER BY STATUS DESC",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesPer(String person){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES WHERE EXTYPE = '"+person.toUpperCase()+"'",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesUnpaid(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES WHERE PAYSTATUS='DUE' AND STATUS = 'COMPLETED'",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiece(int id){                 //By piece id
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES WHERE _id='"+id+"'",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesByStatus(String status){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PIECES WHERE STATUS = '"+status.toUpperCase()+"'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesOf(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES WHERE PERID = '"+id+"' ORDER BY Status DESC",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesOfIncomplete(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES WHERE PERID = '"+id+"'AND STATUS <> 'COMPLETED' ORDER BY STATUS DESC",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPiecesOfUnpaid(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM PIECES WHERE PERID = '"+id+"' AND PRICE = 0.0 ORDER BY Status DESC",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean updateStatus(String piece,String status,String exname,String extype,String dateExpected,String pieceId,double cost,double price,String pay_stat){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("STATUS",status.toUpperCase());
        contentValues.put("PIECE",piece.toUpperCase());
        contentValues.put("COST",cost);
        contentValues.put("PRICE",price);
        contentValues.put("EXNAME",exname.toUpperCase());
        contentValues.put("EXTYPE",extype.toUpperCase());
        contentValues.put("DATEEXPECTED",dateExpected);
        if(extype.equalsIgnoreCase("Customer")){
            contentValues.put("PAYSTATUS", pay_stat);
        }
        int result=db.update("PIECES",contentValues," _id = "+pieceId,null);
        return result > 0;
    }
    public boolean deletePiece(String pieceId){
        SQLiteDatabase db=this.getWritableDatabase();
        int result,result1,result2;
        result=db.delete("PIECES","_id = "+pieceId,null);
        result1=db.delete("PAYMENTS","PIECEID = "+pieceId,null);
        result2=db.delete("PROFITS","PIECEID = "+pieceId,null);
        return result >= 0;
    }
    public boolean deletePieceFromPersonTable(String pieceId, String name, String ExType){
        SQLiteDatabase db=this.getWritableDatabase();
        int no=0;
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        if(ExType.equalsIgnoreCase("embroider")) {
            no=2;
        }
        else
            no=3;
        name+=no;
        int result;
        result=db.delete(name,"PIECEID = '"+pieceId+"'",null);
        return result >= 0;
    }
    public Cursor getMeasurements(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM MEASUREMENTS WHERE PERID = '"+id+"'",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public boolean insertMeasurements(String id,String name, double neck,double shoulder,double chest,double armhole,double sleeve,double mohri,double waist,double hips,double length,double bottomLength) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PERID",id);
        contentValues.put("NAME", name);
        contentValues.put("NECK", neck);
        contentValues.put("SHOULDER",shoulder);
        contentValues.put("CHEST",chest);
        contentValues.put("ARMHOLE",armhole);
        contentValues.put("SLEEVE",sleeve);
        contentValues.put("MOHRI",mohri);
        contentValues.put("WAIST",waist);
        contentValues.put("HIPS",hips);
        contentValues.put("LENGTH",length);
        contentValues.put("BOTTOMLENGTH",bottomLength);
        long result=db.insert("MEASUREMENTS",null,contentValues);
        db.close();
        return result != -1;
    }
    public boolean updateMeasurements(String id,String name, double neck,double shoulder,double chest,double armhole,double sleeve,double mohri,double waist,double hips,double length,double bottomLength) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("NECK", neck);
        contentValues.put("SHOULDER",shoulder);
        contentValues.put("CHEST",chest);
        contentValues.put("ARMHOLE",armhole);
        contentValues.put("SLEEVE",sleeve);
        contentValues.put("MOHRI",mohri);
        contentValues.put("WAIST",waist);
        contentValues.put("HIPS",hips);
        contentValues.put("LENGTH",length);
        contentValues.put("BOTTOMLENGTH",bottomLength);
        int result=db.update("MEASUREMENTS",contentValues,"_id='"+id+"' AND NAME ='"+name+"'",null);
        db.close();
        return result > 0;
    }
    public boolean addExpense(String perId,String CusName, String PIECEID, String PIECE, String DATEADDED, String DESCRIPTION, String AMOUNT) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PERID",perId);
        contentValues.put("NAME", CusName.toUpperCase());
        contentValues.put("PIECEID",PIECEID);
        contentValues.put("PIECE",PIECE.toUpperCase());
        contentValues.put("DATEADDED",DATEADDED);
        contentValues.put("DESCRIPTION",DESCRIPTION);
        contentValues.put("AMOUNT",AMOUNT);
        long result=db.insert("EXPENSES",null,contentValues);
        db.close();
        return result != -1;
    }
    public Cursor getAllExpense(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM EXPENSES",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getCustomerExpense(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM EXPENSES WHERE PERID ='"+id+"'",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getPieceExpense(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM EXPENSES WHERE PIECEID ='"+id+"'",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getExpense(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM EXPENSES WHERE _id ='"+id+"'",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public boolean updateExpense(String id,String date,String desc,String AMOUNT){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("DESCRIPTION",desc);
        contentValues.put("DATEADDED",date);
        contentValues.put("AMOUNT",AMOUNT);
        int result=db.update("EXPENSES",contentValues,"_id="+id,null);
        db.close();
        return result > 0;
    }
    public boolean deleteExpense(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int result,result1;
        result=db.delete("EXPENSES", "_id = "+id,null);
        result1=db.delete("PIECES", "_id = "+id,null);
        return result >= 0;
    }
    public Cursor getExpenseForPiece(String pieceId){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM EXPENSES WHERE PIECEID ='"+pieceId+"'",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public boolean deleteExpenseForPiece(String pieceId){           //Delete all expenses of a piece while deleting it
        SQLiteDatabase db=this.getWritableDatabase();
        int result;
        result=db.delete("EXPENSES", "PIECEID = "+pieceId,null);
        return result >= 0;
    }
    public void createTableET(String per,String name){
        SQLiteDatabase db=this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        db.execSQL("CREATE TABLE "+name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,CUSID INTEGER, CUSNAME VARCHAR(15), PIECE VARCHAR(25), PIECEID INTEGER,STATUS VARCHAR(25), DATE DATE,COST DECIMAL,PAYSTATUS VARCHAR(10));");
    }
    public boolean addPieceForPerson(String per,String name,String cusId,String CUSNAME,String piece,String PieceId,double cost,String date,String payStat,String STATUS){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        contentValues.put("STATUS",STATUS);
        contentValues.put("CUSID",cusId);
        contentValues.put("CUSNAME",CUSNAME.toUpperCase());
        contentValues.put("PIECE",piece.toUpperCase());
        contentValues.put("PIECEID",PieceId);
        contentValues.put("date",date);
        contentValues.put("Cost",cost);
        contentValues.put("PAYSTATUS",payStat);
        long result=db.insert(name,null,contentValues);
        db.close();
        return result != -1;
    }
    public boolean updateTaskForET(String per,String name,String STATUS,String pieceId){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        contentValues.put("STATUS",STATUS);
        int result=db.update(name,contentValues,"PIECEID='"+pieceId+"'",null);
        db.close();
        return result > 0;
    }
    public boolean addToInventory(String ITEM,double COSTPRICE,String DATEADDED,String DESCRIPTION){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ITEM",ITEM.toUpperCase());
        contentValues.put("COSTPRICE",COSTPRICE);
        contentValues.put("DESCRIPTION",DESCRIPTION);
        contentValues.put("DATEADDED",DATEADDED);
        contentValues.put("SELLINGPRICE","-");
        contentValues.put("DATESOLD","-");
        contentValues.put("SOLDTO","-");
        contentValues.put("PROFIT","-");
        long result=db.insert("INVENTORY",null,contentValues);
        db.close();
        contentValues.clear();

        return result != -1;
    }
    public boolean onSoldFromInventory(String id, String SELLINGPRICE, String DATESOLD, String PROFIT, String SOLDTO){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("SELLINGPRICE",SELLINGPRICE);
        contentValues.put("DATESOLD",DATESOLD);
        contentValues.put("PROFIT",PROFIT);
        contentValues.put("SOLDTO",SOLDTO);
        int result=db.update("INVENTORY",contentValues,"_id="+id,null);
        db.close();
        return result > 0;
    }
    public Cursor getAvailableInventory(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM INVENTORY WHERE DATESOLD = '-' ORDER BY ITEM",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getInventory(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM INVENTORY ORDER BY ITEM",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getInventoryByName(String inputText,String query) throws SQLException {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=null;

        if (inputText == null  ||  inputText.length () == 0) {
            if(query.equalsIgnoreCase("")||query.length()==0)
              cursor =db.rawQuery("SELECT * FROM INVENTORY ORDER BY ITEM",null);
            else {
                query = query.substring(3);
                cursor = db.rawQuery("SELECT * FROM INVENTORY WHERE " + query + " ORDER BY ITEM", null);
            }
        }
       else{
            cursor = db.rawQuery("SELECT * FROM INVENTORY WHERE ITEM like '%" + inputText + "%' "+query+" ORDER BY ITEM",null);
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getItemFromInventory(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM INVENTORY WHERE _id="+id,null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getItemsSoldFromInventory(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM INVENTORY WHERE SELLINGPRICE <> '-' ORDER BY ITEM",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public boolean deleteFromInventory(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int result;
        result=db.delete("INVENTORY", "_id = "+id,null);
        return result >= 0;
    }
    public boolean editItemInventory(String id,String ITEM,String COSTPRICE,String DATEADDED,String DESCRIPTION){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("COSTPRICE",COSTPRICE);
        contentValues.put("DATEADDED",DATEADDED);
        contentValues.put("ITEM",ITEM);
        contentValues.put("DESCRIPTION",DESCRIPTION);
        int result=db.update("INVENTORY",contentValues,"_id="+id,null);
        db.close();
        return result > 0;
    }
    public boolean editItemInventorySold(String id,String ITEM,String COSTPRICE,String DATEADDED,String buyer,String SELLINGPRICE,String DATESOLD,String DESCRIPTION,String profit){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("COSTPRICE",COSTPRICE);
        contentValues.put("DATESOLD",DATESOLD);
        contentValues.put("DATEADDED",DATEADDED);
        contentValues.put("ITEM",ITEM);
        contentValues.put("SOLDTO",buyer);
        contentValues.put("SELLINGPRICE",SELLINGPRICE);
        contentValues.put("DESCRIPTION",DESCRIPTION);
        contentValues.put("PROFIT",profit);
        int result=db.update("INVENTORY",contentValues,"_id="+id,null);
        db.close();
        return result > 0;
    }
    public boolean addProfit(String CUSTOMER,String perId,String PIECEID,String PIECE,double COSTPRICE,double SELLINGPRICE,double PROFIT,String DATE){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("CUSTOMER",CUSTOMER.toUpperCase());
        contentValues.put("PERID",perId);
        contentValues.put("PIECEID",PIECEID);
        contentValues.put("PIECE",PIECE.toUpperCase());
        contentValues.put("COSTPRICE",COSTPRICE);
        contentValues.put("SELLINGPRICE",SELLINGPRICE);
        contentValues.put("PROFIT",PROFIT);
        contentValues.put("DATE",DATE);
        long result=db.insert("PROFITS",null,contentValues);
        db.close();
        return result >= 0;
    }

    public boolean updateProfit(String pieceId,String PIECE, String SELLINGPRICE, String PROFIT) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PIECE",PIECE.toUpperCase());
        contentValues.put("SELLINGPRICE",SELLINGPRICE);
        contentValues.put("PROFIT",PROFIT);
        int result=db.update("PROFITS",contentValues,"PIECEID="+pieceId,null);
        db.close();
        return result > 0;
    }
    public boolean updatePayment(String person,String perId,double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PAYMENT",amount);
        int result=0;
        if(person.equalsIgnoreCase("EMBROIDER"))
            result = db.update("EMBROIDERS", contentValues, "_id = '" + perId + "'", null);
        else if(person.equalsIgnoreCase("TAILOR"))
            result = db.update("TAILORS", contentValues, "_id = '" + perId + "'", null);
        db.close();
        return result > 0;
    }
    public boolean addPayment(String person,String perId,String perName,double amount,String date,String description,String piece,String pId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", perName.toUpperCase());
        contentValues.put("PERID",perId);
        contentValues.put("AMOUNT", amount);
        contentValues.put("DATEPAID", date);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("CATEGORY", person.toUpperCase());
        contentValues.put("PIECE",piece);
        contentValues.put("PIECEID",pId);
        long result=db.insert("PAYMENTS",null,contentValues);
        db.close();
        return result >= 0;
    }
    public Cursor getPersonTable(String name,String per){
        SQLiteDatabase db=this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        Cursor cursor=db.rawQuery("SELECT * FROM "+name+" WHERE PAYSTATUS='Due on Delivery'",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPersonTableById(String name,String per,String id){           //get a particular record from embroider's or tailor's table
        SQLiteDatabase db=this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        Cursor cursor=db.rawQuery("SELECT * FROM "+name+" WHERE _id = '"+id+"' ORDER BY DATE",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean updatePayStatusPerTable(String id,double cost,String name,String per){       //update the pay status of an embroider's or tailor's table
        SQLiteDatabase db = this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        ContentValues contentValues = new ContentValues();
        contentValues.put("Cost",cost);
        contentValues.put("PAYSTATUS","Paid");
        int result=db.update(name, contentValues, "_id = '" + id + "'", null);
        db.close();
        return result > 0;
    }
    public void updatePayStatusPerTableForAll(String name,String per){
        SQLiteDatabase db = this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        db.execSQL("UPDATE "+name+" set PAYSTATUS = 'Paid'");
        db.close();
    }
    public Cursor getPiecesForET(String name,String per){
        SQLiteDatabase db=this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        Cursor cursor=db.rawQuery("SELECT "+name+"._id, "+name+".CUSNAME NAME, "+name+".PIECE, "+name+".PAYSTATUS DESCRIPTION, "+name+".COST, PIECES.STATUS , "+name+".PIECEID , "+name+".CUSID PERID FROM "+name+" INNER JOIN PIECES ON "+name+".PIECEID=PIECES._id ORDER BY PIECES.STATUS DESC;",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }
    public Cursor getPiecesForETUnpaid(String name,String per){
        SQLiteDatabase db=this.getWritableDatabase();
        name=name.toUpperCase();
        name=name.trim();
        name=name.replace(" ","");
        name+=per;
        Cursor cursor=db.rawQuery("SELECT "+name+"._id, "+name+".CUSNAME, "+name+".PIECE, "+name+".PAYSTATUS, "+name+".COST, PIECES.STATUS , "+name+".PIECEID , "+name+".CUSID PERID FROM "+name+" INNER JOIN PIECES ON "+name+".PIECEID=PIECES._id WHERE "+name+".PAYSTATUS <> 'Paid';",null);
        if(cursor!=null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getPayments(String person,String function,String perId){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=null;
        if(function.equalsIgnoreCase("all")){
            cursor = db.rawQuery("SELECT * FROM PAYMENTS", null);
        }
        else if (function.equalsIgnoreCase("person")) {
            cursor = db.rawQuery("SELECT * FROM PAYMENTS WHERE CATEGORY = '" + person.toUpperCase() + "'", null);
        }
        else if (function.equalsIgnoreCase("perName")){
            cursor = db.rawQuery("SELECT * FROM PAYMENTS WHERE PERID = '" + perId + "' AND CATEGORY = '" + person.toUpperCase() + "'", null);
        }
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getPaymentPiece(String person, String id) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=null;
        cursor=db.rawQuery("SELECT * FROM PAYMENTS WHERE CATEGORY = '"+ person.toUpperCase() + "' AND PIECEID='"+id+"'",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getProfits() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT * FROM PROFITS",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getProfitsBy() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT * FROM PROFITS",null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}