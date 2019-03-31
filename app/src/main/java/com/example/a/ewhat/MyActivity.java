package com.example.a.ewhat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class MyActivity extends AppCompatActivity {
    //设置显示的list View的信息
    private String[] data={"我的收藏","我吃过的","周报"};
    //判断点击的item
    private void  toNewActivity(int position){
        Intent i;
        switch (position){
            case 0:
                i=new Intent(MyActivity.this,activity_like.class);
                break;
            case 1:
                i=new Intent(MyActivity.this,activity_eat.class);
                break;
            case 2:
                i=new Intent(MyActivity.this,activity_zhou.class);
                break;
            default:
                i=new Intent(MyActivity.this,MyActivity.class);
                break;
        }
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //图片按钮的页面跳转
        ImageButton btn1=(ImageButton)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(MyActivity.this,activity_information.class);
                startActivity(i);
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        //listView的页面跳转
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MyActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toNewActivity(position);
            }
        });
        //button的页面跳转
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(MyActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}