package com.example.nimmakhaate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {
    TextInputEditText custname, custnum;
    Boolean insert;
    Button bt;
    DBHelper DB = new DBHelper(AddActivity.this);
//    HomeActivity ha = new HomeActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        showCustomDialogue();

    }

    void showCustomDialogue(){
//        final Dialog dialog = new Dialog(AddActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.activity_add);

//        custname = dialog.findViewById(R.id.customername);
//        custnum = dialog.findViewById(R.id.customernumebr);
//        bt = dialog.findViewById(R.id.addbtn);
        custname = findViewById(R.id.customername);
        custnum = findViewById(R.id.customernumebr);
        bt = findViewById(R.id.addbtn);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Objects.requireNonNull(custname.getText()).toString().trim();
                String custnumber = custnum.getText().toString().trim();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(custnum.getText().toString())){
                    Toast.makeText(AddActivity.this, "All fields are required",Toast.LENGTH_SHORT).show();
                }
                else {
                    insert = DB.addcustomer(name, custnumber);
                    if(insert == true) {
                        Toast.makeText(AddActivity.this, "Customer added successfully", Toast.LENGTH_SHORT).show();
//                    ha.storeInArray();
                        System.out.println("customer added");

                    }
                    else {
                        Toast.makeText(AddActivity.this, "adding failed", Toast.LENGTH_SHORT).show();
                        System.out.println("adding failed!!");
                    }

                }


//                dialog.dismiss();



            }
        });
//        dialog.show();


}


}