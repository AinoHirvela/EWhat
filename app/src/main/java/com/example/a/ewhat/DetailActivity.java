package com.example.a.ewhat;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ldoublem.thumbUplib.ThumbUpView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

public class DetailActivity extends AppCompatActivity {
    private ThumbUpView eattimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //ImageButton eattimes=(ImageButton)findViewById(R.id.eattimes);
        //ImageButton collect=(ImageButton)findViewById(R.id.collect);
        ImageView imageView=(ImageView)findViewById(R.id.foodimage) ;

        eattimes = (ThumbUpView) findViewById(R.id.eattimes);
        eattimes.setUnLikeType(ThumbUpView.LikeType.broken);
        eattimes.setCracksColor(Color.WHITE);
        eattimes.setFillColor(Color.rgb(240, 128, 128));
        eattimes.setEdgeColor(Color.rgb(33, 3, 219));

        final SparkButton sparkButton=(SparkButton)findViewById(R.id.collect);
        sparkButton.setChecked(false);
        sparkButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    // 点亮按钮时响应操作
                } else {
                    // 关闭按钮时响应操作
                }
            }
        });

        Intent intent=this.getIntent();
        if (intent!=null){
            int imageid=intent.getExtras().getInt("foodimage");
            imageView.setImageResource(imageid);
        }
        //eattimes.setImageResource(R.drawable.eattimes);
        //collect.setImageResource(R.drawable.collect);
    }

    public void like(View v) {
        eattimes.Like();

    }

    public void unlike(View v) {
        eattimes.UnLike();
    }
}
