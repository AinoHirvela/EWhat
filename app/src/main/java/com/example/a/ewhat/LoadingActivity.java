package com.example.a.ewhat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Thread myThread=new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent mainIntent=new Intent(getApplicationContext(),Weather.class);
                    startActivity(mainIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
