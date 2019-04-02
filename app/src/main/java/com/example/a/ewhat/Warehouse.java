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

/**
 * Created by Administrator on 2019/4/1.
 */

public class Warehouse extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;
    //定义按钮
    private RadioButton MaterialButton;
    private RadioButton FoodMaterialButton;
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
        setSupportActionBar(toolbar);
        //实例化Button
        MaterialButton=(RadioButton)findViewById(R.id.material_tab);
        FoodMaterialButton=(RadioButton)findViewById(R.id.foodmaterial_tab);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);

        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        FoodAdapter adapter=new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);
        RadioButton.OnClickListener radioButtonListener=new RadioButton.OnClickListener(){
            //FragmentTransaction transaction=fragmentManager.beginTransaction();
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.material_tab:
                        Intent intentLogin=new Intent(Warehouse.this,LoginActivity.class);
                        startActivity(intentLogin);
                        break;
                    case R.id.foodmaterial_tab:
                        Intent intent1=new Intent(Warehouse.this,MyActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        };
        MaterialButton.setOnClickListener(radioButtonListener);
        FoodMaterialButton.setOnClickListener(radioButtonListener);
        SQLiteStudioService.instance().start(this);
        //创建EWhat.db数据库
        dbOpenHelper=new DBOpenHelper(this,"EWhat.db",null,1);
    }
    //初始化界面
    private void initView(){
        RadioButton MaterialButton=(RadioButton)findViewById(R.id.material_tab);
        RadioButton FoodMaterialButton=(RadioButton)findViewById(R.id.foodmaterial_tab);
    }
    @Override
    protected  void onDestroy(){
        //关闭SQLiteStudio
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }
}
