package com.example.a.ewhat;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import interfaces.heweather.com.interfacesmodule.bean.weather.*;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //添加加速度传感器
    private SensorManager mSensorManager;
    private Vibrator vibrator;

    private DBOpenHelper dbOpenHelper;
    //定义按钮
    private RadioButton locationButton;
    private RadioButton weatherButtonn;
    //private RadioButton addButton;
    private RadioButton foodButton;
    private RadioButton myButton;
    private TextView nowlocation;
    String street;

    private String TAG="TEST";

    private LocationClient mLocationClient=null;

    private List<Food> foodList=new ArrayList<>();


    @SuppressWarnings("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //打开SQLiteStdio
        SQLiteStudioService.instance().start(this);
        //创建EWhat.db数据库
        dbOpenHelper=new DBOpenHelper(this,"EWhat.db",null,1);
        //连接和风天气
        HeConfig.init("HE1903191601381347","3c2f0d928abd44daa4acb19b57528ae6");
        //隐藏原标题
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //定义ToolBar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        //设置导航栏图标
        //toolbar.setNavigationIcon();
        //设置标题
        //toolbar.setTitle("吃What");
        //toolbar.setSubtitle("定制你每一天的饮食");

        //设置标题颜色
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        //toolbar.setSubtitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        //实例化Button
        weatherButtonn=(RadioButton)findViewById(R.id.weather_tab);
        locationButton=(RadioButton)findViewById(R.id.location_tab);
        //addButton=(RadioButton)findViewById(R.id.add_tab);
        foodButton=(RadioButton)findViewById(R.id.food_tab);
        myButton= (RadioButton) findViewById(R.id.my_tab);
        nowlocation=(TextView)findViewById(R.id.nowlocation);

        //内部类对象
        MyLocationListener myLocationListener=new MyLocationListener();
        //实例化
        mLocationClient=new LocationClient(this);
        LocationClientOption option=new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setAddrType("all");
        option.setCoorType("db0911");
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();

        //获得传感器管理服务
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        //initView();
        initFood();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        FoodAdapter adapter=new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        //设置监听器
        RadioButton.OnClickListener radioButtonListener=new RadioButton.OnClickListener(){
            //FragmentTransaction transaction=fragmentManager.beginTransaction();
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.location_tab:
                        break;
                    //天气、加号、食品库
                    //登录注册界面暂时写在天气下面
                    case R.id.weather_tab:
                        /*dbOpenHelper.getWritableDatabase();
                        Intent intentLogin=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intentLogin);*/
                        Intent intent=new Intent(MainActivity.this, LoadingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.my_tab:
                        Intent intent1=new Intent(MainActivity.this,MyActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        };
        locationButton.setOnClickListener(radioButtonListener);
        weatherButtonn.setOnClickListener(radioButtonListener);
        //addButton.setOnClickListener(radioButtonListener);
        foodButton.setOnClickListener(radioButtonListener);
        myButton.setOnClickListener(radioButtonListener);
    }


    //初始化界面
    private void initView(){
        RadioButton locationButton=(RadioButton)findViewById(R.id.location_tab);
        RadioButton weatherButton=(RadioButton)findViewById(R.id.weather_tab);
        //RadioButton addButton=(RadioButton)findViewById(R.id.add_tab);
        RadioButton foodButton=(RadioButton)findViewById(R.id.food_tab);
        RadioButton myButton=(RadioButton)findViewById(R.id.my_tab);
    }

    private void initFood(){
        /*for (int i=0;i<3;i++){
            Food food1 =new Food("food1",R.drawable.message);
            foodList.add(food1);
            Food food2 =new Food("food2",R.drawable.message);
            foodList.add(food2);
            //Food food3 =new Food("food3",R.drawable.message);
            //foodList.add(food3);
            Food food4 =new Food("food4",R.drawable.message);
            foodList.add(food4);
            Food food5 =new Food("food5",R.drawable.message);
            foodList.add(food5);
        }*/
        //用于获取response

        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();

        //完成搭建request
        Request request=new Request.Builder()
                .url(Constant.URL_GetFood)
                .build();

        client.newCall(request).enqueue(new Callback() {
            //判断是否出错
            @Override
            public void onFailure(Call call, IOException e) {

                if (e instanceof SocketTimeoutException){
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"连接超时，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException){
                    //Log.i("TAG","连接失败");
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"连接错误，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            //这是最重要的回复数据的内容
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
                        Food food=new Food(jsonObject.getString("名称"),jsonObject.getString("图片"));
                        foodList.add(food);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    //获取菜单填充器，将自定义的菜单文件进行填充
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_tool_bar,menu);

        //找到SEARCHVIEW
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItem);
        // 当展开无输入内容的时候，没有关闭的图标
        searchView.onActionViewExpanded();

        //显示提交按钮
        searchView.setSubmitButtonEnabled(true);
        //searchView.setQueryHint("查找自己喜欢的");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG,"内容"+newText);
                return true;
            }
        });
        return true;
    }

    //为菜单选项设置监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_locate:
                Intent intent=new Intent(MainActivity.this,BaiDuMapTest.class);
                startActivity(intent);
                //Intent intent=new Intent(MainActivity.this,MyActivity.class);
                //startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        //加速度传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected  void onDestroy(){
        //关闭SQLiteStudio
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType=event.sensor.getType();
        float[] values=event.values;
        if (sensorType==Sensor.TYPE_ACCELEROMETER){
            if (Math.abs(values[0])>14||Math.abs(values[1])>14||Math.abs(values[2])>14){
                Intent intent=new Intent(MainActivity.this,RandomActivity.class);
                //产生一个随机数，并转化为字符串类型
                Random random=new Random();
                //真正用，不能以1开头
                //int randNum=random.nextInt(20)+100000;
                int randNum=123456;
                //测试用
                //int randNum=random.nextInt(1)+123456;
                intent.putExtra("randNum",Integer.toString(randNum));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //定义内部类
    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            try {
                street=location.getAddrStr();
                Toast.makeText(getApplicationContext(),street,Toast.LENGTH_LONG).show();
                nowlocation.setText("目前位置："+street);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

        private List<Food> mFoodList;

        class ViewHolder extends RecyclerView.ViewHolder{
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
        public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            android.view.View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
            final FoodAdapter.ViewHolder holder=new FoodAdapter.ViewHolder(view);

            //注册事件监听器
            holder.foodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*int position=holder.getAdapterPosition();
                Food food=mFoodList.get(position);
                Toast.makeText(v.getContext(),"you click image"+food.getFoodName(),Toast.LENGTH_SHORT).show();*/
                    int position=holder.getAdapterPosition();
                    Food food=mFoodList.get(position);
                    Intent intent=new Intent(v.getContext(),DetailActivity.class);
                    intent.putExtra("foodimage",food.getImageId());
                    intent.putExtra("foodname",food.getFoodName());
                    v.getContext().startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
            Food food=mFoodList.get(position);
            //holder.foodImage.setImageResource(food.getImageId());
            //holder.foodImage.setImageURI(Uri.fromFile(new File(food.getImageId())));
            Glide.with(MainActivity.this).load(food.getImageId()).into(holder.foodImage);
            holder.foodName.setText(food.getFoodName());
        }

        @Override
        public int getItemCount() {
            return mFoodList.size();
        }
    }
}
