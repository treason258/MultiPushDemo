package com.mjiayou.multipushdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.mjiayou.multipushdemo.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private HuaweiApiClient mHuaweiApiClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initHWPush();
    }

    private void initHWPush() {
        mHuaweiApiClient = new HuaweiApiClient.Builder(this)
                .addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected() {
                        LogUtil.i(TAG, "HuaweiApiClient.addConnectionCallbacks | onConnected");
                        getState();
                        getToken();
                        setReceiveEnable();
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

    public boolean isConnected() {
        if (mHuaweiApiClient != null && mHuaweiApiClient.isConnected()) {
            return true;
        } else {
            LogUtil.i(TAG, "HMS is disconnect.");
            return false;
        }
    }

    private void getToken() {
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

    private void getState() {
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

    private void setReceiveEnable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(mHuaweiApiClient, true);
                HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(mHuaweiApiClient, true);
            }
        }).start();
    }

    private String print(TokenResult result) {
        StringBuilder builder = new StringBuilder();
        builder.append("result.getStatus().getStatusCode() -> ").append(result.getStatus().getStatusCode()).append("\n");
        builder.append("result.getStatus().getStatusMessage() -> ").append(result.getStatus().getStatusMessage()).append("\n");
        builder.append("result.getTokenRes().getRetCode() -> ").append(result.getTokenRes().getRetCode()).append("\n");
        builder.append("result.getTokenRes().getToken() -> ").append(result.getTokenRes().getToken()).append("\n");
        return builder.toString();
    }
}
