package com.mjiayou.multipushdemo;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by treason on 2016/11/18.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
