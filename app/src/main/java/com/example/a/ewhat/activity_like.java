package com.example.a.ewhat;

import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_like extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        //1拿到listview对象
        ListView lv=(ListView)this.findViewById(R.id.list_view_like);
        //2配置数据源
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object>map=new HashMap<String, Object>();
        map.put("image_like",R.drawable.apple);
        map.put("name_like","苹果");
        map.put("Calory_like","21");
        map.put("type_like",R.drawable.like);
        list.add(map);
        //3设置适配器
        LikeAdapter adapter=new LikeAdapter(this);
        adapter.setList(list);
        //4关联适配器
        lv.setAdapter(adapter);
        //5设置活动
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击"+position, Toast.LENGTH_SHORT).show();
        //想要实现的功能：根据position获取id,点击跳转到食物的页面
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //取消收藏：此功能可不实现
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.calendar);
        builder.setTitle("确定要取消收藏吗？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity_like.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
        return true;//消化事件
    }
}
