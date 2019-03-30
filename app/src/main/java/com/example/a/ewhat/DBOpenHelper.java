package com.example.a.ewhat;

/**
 * Created by ZhangYuqing on 2019/3/30.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;

public class DBOpenHelper extends SQLiteOpenHelper {
    private Context mContext;
    //用户表创建语句
    public static final String CREATE_USER="create table if not exists User("+
            "Uno character(11) primary key,"+
            "Upwd varchar(20) not null,"+
            "Usex character(2),"+
            "Ubir text,"+
            "Utall integer,"+
            "Uwei real,"+
            "Upic blob)";
    public DBOpenHelper(Context context,String name,CursorFactory factory,int version) {
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    //首次创建数据库时调用
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL执行建表语句
        db.execSQL(CREATE_USER);
        //创建成功显示消息
        Toast.makeText(mContext,"Create User successfully",Toast.LENGTH_SHORT).show();
    }

    @Override
    //当数据库版本发生变化时，自动执行
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVerssion) {

    }
}
