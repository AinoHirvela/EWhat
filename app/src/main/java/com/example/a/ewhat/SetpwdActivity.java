package com.example.a.ewhat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SetpwdActivity extends AppCompatActivity {
    private Button button_yes;
    private TextView textView_reset1;
    private TextView textView_reset2;
    private String number;

    public void init() {
        button_yes=(Button)findViewById(R.id.button_yes);
        textView_reset1=(TextView)findViewById(R.id.editText_reset1);
        textView_reset2=(TextView)findViewById(R.id.editText_reset2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpwd);
        //初始化控件
        init();
        Intent intent=getIntent();
        number=intent.getStringExtra("PHONE_NUMBER");

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用DBOpenHelper
                DBOpenHelper dbOpenHelper=new DBOpenHelper(SetpwdActivity.this,"EWhat.db",null,1);
                SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
                //根据输入的账号到数据库内查询
                Cursor cursor=db.query("User",new String[]{"Uno"},"Uno=?",new String[]{number},null,null,null);

            }
        });
    }
}
