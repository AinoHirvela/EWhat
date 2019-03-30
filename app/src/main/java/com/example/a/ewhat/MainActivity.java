package com.example.a.ewhat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //打开SQLiteStdio
        SQLiteStudioService.instance().start(this);
        //创建EWhat.db数据库
        dbOpenHelper=new DBOpenHelper(this,"EWhat.db",null,1);
    }

    @Override
    protected  void onDestroy(){
        //关闭SQLiteStudio
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }
}
