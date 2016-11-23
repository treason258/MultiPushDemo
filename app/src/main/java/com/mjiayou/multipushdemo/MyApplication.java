package com.mjiayou.multipushdemo;

import android.app.Application;

import com.mjiayou.multipushdemo.helper.JPushHelper;
import com.mjiayou.multipushdemo.helper.MiPushHelper;

/**
 * Created by treason on 2016/11/18.
 */

public class MyApplication extends Application {

    public static final String TAG = "MyApplication";

    private static MyApplication mInstance;

    public static MyApplication get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // TODO 全部打开测试
        JPushHelper.init(getApplicationContext());
        MiPushHelper.init(getApplicationContext());
//        HWPushHelper.init(getApplicationContext());

//        // 根据系统开启推送平台
//        if (AppUtil.ROM.isEMUI()) { // 如果是华为系统，则使用华为推送
//            HWPushHelper.init(getApplicationContext());
//        } else { // 如果不是，则使用小米推送
//            MiPushHelper.init(getApplicationContext());
//        }
    }
}
