package com.example.a.ewhat;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 刘蕊 on 2019/3/27.
 */
//定义了一个内部类
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private List<Food> mFoodList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View foodView;
        ImageView foodImage;
        TextView foodName;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
            foodView=itemView;
            foodImage=(ImageView)itemView.findViewById(R.id.food_image);
            foodName=(TextView)itemView.findViewById(R.id.food_name);
        }
    }

    //构造函数
    public FoodAdapter(List<Food> foodList){
        mFoodList=foodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        android.view.View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        //注册事件监听器
        holder.foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Food food=mFoodList.get(position);
                Intent intent=new Intent(v.getContext(),DetailActivity.class);
                intent.putExtra("foodimage",food.getImageId());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
        Food food=mFoodList.get(position);
        holder.foodImage.setImageResource(food.getImageId());
        holder.foodName.setText(food.getFoodName());
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}