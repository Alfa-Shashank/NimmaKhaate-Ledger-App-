package com.example.nimmakhaate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import javax.xml.transform.sax.SAXResult;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "NimmaKhaate.db";
    private Context context;

    private int newuserid;
    private int loginuserid;
    int customerid = 0;
    MainActivity m;
    public DBHelper(Context context) {

        super(context, "login.db", null, 1);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table users( username TEXT primary key, password TEXT)");
//        db.execSQL("create table customers(id INTEGER primary key AUTOINCREMENT, customername TEXT, customernumber VARCHAR(20), amttogive INTEGER, amttoget INTEGER)");

        db.execSQL("create table users(user_id INTEGER primary key, username TEXT, password TEXT)");
        db.execSQL("create table customers(customer_id INTEGER primary key AUTOINCREMENT, customer_name TEXT, customer_number VARCHAR(20),user_id INTEGER, FOREIGN KEY (user_id) REFERENCES users (user_id) on delete cascade)");
        db.execSQL("create table transactions(customer_id INTEGER, amttogive INTEGER, amttoget INTEGER, FOREIGN KEY (customer_id) REFERENCES customers (customer_id) on delete cascade)");
        db.execSQL("create table loginusers(userid INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists customers");
        onCreate(db);



    }

    public boolean insertData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        new lines added 50 and 57
        Cursor cursor = db.rawQuery("select * from users ", null);
        if(cursor.getCount() > 0){
            cursor.moveToLast();
            int lastuserid = Integer.valueOf(cursor.getString(0));
//            System.out.println(lastuserid);
            newuserid = lastuserid + 1;
//            System.out.println(newuserid);
        }
        else{
            newuserid = 1;
//            System.out.println(newuserid);
        }

        values.put("user_id",newuserid);
        values.put("username",username);
        values.put("password",password);

        long result = db.insert("users",null,values);
        if(result == -1){
            newuserid-=1;
            return false;
        }
        else
            loginuserid = newuserid;
            ContentValues v = new ContentValues();
            v.put("userid",loginuserid);
            db.insert("loginusers",null,v);
            return true;

    }
//    new method added
//    Cursor getuserid(String username){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select user_id from users where username = ?", new String[] {username});
//        if(cursor.getCount() > 0){
//            return cursor;
//        }
//    }

    public Boolean checkusername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username = ?", new String[] {username});
        if(cursor.getCount() > 0){
            return true;
        }
        else
            return false;

    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
//            new lines added 89-92
            Cursor cursor1 = db.rawQuery("select user_id from users where username = ? and password = ?", new String[]{username, password});
            while (cursor1.moveToNext()) {
                loginuserid = Integer.parseInt(cursor1.getString(0));
                System.out.println("login userid: "+ loginuserid);
            }
            return true;
        } else
            return false;
    }

    public Boolean addcustomer(String customername, String customernumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        new line added 102
        cv.put("customer_name",customername);
        cv.put("customer_number",customernumber);
//        loginuserid = m.userid;
        cv.put("user_id",loginuserid);
//        uncomment below lines for previous version
//        cv.put("amttoget",0);
//        cv.put("amttogive",0);
        long result = db.insert("customers",null,cv);
        if(result == -1){
            return false;
        }
        else {
//            new lines added 115 - 130
            ContentValues cv1 = new ContentValues();
            Cursor cursor = db.rawQuery("select customer_id from customers where customer_name = ? and user_id = ? and customer_number = ?", new String[]{customername, String.valueOf(loginuserid), customernumber});
            while (cursor.moveToNext()) {
                customerid = Integer.parseInt(cursor.getString(0));
            }
            cv1.put("customer_id",customerid);
            cv1.put("amttoget",0);
            cv1.put("amttogive",0);
            long result1 = db.insert("transactions",null,cv1);
            if(result1 == -1){
                return false;
            }
            else {

                return true;
            }
//            return true

        }

    }
//below method name changed from readdata to customerreaddata
    Cursor customerreaddata(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null){
            Cursor cursor = db.rawQuery("select customer_id, customer_name, user_id from customers where user_id = ? group by customer_id", new String[]{String.valueOf(loginuserid)});
            return cursor;
        }
        else{
            return null;
        }

//        if(db != null){
//            cursor = db.rawQuery(query,null);
//        }
//        return cursor;
    }
//    below method is new
    Cursor transactionreaddata(String custid){
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = null;
        Cursor cursor = db.rawQuery("select amttogive, amttoget from transactions where customer_id=? group by customer_id", new String[]{String.valueOf(custid)});
//        String query = "select * from transactions";
//        if(db != null){
//            cursor = db.rawQuery(query,null);
//        }
        return cursor;
    }

    Cursor getphone(String row_id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select customer_number from customers where customer_id = ?", new String[]{row_id});

        return cursor;
    }

    Cursor updateGiveAmt(String row_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        String row_id = "1";
//        Cursor cursor = db.rawQuery("select amttogive, amttoget from customers where id = ?", new String[]{row_id});
//        new line below
        Cursor cursor = db.rawQuery("select amttogive, amttoget from transactions where customer_id = ?", new String[]{row_id});
//        while (cursor.moveToNext()) {
//            int amt = Integer.parseInt(cursor.getString(0));
//            int updatedAmttoget = amt + newamt;
//            System.out.println(updatedAmttoget);
//        }
        return cursor;

    }

//    Cursor updateGetAmt(String row_id) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
////        String row_id = "1";
//        Cursor cursor = db.rawQuery("select amttoget from customers where id = ?", new String[]{row_id});
////        while (cursor.moveToNext()) {
////            int amt = Integer.parseInt(cursor.getString(0));
////            int updatedAmttoget = amt + newamt;
////            System.out.println(updatedAmttoget);
////        }
//        return cursor;

//    }

    void setNewAmt(String row_id, int amttogive, int amttoget){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("amttogive", amttogive);
        cv.put("amttoget", amttoget);
//        long result = db.update("customers", cv, "id=?", new String[]{row_id});
        long result = db.update("transactions", cv, "customer_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteonerow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("customers", "customer_id=?", new String[]{row_id});
        long result1 = db.delete("transactions", "customer_id=?", new String[]{row_id});
        if(result == -1 || result1 == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Customer Deleted.", Toast.LENGTH_SHORT).show();
            Cursor cursor = customerreaddata();
            int i =0;
            while (cursor.moveToNext()){
                System.out.println(cursor.getString(i));

            }
        }


    }




}
