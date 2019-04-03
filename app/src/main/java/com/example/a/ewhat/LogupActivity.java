package com.example.a.ewhat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.mob.MobSDK;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LogupActivity extends AppCompatActivity {
    private TextView textView_phone;
    private TextView textView_pwd1;
    private TextView textView_pwd2;
    private TextView textView_key;
    private Button button_getkey;
    private Button button_comfirm;
    private ImageButton imageButton_back;
    private EventHandler eventHandler;
    private boolean flag=true; //是否已发送了验证码
    private boolean keyFlag=false; //验证码是否正确
    private TimeCountUntil timeCountUntil;
    private String phone_number;
    private String key_number;
    private String password;
    private String TAG="TEST";

    //绑定获取界面控件
    public void init(){
        textView_phone=(TextView)findViewById(R.id.editText_phone);
        textView_pwd1=(TextView)findViewById(R.id.editText_pwd1);
        textView_pwd2=(TextView)findViewById(R.id.editText_pwd2);
        textView_key=(TextView)findViewById(R.id.editText_key);
        button_getkey=(Button)findViewById(R.id.button_getkey);
        button_comfirm=(Button)findViewById(R.id.button_comfirm);
        imageButton_back=(ImageButton)findViewById(R.id.imageButton_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);
        //初始化界面控件
        init();
        //初始化MobSDK
        MobSDK.init(this);
        //重新发送验证码倒计时
        timeCountUntil=new TimeCountUntil(button_getkey,60000,1000);
        //连接数据库
        DBOpenHelper dbOpenHelper=new DBOpenHelper(LogupActivity.this,"EWhat.db",null,1);
        dbOpenHelper.getWritableDatabase();
        Log.i(TAG,"get database successfully");

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
                    case  R.id.imageButton_back:
                        Intent intentBack=new Intent(LogupActivity.this,LoginActivity.class);
                        startActivity(intentBack);
                        break;
                    case R.id.button_getkey:
                        if(isPhone()) {
                            timeCountUntil.start();
                            SMSSDK.getVerificationCode("86",phone_number);
                            textView_key.requestFocus();
                        }
                        break;
                    case R.id.button_comfirm:
                        if(isKeyCorrect())
                            SMSSDK.submitVerificationCode("86",phone_number,key_number);
                        flag=false;
                        //Log.i(TAG,"get key successfully 1 "+keyFlag);
                        if(setPassword()) {
                            //调用DBOpenHelper
                            DBOpenHelper dbOpenHelper=new DBOpenHelper(LogupActivity.this,"EWhat.db",null,1);
                            SQLiteDatabase db=dbOpenHelper.getWritableDatabase();

                            //根据输入的账号到数据库内查询
                            Cursor cursor=db.query("User",new String[]{"Uno"},"Uno=?",new String[]{textView_phone.getText().toString()},null,null,null);
                            Log.i(TAG,"enter successfully1");
                            if(cursor!=null&&cursor.getCount()>=1) {
                                Toast.makeText(LogupActivity.this,"该用户已存在",Toast.LENGTH_LONG).show();
                                cursor.close();
                            } else {
                                Log.i(TAG,"enter successfully2");
                                password=textView_pwd1.getText().toString().trim();
                                phone_number=textView_phone.getText().toString().trim();
                                ContentValues values=new ContentValues();
                                values.put("Uno",phone_number);Log.i(TAG,"phone_number="+phone_number);
                                values.put("Upwd",password);Log.i(TAG,"password="+password);
                                long rowid=db.insert("User",null,values);Log.i(TAG,"rowid="+rowid);
                                if(rowid!=-1) {
                                    Toast.makeText(LogupActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LogupActivity.this,"注册失败，请稍后重试",Toast.LENGTH_LONG).show();
                                }
                            }
                            db.close();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        button_getkey.setOnClickListener(button);
        button_comfirm.setOnClickListener(button);
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
                        textView_phone.requestFocus();
                        return;
                    }
                }
            }
            if(result==SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    keyFlag=true;
                    Toast.makeText(getApplicationContext(), "验证码正确", Toast.LENGTH_LONG).show();
                    Log.i(TAG,"get key successfully 2 "+keyFlag);
                }
            } else {
                if(flag) {
                    button_getkey.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败，请重新获取", Toast.LENGTH_LONG).show();
                    textView_phone.requestFocus();
                } else {
                    keyFlag=false;
                    Toast.makeText(getApplicationContext(),"验证码错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private boolean isPhone() {
        if(TextUtils.isEmpty(textView_phone.getText().toString().trim())) {
            Toast.makeText(LogupActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            textView_phone.requestFocus();
            return false;
        } else if(textView_phone.getText().toString().trim().length()!=11) {
            Toast.makeText(LogupActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            textView_phone.requestFocus();
            return false;
        } else {
            phone_number=textView_phone.getText().toString().trim();
            String num="[1][358]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else {
                Toast.makeText(LogupActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean isKeyCorrect() {
        isPhone();
        if(TextUtils.isEmpty(textView_key.getText().toString().trim())) {
            Toast.makeText(LogupActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            textView_key.requestFocus();
            return false;
        }
        else if(textView_key.getText().toString().trim().length()!=4) {
            Toast.makeText(LogupActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            textView_key.requestFocus();
            return false;
        } else {
            key_number=textView_key.getText().toString().trim();
            return true;
        }
    }

    private boolean setPassword() {
        if(textView_pwd1.getText().toString().trim().length()<8||textView_pwd1.getText().toString().trim().length()>20) {
            Toast.makeText(LogupActivity.this,"密码长度不合法",Toast.LENGTH_SHORT).show();
            return false;
        } else if(textView_pwd1.getText().toString().equals(textView_pwd2.getText().toString())) {
            password=textView_pwd1.getText().toString().trim();
            return true;
        } else {
            Toast.makeText(LogupActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
