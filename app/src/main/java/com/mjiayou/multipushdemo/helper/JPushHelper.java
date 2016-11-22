package com.mjiayou.multipushdemo.helper;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by treason on 2016/11/22.
 */

public class JPushHelper {

    public static final String TAG = "JPushHelper";

    /**
     * 极光推送初始化
     */
    public static void init(Context context) {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
    }
}