package com.example.nimmakhaate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {


    FloatingActionButton addcust;
    DBHelper DB;
    TextInputEditText custname, custnum;
    Boolean insert;
    Button bt;
    ArrayList<String> customers,id;
    ArrayList<String> takeamt, giveamt;
    CustomersRVAdapter customersRVAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.RVitems);
        addcust = findViewById(R.id.FAbtn);
        DB = new DBHelper(this);
        addcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                startActivity(intent);
//                showCustomDialogue();


            }
        });

        DB = new DBHelper(HomeActivity.this);
        customers = new ArrayList<>();
        takeamt = new ArrayList<>();
        giveamt = new ArrayList<>();
        id = new ArrayList<>();


        storeInArray();
//        System.out.println(customers);

        customersRVAdapter = new CustomersRVAdapter(HomeActivity.this,this, customers,takeamt,giveamt,id);
        recyclerView.setAdapter(customersRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeInArray(){
        Cursor cursor1 = DB.customerreaddata();

        if(cursor1.getCount() == 0){
            Toast.makeText(HomeActivity.this,"No data",Toast.LENGTH_SHORT).show();
        }
        else{
//            cursor1.moveToFirst();
            while (cursor1.moveToNext()) {
                id.add(cursor1.getString(0));
                customers.add(cursor1.getString(1));

                Cursor cursor2 = DB.transactionreaddata(cursor1.getString(0));
                while(cursor2.moveToNext()){
                    giveamt.add(cursor2.getString(0));
                    takeamt.add(cursor2.getString(1));

                }

//                takeamt.add(cursor.getString(4));
//                giveamt.add(cursor.getString(3));

            }
            for(int i=0; i<id.size(); i++){
                System.out.println("cust id: " + id.get(i));
                System.out.println("customer name: " + customers.get(i));

            }

        }

    }

    void showCustomDialogue() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_add);

        custname = dialog.findViewById(R.id.customername);
        custnum = dialog.findViewById(R.id.customernumebr);
        bt = dialog.findViewById(R.id.addbtn);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Objects.requireNonNull(custname.getText()).toString().trim();
                String custnumber = custnum.getText().toString().trim();

                insert = DB.addcustomer(name, custnumber);
                if (insert == true) {
                    Toast.makeText(HomeActivity.this, "Customer added successfully", Toast.LENGTH_SHORT).show();
//                    ha.storeInArray();


                } else {
                    Toast.makeText(HomeActivity.this, "adding failed", Toast.LENGTH_SHORT).show();
                }
//                dialog.dismiss();


            }
        });
        dialog.show();

    }



}

