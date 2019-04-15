package com.example.a.ewhat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_eat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);
        //1拿到listview的对象
        ListView ls=(ListView)findViewById(R.id.list_view_eaten);
        //2配置数据源
        List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
        Map<String,Object>map=new HashMap<String, Object>();
        map.put("image_eaten",R.drawable.apple);
        map.put("name_eaten","苹果");
        map.put("Calory_eaten","21");
        map.put("times_eaten","吃过6次");
        list.add(map);
        //3设置适配器
        SimpleAdapter adapter=new SimpleAdapter(
                this,
                list,
                R.layout.item_eaten,
                new String[]{"image_eaten","name_eaten","Calory_eaten","times_eaten"},
                new int[]{R.id.image_eaten,R.id.name_eaten,R.id.Calory_eaten,R.id.times_eaten}
        );
        //4关联适配器
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(activity_eat.this,"点击了"+position,Toast.LENGTH_SHORT).show();
            }
        });

    }
}