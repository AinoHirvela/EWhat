package com.example.a.ewhat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyActivity extends AppCompatActivity {
    private String[] data={"我的收藏","我吃过的","周报"};
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MyActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toNewActivity(position);
            }
        });
    }
}