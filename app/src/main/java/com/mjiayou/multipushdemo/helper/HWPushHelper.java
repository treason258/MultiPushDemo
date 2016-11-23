package com.mjiayou.multipushdemo.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.mjiayou.multipushdemo.util.LogUtil;

/**
 * Created by treason on 2016/11/22.
 */

public class HWPushHelper {

    public static final String TAG = "HWPushHelper";

    private static HuaweiApiClient mHuaweiApiClient = null;

    private boolean mResolvingError = false;

    public static void init(Context context) {
        Configs.PUSH_PLATFORM += " 华为推送";

        mHuaweiApiClient = new HuaweiApiClient.Builder(context)
                .addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected() {
                        LogUtil.i(TAG, "HuaweiApiClient.addConnectionCallbacks | onConnected");
                        getState();
                        getToken();
                        setReceiveEnable();
//                        setTag();
//                        getTag();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        LogUtil.i(TAG, "HuaweiApiClient.addConnectionCallbacks | onConnectionSuspended -> " + i);
                    }
                })
                .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        LogUtil.i(TAG, "HuaweiApiClient.addOnConnectionFailedListener | onConnectionFailed -> " + connectionResult.getErrorCode());
                    }
                })
                .build();
        mHuaweiApiClient.connect();
    }

    /**
     * 判断HMS是否连接
     */
    public static boolean isConnected() {
        if (mHuaweiApiClient != null && mHuaweiApiClient.isConnected()) {
            return true;
        } else {
            LogUtil.i(TAG, "HMS is disconnect.");
            return false;
        }
    }

    /**
     * 获取Token
     */
    public static void getToken() {
        if (!isConnected()) {
            return;
        }
        // 同步调用方式
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(mHuaweiApiClient);
        // 结果通过广播返回，不通过pendingResult返回，预留接口
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                LogUtil.i(TAG, "getToken | onResult -> \n" + print(result));
            }
        });
    }

    /**
     * 获取Push连接状态
     */
    public static void getState() {
        if (!isConnected()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 状态结果通过广播返回
                HuaweiPush.HuaweiPushApi.getPushState(mHuaweiApiClient);
            }
        }.start();
    }

    /**
     * 设置接收推送
     */
    public static void setReceiveEnable() {
        if (!isConnected()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(mHuaweiApiClient, true);
                HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(mHuaweiApiClient, true);
            }
        }).start();
    }

    /**
     * 打印
     */
    public static String print(TokenResult result) {
        StringBuilder builder = new StringBuilder();
        builder.append("result.getStatus().getStatusCode() -> ").append(result.getStatus().getStatusCode()).append("\n");
        builder.append("result.getStatus().getStatusMessage() -> ").append(result.getStatus().getStatusMessage()).append("\n");
        builder.append("result.getTokenRes().getRetCode() -> ").append(result.getTokenRes().getRetCode()).append("\n");
        builder.append("result.getTokenRes().getToken() -> ").append(result.getTokenRes().getToken()).append("\n");
        return builder.toString();
    }

//    public static void setTag() {
//        if (!isConnected()) {
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String key = "123";
//                String value = "456";
//                HashMap<String, String> map = new HashMap<>();
//                map.put(key, value);
//                PendingResult<HandleTagsResult> handleTagsResultPendingResult = HuaweiPush.HuaweiPushApi.setTags(mHuaweiApiClient, map);
//                handleTagsResultPendingResult.setResultCallback(new ResultCallback<HandleTagsResult>() {
//                    @Override
//                    public void onResult(HandleTagsResult handleTagsResult) {
//                        LogUtil.i(TAG, "setTags | onResult");
//                        LogUtil.i(TAG, "handleTagsResult.getStatus() -> " + handleTagsResult.getStatus());
//                        if (handleTagsResult.getTagsRes() != null) {
//                            LogUtil.i(TAG, "handleTagsResult.getTagsRes().getContent() -> " + handleTagsResult.getTagsRes().getContent());
//                        } else {
//                            LogUtil.i(TAG, "handleTagsResult.getTagsRes() != null");
//                        }
//                    }
//                });
//            }
//        }).start();
//    }
//
//    public static void getTag() {
//        if (!isConnected()) {
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PendingResult<GetTagResult> resultPendingResult = HuaweiPush.HuaweiPushApi.getTags(mHuaweiApiClient);
//                resultPendingResult.setResultCallback(new ResultCallback<GetTagResult>() {
//                    @Override
//                    public void onResult(GetTagResult getTagResult) {
//                        LogUtil.i(TAG, "getTags | onResult");
//
//                        Map<String, String> tags = getTagResult.getTags();
//                        if (tags == null) {
//                            LogUtil.i(TAG, "tags == null");
//                            return;
//                        }
//
//                        StringBuilder builder = new StringBuilder();
//                        for (String key : tags.keySet()) {
//                            builder.append(key + " -> " + tags.get(key)).append("\n");
//                        }
//                        LogUtil.i(TAG, builder.toString());
//                    }
//                });
//            }
//        }).start();
//    }
}
