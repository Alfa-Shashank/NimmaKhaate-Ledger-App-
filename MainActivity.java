package com.example.nimmakhaate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password, confirmpassword;
    Button signup,signin,signin1;
    DBHelper DB;
    boolean insert;
    public int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        signin = findViewById(R.id.signin);
        confirmpassword = findViewById(R.id.confirmpassword);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repassword = confirmpassword.getText().toString();

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repassword))
                    Toast.makeText(MainActivity.this,"All fields are required", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repassword)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser == false) {
                            insert = DB.insertData(user, pass);
                            if (insert == true) {
//                                this is new
//                                Cursor cursor = DB.getuserid(user);
//                                while (cursor.moveToNext()) {
//                                    userid = Integer.parseInt(cursor.getString(0));
//                                }
//                                till here
//                                userid = insert;
                                Toast.makeText(MainActivity.this, "Registered Successfuly", Toast.LENGTH_SHORT).show();
                                System.out.println("registered!!!");
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Registration Unsuccssfull", Toast.LENGTH_SHORT).show();
                                System.out.println("unsucessfull!!");
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else{
                        Toast.makeText(MainActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);


            }
        });





    }


}