package com.example.a.ewhat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton eattimes=(ImageButton)findViewById(R.id.eattimes);
        ImageButton collect=(ImageButton)findViewById(R.id.collect);
        ImageView imageView=(ImageView)findViewById(R.id.foodimage) ;

        Intent intent=this.getIntent();
        if (intent!=null){
            int imageid=intent.getExtras().getInt("foodimage");
            //int intExtra=Integer.parseInt(stringExtra);
            imageView.setImageResource(imageid);
        }
        eattimes.setImageResource(R.drawable.eattimes);
        collect.setImageResource(R.drawable.collect);
    }
}
