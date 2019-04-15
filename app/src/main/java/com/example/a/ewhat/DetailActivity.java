package com.example.a.ewhat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

public class DetailActivity extends AppCompatActivity {

    String imageid;
    String name;
    TextView foodName;
    TextView times;

    public int eattimes;

    //列表的适配器
    private List<Shop> shopList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton eatButton=(ImageButton)findViewById(R.id.eattimes);
        ImageButton collectButton=(ImageButton)findViewById(R.id.collect);
        ImageView imageView=(ImageView)findViewById(R.id.foodimage) ;
        foodName=(TextView)findViewById(R.id.foodname);
        times=(TextView)findViewById(R.id.times);

        Intent intent=this.getIntent();
        if (intent!=null){
            imageid=intent.getExtras().getString("foodimage");
            name=intent.getExtras().getString("foodname");
            //int intExtra=Integer.parseInt(stringExtra);
            //imageView.setImageResource(imageid);
            //imageView.setImageURI(Uri.fromFile(new File(imageid)));
            Glide.with(this).load(imageid).into(imageView);
            foodName.setText(name);
        }

        //食用按钮
        eatButton.setImageResource(R.drawable.eattimes);
        eatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eatRequest();
            }
        });
        //收藏按钮
        collectButton.setImageResource(R.drawable.collect);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectRequest();
            }
        });

        //发送请求
        sendMessage();

        initShops();
        ShopAdapter adapter=new ShopAdapter(DetailActivity.this,R.layout.shop_item,shopList);
        ListView listView=(ListView)findViewById(R.id.business);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shop shop=shopList.get(position);
                Toast.makeText(DetailActivity.this,shop.getShopAddress(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    //向服务器发送请求
    private void  sendMessage(){

        //建立一个请连接对象
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        //建立一个请求对象
        Request request=new Request.Builder()
                .url(Constant.URL_GetDetail+"?foodName="+name+"&Uno=13000000001")
                .build();

        //发送请求并获取回复内容
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
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
                        //times.setText("您已经吃过"+jsonObject.getInt("次数")+"次");
                        eattimes=jsonObject.getInt("次数");
                        DetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                times.setText("您已经吃过"+eattimes+"次");
                            }
                        });
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void eatRequest(){
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        Request request=new Request.Builder()
                .url(Constant.URL_EatRequest+"?foodName="+name+"&Uno=13000000001")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //添加一个Toast
                //将JSON内容转换为字符串
                String responseData=response.body().string();
                //返回一个JSON数组
                JSONArray jsonArray=null;
                try {
                    //填写数组
                    jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=null;
                    jsonObject=jsonArray.getJSONObject(0);
                    Toast.makeText(DetailActivity.this,jsonObject.getString("消息"),Toast.LENGTH_SHORT).show();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void collectRequest(){
        OkHttpClient client=new OkHttpClient.Builder()
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .build();

        Request request=new Request.Builder()
                .url(Constant.URL_CollectRequest+"?name="+name)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //添加一个Toast，或者加入动画
                //将JSON内容转换为字符串
                String responseData=response.body().string();
                //返回一个JSON数组
                JSONArray jsonArray=null;
                //填写数组
                try {
                    jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=null;
                    jsonObject=jsonArray.getJSONObject(0);
                    Toast.makeText(DetailActivity.this,jsonObject.getString("消息"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    //这个地方是对列表内容的初始化
    private void initShops(){
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        //完成搭建request
        Request request=new Request.Builder()
                .url(Constant.URL_GetShop+"?foodName="+name)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
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

        public ShopAdapter(Context context, int textViewResourceId, List<Shop>objects) {
            super(context,textViewResourceId, objects);
            resourceId=textViewResourceId;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Shop shop=getItem(position);
            View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            TextView shopAddress=(TextView)view.findViewById(R.id.shop_address);
            shopAddress.setText(shop.getShopName());
            return view;
        }
    }
}
