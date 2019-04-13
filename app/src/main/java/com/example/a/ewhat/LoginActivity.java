package com.example.a.ewhat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView textView_name;
    private TextView textView_pwd;
    private Button button_login;
    private Button button_logup;
    private Button button_forget;
    private ImageButton imageButton_back;
    private ImageView unameClear;
    private ImageView pwdClear;

    //获取界面控件
    public void init() {
        textView_name=(TextView)findViewById(R.id.editText_name);
        textView_pwd=(TextView)findViewById(R.id.editText_pwd);
        button_login=(Button)findViewById(R.id.button_comfirm);
        button_logup=(Button)findViewById(R.id.button_logup);
        button_forget=(Button)findViewById(R.id.button_forget);
        imageButton_back=(ImageButton)findViewById(R.id.imageButton_back);
        unameClear = (ImageView) findViewById(R.id.iv_unameClear);
        pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);

        EditTextClearTools.addClearListener(textView_name,unameClear);
        EditTextClearTools.addClearListener(textView_pwd,pwdClear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        button_logup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogup=new Intent(LoginActivity.this,LogupActivity.class);
                startActivity(intentLogup);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //调用DBOpenHelper访问数据库
                DBOpenHelper dbOpenHelper=new DBOpenHelper(LoginActivity.this,"EWhat.db",null,1);
                SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
                if(textView_name.getText().toString().equals("")){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("系统提示")
                            .setMessage("用户名不能为空！")
                            .setPositiveButton("确定", null)
                            .show();
                } else if (textView_pwd.getText().toString().equals("")){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("系统提示")
                            .setMessage("密码不能为空！")
                            .setPositiveButton("确定", null)
                            .show();
                } else {
                    //获得用户输入用户名和密码
                    String[] textInfo={textView_name.getText().toString(),textView_pwd.getText().toString()};
                    Cursor cursor1=db.query("User",null,"Uno=? and Upwd=?",textInfo,null,null,null);
                    if(cursor1!=null&&cursor1.getCount()>=1) {
                        //Toast.makeText(LoginActivity.this,"Login successfully",Toast.LENGTH_SHORT).show();
                        Intent intentBack=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intentBack);
                    } else {
                        //Toast.makeText(LoginActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        //根据用户名查询数据库判断该用户是否已经注册
                        Cursor cursor2=db.query("User",new String[]{"Uno"},"Uno=?",new String[]{textView_name.getText().toString()},null,null,null);
                        if(cursor2!=null&&cursor2.getCount()>=1){
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("系统提示")
                                    .setMessage("密码错误，请检查后重新输入！")
                                    .setPositiveButton("确定", null)
                                    .show();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("系统提示")
                                    .setMessage("账号不存在，请先注册账号！")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialogInterface,int i){
                                            Intent intentLogup=new Intent(LoginActivity.this,LogupActivity.class);
                                            startActivity(intentLogup);
                                        }
                                    })
                                    .show();
                        }
                        cursor2.close();
                    }
                    cursor1.close();
                    db.close();
                }

            }
        });

        button_forget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentForget=new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intentForget);
            }
        });

        imageButton_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentBack=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intentBack);
            }
        });
    }
}