package com.example.a.ewhat;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RandomActivity extends AppCompatActivity {

    String randNum;
    String foodName;
    String foodPic;
    int eatTimes;
    private ImageView foodpic;
    private TextView foodname;
    private TextView eattimes;
    //列表的适配器
    private List<Shop> shopList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里应该进行更改
        setContentView(R.layout.activity_detail);

        foodname=(TextView)findViewById(R.id.foodname);
        foodpic=(ImageView)findViewById(R.id.foodimage);
        eattimes=(TextView)findViewById(R.id.times);

        Intent intent=this.getIntent();
        randNum=intent.getExtras().getString("randNum");

        sendFoodRequest();
        //Glide.with(RandomActivity.this).load(foodPic).into(foodpic);
        sendListRequest();
    }

    //请求图片、名称、次数、商家的一些信息
    private void sendFoodRequest(){
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        Request request=new Request.Builder()
                .url(Constant.URL_RandShake+"?fno="+randNum+"&uno=13000000001")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(RandomActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(RandomActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                //返回一个JSON数组
                JSONArray jsonArray=null;
                try {
                    //填写数组
                    jsonArray=new JSONArray(responseData);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=null;
                        //获取第一个数据
                        jsonObject=jsonArray.getJSONObject(i);
                        //接下来为添加内容
                        //times.setText("您已经吃过"+jsonObject.getInt("次数")+"次");
                        foodName=jsonObject.getString("食物名称");
                        foodPic=jsonObject.getString("图片地址");
                        eatTimes=jsonObject.getInt("次数");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(RandomActivity.this).load(foodPic).into(foodpic);
                            }
                        });


                        RandomActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                foodname.setText(foodName);
                                eattimes.setText("您已经吃过"+Integer.toString(eatTimes)+"次");
                                //Glide.with(RandomActivity.this).load(foodPic).into(foodpic);
                            }
                        });
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendListRequest(){
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        Request request=new  Request.Builder()
                .url(Constant.URL_RandList+"?fno="+randNum)
                .build();

        //回复回复界面
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(RandomActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(RandomActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将JSON内容转换为字符串
                String responseData=response.body().string();
                //返回一个JSON数组
                JSONArray jsonArray=null;
                try {
                    //填写数组
                    jsonArray=new JSONArray(responseData);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=null;
                        //获取第一个数据
                        jsonObject=jsonArray.getJSONObject(i);
                        //接下来为添加内容
                        Shop shop=new Shop(jsonObject.getString("店名"),jsonObject.getString("地址"));
                        shopList.add(shop);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public class ShopAdapter extends ArrayAdapter<Shop> {
        private int resourceId;

        public ShopAdapter(Context context, int textViewResourceId, List<Shop> objects) {
            super(context,textViewResourceId, objects);
            resourceId=textViewResourceId;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Shop shop=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            TextView shopAddress=(TextView)view.findViewById(R.id.shop_address);
            shopAddress.setText(shop.getShopAddress());

            return view;
        }
    }
}
