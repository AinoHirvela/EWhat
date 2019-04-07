package com.example.a.ewhat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetActivity extends AppCompatActivity {
    private Button button_getkey;
    private Button button_find;
    private ImageButton imageButton_back;
    private EditText editText_key;
    private EditText editText_phone;
    private TimeCountUntil timeCountUntil;
    private EventHandler eventHandler;
    private String phone_number;
    private String key_number;
    private boolean flag=true; //是否已发送了验证码

    //绑定获取界面控件
    public void init(){
        button_getkey=(Button)findViewById(R.id.button_getkey);
        button_find=(Button)findViewById(R.id.button_find);
        imageButton_back=(ImageButton)findViewById(R.id.imageButton_back);
        editText_key=(EditText)findViewById(R.id.editText_key);
        editText_phone=(EditText)findViewById(R.id.editText_phone);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        //初始化界面控件
        init();
        //初始化MobSDK
        MobSDK.init(this);
        //重新发送验证码倒计时
        timeCountUntil=new TimeCountUntil(button_getkey,60000,1000);

        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack=new Intent(ForgetActivity.this,LoginActivity.class);
                startActivity(intentBack);
            }
        });

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };
        //注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);

        //按钮响应时事件
        Button.OnClickListener button=new Button.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                switch (view.getId()) {
                    case R.id.button_getkey:
                        if(isPhone()) {
                            //调用DBOpenHelper
                            DBOpenHelper dbOpenHelper=new DBOpenHelper(ForgetActivity.this,"EWhat.db",null,1);
                            SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
                            //根据输入的账号到数据库内查询
                            Cursor cursor=db.query("User",new String[]{"Uno"},"Uno=?",new String[]{editText_phone.getText().toString()},null,null,null);
                            if(cursor!=null&&cursor.getCount()>=1) {
                                timeCountUntil.start();
                                SMSSDK.getVerificationCode("86",phone_number);
                                editText_key.requestFocus();
                                cursor.close();
                            } else {
                                Toast.makeText(ForgetActivity.this,"该用户未注册，请先注册",Toast.LENGTH_LONG).show();
                                Intent intentLogin=new Intent(ForgetActivity.this,LogupActivity.class);
                                startActivity(intentLogin);
                            }
                        }
                        break;
                    case R.id.button_find:
                        if(isKeyCorrect())
                            SMSSDK.submitVerificationCode("86",phone_number,key_number);
                        flag=false;
                        new AlertDialog.Builder(ForgetActivity.this)
                                .setTitle("系统提示")
                                .setMessage("验证成功！")
                                .setPositiveButton("确定", null)
                                .show();
                        Intent intentSetpwd=new Intent(ForgetActivity.this,SetpwdActivity.class);
                        intentSetpwd.putExtra("PHONE_NUMBER",editText_phone.getText().toString().trim());
                        startActivity(intentSetpwd);
                        break;
                    default:
                        break;
                }
            }
        };
        button_getkey.setOnClickListener(button);
        button_find.setOnClickListener(button);
    }

    //使用Handler来分发Message对象到主线程中，处理事件
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入", Toast.LENGTH_LONG).show();
                        editText_phone.requestFocus();
                        return;
                    }
                }
            }
            if(result==SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_LONG).show();
                }
            } else {
                if(flag) {
                    button_getkey.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败，请重新获取", Toast.LENGTH_LONG).show();
                    editText_phone.requestFocus();
                } else {
                    Toast.makeText(getApplicationContext(),"验证码错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private boolean isPhone() {
        if(TextUtils.isEmpty(editText_phone.getText().toString().trim())) {
            Toast.makeText(ForgetActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            editText_phone.requestFocus();
            return false;
        } else if(editText_phone.getText().toString().trim().length()!=11) {
            Toast.makeText(ForgetActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            editText_phone.requestFocus();
            return false;
        } else {
            phone_number=editText_phone.getText().toString().trim();
            String num="[1][358]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else {
                Toast.makeText(ForgetActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean isKeyCorrect() {
        isPhone();
        if(TextUtils.isEmpty(editText_key.getText().toString().trim())) {
            Toast.makeText(ForgetActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            editText_key.requestFocus();
            return false;
        }
        else if(editText_key.getText().toString().trim().length()!=4) {
            Toast.makeText(ForgetActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            editText_key.requestFocus();
            return false;
        } else {
            key_number=editText_key.getText().toString().trim();
            return true;
        }
    }
}
