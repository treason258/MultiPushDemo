package com.mjiayou.multipushdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.huawei.android.pushagent.PushManager;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by treason on 2016/11/18.
 */

public class MyApplication extends Application {

    public static final String TAG = "MyApplication";

    public static final int PUSH_TYPE_JPUSH = 0;
    public static final int PUSH_TYPE_MIPUSH = 1;
    public static final int PUSH_TYPE_HWPUSH = 2;

    public int mPushType;

    @Override
    public void onCreate() {
        super.onCreate();

        // 选择对应的推送初始化
        mPushType = PUSH_TYPE_MIPUSH;
        initPush(mPushType);
//        initPush(PUSH_TYPE_JPUSH);
//        initPush(PUSH_TYPE_MIPUSH);
//        initPush(PUSH_TYPE_HWPUSH);
    }

    private void initPush(int pushType) {
        switch (pushType) {
            case PUSH_TYPE_JPUSH: {
                // 初始化极光推送
                JPushInterface.setDebugMode(true);
                JPushInterface.init(this);
                break;
            }
            case PUSH_TYPE_MIPUSH: {
                //初始化小米推送
                String APP_ID = "2882303761517526557";
                String APP_KEY = "5371752685557";
                if (shouldInit()) {
                    MiPushClient.registerPush(this, APP_ID, APP_KEY);
                }
                //打开Log
                LoggerInterface newLogger = new LoggerInterface() {
                    @Override
                    public void setTag(String tag) {
                        // ignore
                    }

                    @Override
                    public void log(String content, Throwable t) {
                        Log.d(TAG, content, t);
                    }

                    @Override
                    public void log(String content) {
                        Log.d(TAG, content);
                    }
                };
                Logger.setLogger(this, newLogger);
                break;
            }
            case PUSH_TYPE_HWPUSH: {
                String key = "";
                String value = "";
                PushManager.requestToken(this);
                break;
            }
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
