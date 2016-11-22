package com.mjiayou.multipushdemo.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.mjiayou.multipushdemo.util.LogUtil;

/**
 * Created by treason on 2016/11/18.
 */

public class MyHWPushReceiver extends PushReceiver {

    private static final String TAG = "MyHWPushReceiver";

    @Override
    public void onToken(Context context, String token, Bundle extras) {
        String belongId = extras.getString("belongId");
        String content = "get token and belongId successful, token = " + token + ",belongId = " + belongId;
        LogUtil.d(TAG, content);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = "Receive a Push pass-by message： " + new String(msg, "UTF-8");
            LogUtil.d(TAG, content);
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "receive extented notification message: " + extras.getString(BOUND_KEY.pushMsgKey);
            LogUtil.d(TAG, content);
        }
        super.onEvent(context, event, extras);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        try {
            String content = "The current push status： " + (pushState ? "Connected" : "Disconnected");
            LogUtil.d(TAG, content);
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
    }
}
