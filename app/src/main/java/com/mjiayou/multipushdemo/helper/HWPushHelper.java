package com.mjiayou.multipushdemo.helper;

import android.content.Context;

import com.huawei.android.pushagent.PushManager;
import com.mjiayou.multipushdemo.common.Configs;
import com.mjiayou.multipushdemo.util.LogUtil;

/**
 * Created by treason on 2016/11/22.
 */

public class HWPushHelper {

    public static final String TAG = "HWPushHelper";

    public static void init(Context context) {
        Configs.PUSH_PLATFORM += " 华为推送";

        PushManager.requestToken(context);
        LogUtil.i(TAG, "PushManager.requestToken(context);");
    }
}
