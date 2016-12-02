package com.mjiayou.multipushdemo;

import android.app.Application;

import com.mjiayou.multipushdemo.common.Configs;
import com.mjiayou.multipushdemo.helper.JPushHelper;
import com.mjiayou.multipushdemo.helper.MiPushHelper;
import com.mjiayou.multipushdemo.util.RomUtil;

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

//        // TODO 全部打开测试
//        Configs.PUSH_RULE += "测试包，三个推送平台全部打开";
//        JPushHelper.init(getApplicationContext());
//        MiPushHelper.init(getApplicationContext());

        Configs.PUSH_RULE += "根据系统开启推送平台：如果是小米系统，则走小米推送；如果不是，则走极光推送";
        if (RomUtil.isMIUI()) { // 如果是小米系统，则走小米推送
            MiPushHelper.init(getApplicationContext());
        } else { // 如果不是，则走极光推送
            JPushHelper.init(getApplicationContext());
        }
    }
}
