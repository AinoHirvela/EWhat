package com.example.a.ewhat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.os.Handler;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MyActivity extends AppCompatActivity {
    private DBOpenHelper dbOpenHelper;
    ImageView image;
    ImageView mountain;
    TextView text;
    TextView percent;
    //设置显示的list View的信息
    //private String[] data={"我的收藏","我吃过的","周报"};
    //判断点击的item
    private void  toNewActivity(int position){
        Intent i;
        switch (position){
            case 0:
                i=new Intent(MyActivity.this,activity_like.class);
                break;
            case 1:
                i=new Intent(MyActivity.this,activity_eat.class);
                break;
            case 2:
                i=new Intent(MyActivity.this,activity_zhou.class);
                break;
            default:
                i=new Intent(MyActivity.this,MyActivity.class);
                break;
        }
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        //打开SQLiteStdio
        SQLiteStudioService.instance().start(this);
        //创建EWhat.db数据库
        dbOpenHelper=new DBOpenHelper(this,"EWhat.db",null,1);
        //图片按钮的页面跳转
        ImageButton btn1=(ImageButton)findViewById(R.id.picture);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(MyActivity.this,activity_information.class);
                startActivity(i);
            }
        });

        //listView的页面跳转
       //1拿到listview的对象

        ListView listView=(ListView)findViewById(R.id.list_view);
        //2数据源
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("lv_pic",R.drawable.like);
        map.put("lv_name","我的收藏");
        list.add(map);
        map=new HashMap<String, Object>();
        map.put("lv_pic",R.drawable.eaten);
        map.put("lv_name","我吃过的");
        list.add(map);
        map=new HashMap<String, Object>();
        map.put("lv_pic",R.drawable.calendar);
        map.put("lv_name","周报");
        list.add(map);
        //3设置适配器
        SimpleAdapter adapter=new SimpleAdapter(
                this,list,R.layout.item_my,new String[]{"lv_pic","lv_name"},new int[]{R.id.lv_pic,R.id.lv_name}
        );
        //4关联适配器
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toNewActivity(position);
            }
        });
        //button的页面跳转
        Button button=(Button)findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dbOpenHelper.getWritableDatabase();
                Intent i=new Intent(MyActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });



        image = (ImageView) findViewById(R.id.image);
        mountain = (ImageView) findViewById(R.id.mountain);
        text = (TextView) findViewById(R.id.text);
        percent = (TextView) findViewById(R.id.percent);

        findViewById(R.id.parallel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateParallel();
            }
        });

        findViewById(R.id.mountain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleAnimation();
            }
        });

        findViewById(R.id.sequentially).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateSequentially();
            }
        });
    }

    protected void simpleAnimation() {
        com.example.a.ewhat.ViewAnimator.animate(mountain)
                .translationY(-1000, 0)
                .alpha(0, 1)
                .andAnimate(text)
                .translationX(-200, 0)
                .interpolator(new DecelerateInterpolator())
                .duration(2000)

                .thenAnimate(mountain)
                .scale(1f, 0.5f, 1f)
                .interpolator(new AccelerateInterpolator())
                .duration(1000)

                .start();
    }

    protected void animateParallel() {
        final ViewAnimator viewAnimator = com.example.a.ewhat.ViewAnimator.animate(mountain, image)
                .dp().translationY(-1000, 0)
                .alpha(0, 1)
                .singleInterpolator(new OvershootInterpolator())

                .andAnimate(percent)
                .scale(0, 1)

                .andAnimate(text)
                .textColor(Color.BLACK, Color.WHITE)
                .backgroundColor(Color.WHITE, Color.BLACK)

                .waitForHeight()
                .singleInterpolator(new AccelerateDecelerateInterpolator())
                .duration(2000)

                .thenAnimate(percent)
                .custom(new AnimationListener.Update<TextView>() {
                    @Override
                    public void update(TextView view, float value) {
                        view.setText(String.format(Locale.US, "%.02f%%", value));
                    }
                }, 0, 1)

                .andAnimate(image)
                .rotation(0, 360)

                .duration(5000)

                .start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewAnimator.cancel();
                Toast.makeText(getBaseContext(), "animator canceled", Toast.LENGTH_SHORT).show();
            }
        }, 4000);
    }

    protected void animateSequentially() {
        com.example.a.ewhat.ViewAnimator.animate(image)
                .dp().width(100f, 150f)
                .alpha(1, 0.1f)
                .interpolator(new DecelerateInterpolator())
                .duration(800)
                .thenAnimate(image)
                .dp().width(150f, 100f)
                .alpha(0.1f, 1f)
                .interpolator(new AccelerateInterpolator())
                .duration(1200)
                .start();
    }
}
