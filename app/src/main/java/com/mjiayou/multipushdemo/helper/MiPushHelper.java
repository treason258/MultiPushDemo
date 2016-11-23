package com.mjiayou.multipushdemo.helper;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.mjiayou.multipushdemo.util.LogUtil;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by treason on 2016/11/22.
 */

public class MiPushHelper {

    public static final String TAG = "MiPushHelper";

    public static void init(Context context) {
        Configs.PUSH_PLATFORM += " 小米推送";

        //初始化小米推送
        String APP_ID = "2882303761517526557";
        String APP_KEY = "5371752685557";
        if (shouldMiPushInit(context)) {
            MiPushClient.registerPush(context, APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                LogUtil.i(TAG, "setTag");
            }

            @Override
            public void log(String content, Throwable t) {
                LogUtil.e(TAG, "log -> " + content, t);
            }

            @Override
            public void log(String content) {
                LogUtil.d(TAG, "log -> " + content);
            }
        };
        Logger.setLogger(context, newLogger);
    }

    private static boolean shouldMiPushInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
