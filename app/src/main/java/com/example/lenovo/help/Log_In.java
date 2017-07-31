package com.example.lenovo.help;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Log_In extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private EditText phoneNumber;
    private EditText password;
    private TextView sign_up;
    /*private String n="1";
    private String p="2";*/
    Button login_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);
        dbHelper=new MyDatabaseHelper(this,"UserData.db",null,1);
        login_button=(Button)findViewById(R.id.log_in_Button);
        sign_up=(TextView)findViewById(R.id.sign_up_text);
        sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Log_In.this,Sign_up.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                phoneNumber=(EditText)findViewById(R.id.log_in_pho);
                password=(EditText)findViewById(R.id.log_in_pass);
                String sp=phoneNumber.getText().toString();
                String spa=password.getText().toString();

                if(login(sp,spa)){
                    Toast.makeText(Log_In.this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Log_In.this,MainActivity.class);
                    Log.d("Log_In","start main activity");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Log_In.this,"密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean login(String phonenumber,String password){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql="select * from Register where phoneNumber=? and password=?";
        Cursor cursor=db.rawQuery(sql,new String[]{phonenumber,password});
        if (cursor.moveToFirst()) {
           /* SharedPreferences.Editor editor=getSharedPreferences("userInfo",MODE_PRIVATE).edit();
            editor.putString("name",n);
            editor.putString("phonenumber",p);
            editor.apply();*/
          /* NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
           View headerView = navigationView.getHeaderView(0);
            TextView name=(TextView) headerView.findViewById(R.id.username);
            TextView number=(TextView)headerView.findViewById(R.id.telephone);
            name.setText(cursor.getString(cursor.getColumnIndex("userName")));
            number.setText(cursor.getString(cursor.getColumnIndex("telephoneNumber")));
            SharedPreferences.Editor editor = getSharedPreferences("userdata",MODE_APPEND).edit();
            editor.putString("name",cursor.getString(cursor.getColumnIndex("userName")));
            editor.putString("tele",cursor.getString(cursor.getColumnIndex("telephoneNumber")));*/
            cursor.close();
            return true;
        }
        return false;
    }
}
