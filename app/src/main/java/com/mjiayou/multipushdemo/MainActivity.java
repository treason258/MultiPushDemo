package com.mjiayou.multipushdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.mjiayou.multipushdemo.helper.Configs;
import com.mjiayou.multipushdemo.util.AppUtil;
import com.mjiayou.multipushdemo.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Activity mActivity;
    private Context mContext;

    private TextView mTvInfo;

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static HuaweiApiClient mHuaweiApiClient = null;
    private boolean mResolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;

        mTvInfo = (TextView) findViewById(R.id.tv_info);

        initHWPush(this);

        StringBuilder builder = new StringBuilder();
        builder.append("签名MD5 -> " + AppUtil.getSignMD5(this, getPackageName())).append("\n");
        builder.append("设备IMEI -> " + AppUtil.getIMEI(this)).append("\n");
        builder.append("\n");
        builder.append("是否是小米系统 -> " + AppUtil.ROM.isMIUI()).append("\n");
        builder.append("是否是华为系统 -> " + AppUtil.ROM.isMIUI()).append("\n");
        builder.append("\n");
        builder.append("当前使用的推送平台 -> " + Configs.PUSH_PLATFORM).append("\n");
        LogUtil.i(TAG, builder.toString());
        mTvInfo.setText(builder.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHuaweiApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RESOLVE_ERROR) {
            LogUtil.i(TAG, "onActivityResult, ResultCode: " + resultCode + ", Intent: " + data);

            mResolvingError = false;

            int result = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(this);

            if (result == ConnectionResult.SUCCESS) {
                if (!mHuaweiApiClient.isConnecting() && !mHuaweiApiClient.isConnected()) {
                    mHuaweiApiClient.connect();
                }
            } else {
                LogUtil.i(TAG, "result != ConnectionResult.SUCCESS");
            }
        }
    }

    private void initHWPush(Context context) {
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

                        if (mResolvingError) {
                            return;
                        }

                        int errorCode = connectionResult.getErrorCode();
                        HuaweiApiAvailability availability = HuaweiApiAvailability.getInstance();

                        if (availability.isUserResolvableError(errorCode)) {
                            mResolvingError = true;
                            availability.resolveError(mActivity, errorCode, REQUEST_RESOLVE_ERROR, new HuaweiApiAvailability.OnUpdateListener() {
                                @Override
                                public void onUpdateFailed(@NonNull ConnectionResult connectionResult) {
                                    LogUtil.i(TAG, "onUpdateFailed, ErrorCode: " + connectionResult.getErrorCode());

                                    mResolvingError = false;
                                }
                            });
                        }
                    }
                })
                .build();
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
}
