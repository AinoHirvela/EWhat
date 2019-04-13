package com.example.a.ewhat;

/**
 * Created by 刘蕊 on 2019/4/13.
 */

public class Shop {
    private String shopName;
    private String shopAddress;

    public Shop(String shopName,String shopAddress){
        this.shopName=shopName;
        this.shopAddress=shopAddress;
    }

    public String getShopAddress(){
        return shopAddress;
    }

    public String getShopName(){
        return shopName;
    }
}
