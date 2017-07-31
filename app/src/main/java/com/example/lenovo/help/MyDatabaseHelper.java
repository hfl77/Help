package com.example.lenovo.help;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2017/6/10.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_REGISTER="create table Register("
            +"id integer primary key autoincrement, "
            +"phoneNumber text, "
            +"userName text, "
            +"password text)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_REGISTER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
