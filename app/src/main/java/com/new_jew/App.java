package com.new_jew;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

/**
 * Created by zhangpei on 2016/11/7.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "18cecb3cfa45d", "15594b787f8de14e3017767eba9cdda5");
        //初始化SDK
        x.Ext.init(this);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        SDKInitializer.initialize(getApplicationContext());
        //百度地图


    }


}
