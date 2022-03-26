package com.example.nimmakhaate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText amount;
    Button youllget, youllgive, delete, send;
    int newamt, updatedAmttoget, updatedAmttogive;
    String message, phone = "", pendingamt;
    DBHelper DB = new DBHelper(UpdateActivity.this);

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        amount = findViewById(R.id.amount);
        youllget = findViewById(R.id.youllget);
        youllgive = findViewById(R.id.youllgive);
        delete = findViewById(R.id.delete);
        send = findViewById(R.id.whatsapp);

        getIntentData();

        youllgive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newamt = Integer.parseInt(amount.getText().toString().trim());
                if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(UpdateActivity.this, "Amount is required",Toast.LENGTH_SHORT).show();
                }
//                System.out.println(amt);
                Cursor cursor = DB.updateGiveAmt(id);

                while (cursor.moveToNext()) {
                    int dbgiveamt = Integer.parseInt(cursor.getString(0));
                    int dbgetamt = Integer.parseInt(cursor.getString(1));
                    if(dbgiveamt == 0 && dbgetamt == 0){
                        updatedAmttoget = dbgetamt + newamt;
                    }
                    else if(dbgiveamt == 0){
                        updatedAmttoget = dbgetamt + newamt;
                        System.out.println(updatedAmttoget);
                    }
                    else if(dbgetamt == 0 && newamt <= dbgiveamt){
                        updatedAmttogive = dbgiveamt - newamt;
                    }
                    else if(newamt >= dbgiveamt){
                        updatedAmttogive = 0;
                        updatedAmttoget = newamt - dbgiveamt;
                    }

                    else {
                        updatedAmttoget = (dbgetamt + newamt)-dbgiveamt;
                        updatedAmttogive = dbgiveamt - newamt;
                        if(updatedAmttogive <= 0 || newamt <= updatedAmttogive){
                            updatedAmttogive = 0;
                        }
                    }
                    DB.setNewAmt(id,updatedAmttogive,updatedAmttoget);

//                    System.out.println("amt to get: "  + updatedAmttoget);
//                    System.out.println("amt to give: " + updatedAmttogive);
                }

            }
        });

        youllget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newamt = Integer.parseInt(amount.getText().toString().trim());
                if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(UpdateActivity.this, "Amount is required",Toast.LENGTH_SHORT).show();
                }
//                System.out.println(newamt);
                Cursor cursor = DB.updateGiveAmt(id);
                while (cursor.moveToNext()){
                    int dbgiveamt = Integer.parseInt(cursor.getString(0));
                    int dbgetamt = Integer.parseInt(cursor.getString(1));
                    if(dbgiveamt == 0 && dbgetamt == 0){
                        updatedAmttogive = dbgiveamt + newamt;
                    }
                    else if(dbgetamt == 0){
                        updatedAmttogive = dbgiveamt + newamt;
                    }
                    else if(dbgiveamt == 0 && newamt <= dbgetamt){
                        updatedAmttoget = dbgetamt - newamt;
                    }
                    else if(newamt >= dbgetamt){
                        updatedAmttoget = 0;
                        updatedAmttogive = newamt - dbgetamt;
                    }
                    else{
                        updatedAmttogive = (dbgiveamt + newamt) - dbgetamt;
                        updatedAmttoget = dbgetamt - newamt;
                        if(updatedAmttoget <= 0 || newamt >= updatedAmttogive){
                            updatedAmttoget = 0;
                        }
                    }
                    DB.setNewAmt(id,updatedAmttogive,updatedAmttoget);
//                    System.out.println("amt to give: "+  updatedAmttogive);
//                    System.out.println("amt to get: "+  updatedAmttoget);

                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = String.format("Dear Sir/Madam,"+"\n"+"Your payment of rupees %s is pending at my business. Please do the needful as soon as possible!!"+"\n"+"- Nimma Khaate app", pendingamt);
//                System.out.println(message);
                Cursor cursor = DB.getphone(id);
                while (cursor.moveToNext()) {
                    phone = cursor.getString(0);
                    phone = "+91" + phone;
                    System.out.println(message);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phone+"&text="+message));
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialogebox();
            }
        });

    }

    void getIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("pendingamt")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            pendingamt = getIntent().getStringExtra("pendingamt");
//            System.out.println(pendingamt);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isinstalled(){
        PackageManager packageManager = getPackageManager();
        boolean whatsappInstalled;

        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        }catch (PackageManager.NameNotFoundException e){
            whatsappInstalled = false;
        }
        return whatsappInstalled;
    }



    void confirmDialogebox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper DB = new DBHelper(UpdateActivity.this);
                DB.deleteonerow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }


}