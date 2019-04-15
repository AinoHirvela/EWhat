package com.example.a.ewhat;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Color;
import android.widget.ImageView;

import com.ldoublem.thumbUplib.ThumbUpView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;
import com.varunest.sparkbutton.SparkEventListener;

import static interfaces.heweather.com.interfacesmodule.view.HeContext.context;

public class WeeklyActivity extends AppCompatActivity {
    ThumbUpView tpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);

        final SparkButton sparkButton=(SparkButton)findViewById(R.id.heart_button);
        sparkButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    sparkButton.setChecked(true);
                } else {
                    sparkButton.setChecked(false);
                }
            }
        });

        /*tpv = (ThumbUpView) findViewById(R.id.tpv);
        tpv.setUnLikeType(ThumbUpView.LikeType.broken);
        tpv.setCracksColor(Color.WHITE);
        tpv.setFillColor(Color.rgb(240, 128, 128));
        tpv.setEdgeColor(Color.rgb(33, 3, 219));
        tpv.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {

            }
        });*/
    }

    /*public void like(View v) {
        tpv.Like();

    }

    public void unlike(View v) {
        tpv.UnLike();
    }*/
}
