package com.example.a.ewhat;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;
    //定义按钮
    private RadioButton locationButton;
    private RadioButton weatherButtonn;
    private RadioButton addButton;
    private RadioButton foodButton;
    private RadioButton myButton;
    private String TAG="TEST";

    private List<Food> foodList=new ArrayList<>();


    @SuppressWarnings("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        addButton=(RadioButton)findViewById(R.id.add_tab);
        foodButton=(RadioButton)findViewById(R.id.food_tab);
        myButton= (RadioButton) findViewById(R.id.my_tab);
        initView();
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
                        Intent intentLogin=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intentLogin);
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
        addButton.setOnClickListener(radioButtonListener);
        foodButton.setOnClickListener(radioButtonListener);
        myButton.setOnClickListener(radioButtonListener);

        //打开SQLiteStdio
        SQLiteStudioService.instance().start(this);
        //创建EWhat.db数据库
        dbOpenHelper=new DBOpenHelper(this,"EWhat.db",null,1);
    }

    //初始化界面
    private void initView(){
        RadioButton locationButton=(RadioButton)findViewById(R.id.location_tab);
        RadioButton weatherButton=(RadioButton)findViewById(R.id.weather_tab);
        RadioButton addButton=(RadioButton)findViewById(R.id.add_tab);
        RadioButton foodButton=(RadioButton)findViewById(R.id.food_tab);
        RadioButton myButton=(RadioButton)findViewById(R.id.my_tab);
    }

    private void initFood(){
        for (int i=0;i<3;i++){
            Food food1 =new Food("food1",R.drawable.message);
            foodList.add(food1);
            Food food2 =new Food("food2",R.drawable.message);
            foodList.add(food2);
            Food food3 =new Food("food3",R.drawable.message);
            foodList.add(food3);
            Food food4 =new Food("food4",R.drawable.message);
            foodList.add(food4);
            Food food5 =new Food("food5",R.drawable.message);
            foodList.add(food5);
        }
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
    protected  void onDestroy(){
        //关闭SQLiteStudio
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }
}
