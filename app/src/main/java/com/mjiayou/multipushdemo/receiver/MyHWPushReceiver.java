package com.mjiayou.multipushdemo.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.huawei.android.pushagent.api.PushEventReceiver;
import com.huawei.android.pushagent.api.PushManager;
import com.mjiayou.multipushdemo.util.LogUtil;

/**
 * Created by treason on 2016/11/18.
 */

public class MyHWPushReceiver extends PushEventReceiver {

    private static final String TAG = "MyHWPushReceiver";

    private static final int MAX_RECONNECT = 6;
    private static int mReconnectCount = 0;

    @Override
    public void onToken(Context context, String token, Bundle extras) {
        String belongId = extras.getString("belongId");
        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
        LogUtil.i(TAG, content);

        PushManager.requestPushState(context);
        LogUtil.i(TAG, "PushManager.requestPushState(context);");
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = "收到一条Push消息： " + new String(msg, "UTF-8");
            LogUtil.d(TAG, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);
            LogUtil.d(TAG, content);
        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if (TYPE_TAG == reportType) {
                message = "TAG report result :";
            }
            LogUtil.d(TAG, message + isSuccess);
        }
        super.onEvent(context, event, extras);
    }

    @Override
    public void onPushState(Context context, boolean enable) {
        super.onPushState(context, enable);
        LogUtil.i(TAG, "onPushState -> " + enable);
        if (!enable && mReconnectCount < MAX_RECONNECT) {
            PushManager.requestPushState(context);
            mReconnectCount++;
            LogUtil.i(TAG, "重新检测连接状态次数：" + mReconnectCount);
        }
    }
}
