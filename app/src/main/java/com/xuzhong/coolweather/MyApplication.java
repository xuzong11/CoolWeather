package com.xuzhong.coolweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 获取全局Context类,调用MyApplication.getContext()
 * 在AndroidManifest.xml指定  android:name="com.xuzhong.coolweather.MyApplication"
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }
    public static Context getContext(){
        return context;
    }
}
