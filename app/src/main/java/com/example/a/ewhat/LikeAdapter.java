package com.example.a.ewhat;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by 史清杰 on 2019/4/14.
 */

public class LikeAdapter extends BaseAdapter {
    List<Map<String,Object>> list;
    LayoutInflater inflater;//设置反射器
    public LikeAdapter(Context context){
        this.inflater=LayoutInflater.from(context);
    }
    public  void setList(List<Map<String,Object>> list){
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view=inflater.inflate(R.layout.item_like,null);//通过反射器引入布局文件
        ImageView image_like=(ImageView)view.findViewById(R.id.image_like);
        TextView name_like=(TextView)view.findViewById(R.id.name_like);
        TextView Calory_like=(TextView)view.findViewById(R.id.Calory_like);
        ImageView type_like=(ImageView)view.findViewById(R.id.type_like);

        Map map=list.get(position);
        image_like.setImageResource((Integer)map.get("image_like"));
        name_like.setText((String)map.get("name_like"));
        Calory_like.setText((String)map.get("Calory_like"));
        type_like.setImageResource((Integer)map.get("type_like"));
        return view;
    }

}
