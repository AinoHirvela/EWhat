package com.example.a.ewhat;

import android.app.Activity;

import com.baidu.mapapi.map.BaiduMap;

/**
 * Created by 刘蕊 on 2019/4/12.
 */

public class LocationActivity extends Activity implements BaiduMap.OnMyLocationClickListener {
    @Override
    public boolean onMyLocationClick() {
        return false;
    }
}
