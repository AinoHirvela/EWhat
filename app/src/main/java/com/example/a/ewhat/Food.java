package com.example.a.ewhat;

/**
 * Created by 刘蕊 on 2019/3/27.
 */
//定义一个实体类
public class Food {
    private String foodName;
    //private int imageId;
    private String imageId;

    //构造函数
    /*public Food(String foodName, int imageId){
        this.foodName=foodName;
        this.imageId=imageId;
    }*/
    public Food(String foodName,String imageId){
        this.foodName=foodName;
        this.imageId=imageId;
    }

    public String getFoodName(){
        return foodName;
    }

    /*public int getImageId(){
        return imageId;
    }*/
    public String getImageId(){
        return imageId;
    }
}
