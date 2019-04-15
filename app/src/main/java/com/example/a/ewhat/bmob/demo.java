package com.example.a.ewhat.bmob;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.a.ewhat.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 刘蕊 on 2019/4/2.
 */

public class demo extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        Bmob.initialize(this, "e4df9d4aa831ed0a9e6dc2efe79c8d1");
        BmobQuery<User> query=new BmobQuery<User>();
        //查找Person表里面id为6b6c11c537的数据
        query.getObject("5CuE0004", new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {

                if(e==null){
                  Toast.makeText(demo.this,"success",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(demo.this,"FAILL",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> list, BmobException e) {
//                if(e==null){
//                    Toast.makeText(demo.this,"success",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(demo.this,"fail",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

}
