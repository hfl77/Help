package com.example.lenovo.help;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Sign_up extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private EditText phoneNumber;
    private EditText userName;
    private EditText password;
    String sph="";
    String su="";
    String sp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dbHelper=new MyDatabaseHelper(this,"UserData.db",null,1);
        Button signupButton=(Button)findViewById(R.id.sign_up_button);
        phoneNumber=(EditText)findViewById(R.id.phoneNumber_Edit);
        userName=(EditText)findViewById(R.id.username_Edit);
        password=(EditText)findViewById(R.id.password_Edit);
        Log.d("Sign_up",sph);
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sph=phoneNumber.getText().toString();
                su=userName.getText().toString();
                sp=password.getText().toString();
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("phoneNumber",sph);
                values.put("userName",su);
                values.put("password",sp);
                db.insert("Register",null,values);
                values.clear();
                db.close();
                Toast.makeText(Sign_up.this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Sign_up.this,Log_In.class);
                startActivity(intent);
            }
        });
    }
}
