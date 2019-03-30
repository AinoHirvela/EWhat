package com.example.a.ewhat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 刘蕊 on 2019/3/27.
 */
//定义了一个内部类
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    private List<Food> mFoodList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImage;
        TextView foodName;

        public ViewHolder(android.view.View itemView) {
            super(itemView);
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
        ViewHolder holder=new ViewHolder(view);
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