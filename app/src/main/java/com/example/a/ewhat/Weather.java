package com.example.a.ewhat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Weather extends AppCompatActivity implements View.OnClickListener {
    private String city_key="101030100";
    private ImageView imageView_update;
    //天气信息
    private TextView cityT,timeT,humidityT,weekT,pmDataT,pmQualityT,temperatureTH,temperatureTL,climateT,windT;
    private ImageView weatherStateImg,pmStateImg;
    private TodayWeather todayWeather=null;

    //启动更新天气情况
    private Handler mHandler=new Handler() {
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case 1:
                    updateTodayWeather((TodayWeather)message.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void init() {
        imageView_update=(ImageView)findViewById(R.id.title_city_update);
        imageView_update.setOnClickListener(this);
        //title
        //cityNameT = (TextView)findViewById(R.id.title_city_name);
        //today weather
        cityT = (TextView)findViewById(R.id.todayinfo1_cityName);
        timeT = (TextView)findViewById(R.id.todayinfo1_updateTime);
        humidityT = (TextView)findViewById(R.id.todayinfo1_humidity);
        weekT = (TextView)findViewById(R.id.todayinfo2_week);
        pmDataT = (TextView)findViewById(R.id.todayinfo1_pm25);
        pmQualityT = (TextView)findViewById(R.id.todayinfo1_pm25status);
        temperatureTH = (TextView)findViewById(R.id.todayinfo2_temperatureHigh);
        temperatureTL=(TextView)findViewById(R.id.todayinfo2_temperatureLow);
        climateT = (TextView)findViewById(R.id.todayinfo2_weatherState);
        windT = (TextView)findViewById(R.id.todayinfo2_wind);

        weatherStateImg = (ImageView)findViewById(R.id.todayinfo2_weatherStatusImg);
        pmStateImg = (ImageView)findViewById(R.id.todayinfo1_pm25img);

        //cityNameT.setText("NULL");
        cityT.setText("NULL");
        timeT.setText("NULL");
        humidityT.setText("NULL");
        weekT.setText("NULL");
        //pmDataT.setText("NULL");
        pmQualityT.setText("NULL");
        temperatureTH.setText("NULL");
        temperatureTL.setText("NULL");
        climateT.setText("NULL");
        windT.setText("NULL");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        init();

        //检查网络连接状态
        if(CheckNet.getNetState(this)==CheckNet.NET_NONE) {
            Log.d("WEATHER","网络不通");
            Toast.makeText(Weather.this,"网络不通",Toast.LENGTH_SHORT).show();
        } else {
            Log.d("WEATHER","网络已连通");
            Toast.makeText(Weather.this,"网络已连通",Toast.LENGTH_SHORT).show();
            getWeatherDatafromNet(city_key);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.title_city_update) {
            Toast.makeText(Weather.this,"刷新数据",Toast.LENGTH_SHORT).show();
            getWeatherDatafromNet(city_key);
        }
        if(view.getId()==R.id.title_city_share) {
            Intent intent=new Intent(Weather.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void getWeatherDatafromNet(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("Address:",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while((str=reader.readLine())!=null)
                    {
                        sb.append(str);
                        Log.d("date from url",str);
                    }
                    String response = sb.toString();
                    Log.d("response",response);
                    todayWeather=parseXML(response);
                    if(todayWeather!=null) {
                        Message message=new Message();
                        message.what=1;
                        message.obj=todayWeather;
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private TodayWeather parseXML(String xmlData) {
        TodayWeather todayWeather = null;

        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            Log.d("MWeater","start parse xml");

            while(eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse","start doc");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp"))
                        {
                            todayWeather = new TodayWeather();
                        }
                        if(todayWeather!=null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                Log.d("city", xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                Log.d("updatetime", xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                Log.d("wendu", xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengli", xmlPullParser.getText());
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                Log.d("shidu", xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("fengxiang", xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                Log.d("pm25", xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                Log.d("quelity", xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("date", xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("high", xmlPullParser.getText());
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("low", xmlPullParser.getText());
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                Log.d("type", xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType=xmlPullParser.next();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return todayWeather;
    }

    private void updateTodayWeather(TodayWeather todayWeather) {
        //cityNameT.setText(todayWeather.getCity()+"天气");
        cityT.setText(todayWeather.getCity());
        timeT.setText("今天"+todayWeather.getUpdatetime()+"发布");
        humidityT.setText("湿度: "+todayWeather.getShidu());
        pmDataT.setText(todayWeather.getPm25());
        pmQualityT.setText(todayWeather.getQuality());
        Calendar calendar=Calendar.getInstance();
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1)+"月";
        weekT.setText(month+todayWeather.getDate());
        temperatureTH.setText(todayWeather.getHigh().substring(2,todayWeather.getHigh().length()));
        temperatureTL.setText(todayWeather.getLow().substring(2,todayWeather.getLow().length()));
        climateT.setText(todayWeather.getType());
        windT.setText("风力: "+todayWeather.getFengli());


        if(todayWeather.getType()!=null) {
            switch(todayWeather.getType()) {
                case "晴":
                    weatherStateImg.setImageResource(R.mipmap.ic_sunny_big);
                    break;
                case "阴":
                    weatherStateImg.setImageResource(R.mipmap.ic_cloudy_big);
                    break;
                case "多云":
                    weatherStateImg.setImageResource(R.mipmap.ic_overcast_big);
                    break;
                case "小雨":
                    weatherStateImg.setImageResource(R.mipmap.ic_lightrain_big);
                    break;
                case "中雨":
                case "大雨":
                    weatherStateImg.setImageResource(R.mipmap.ic_heavyrain_big);
                    break;
                case "阵雨":
                    weatherStateImg.setImageResource(R.mipmap.ic_shower_big);
                    break;
                case "雷阵雨":
                    weatherStateImg.setImageResource(R.mipmap.ic_thundeshowehail_big);
                    break;
                case "阵雪":
                case "暴雪":
                case "小雪":
                case "中雪":
                case "大雪":
                    weatherStateImg.setImageResource(R.mipmap.ic_snow_big);
                    break;
                case "雨夹雪":
                    weatherStateImg.setImageResource(R.mipmap.ic_rainsnow_big);
                    break;
                default:
                    break;
            }
            Toast.makeText(Weather.this,"更新成功",Toast.LENGTH_SHORT).show();
        }
    }
}
