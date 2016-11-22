package com.mjiayou.multipushdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mjiayou.multipushdemo.helper.Configs;
import com.mjiayou.multipushdemo.util.AppUtil;
import com.mjiayou.multipushdemo.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvInfo = (TextView) findViewById(R.id.tv_info);

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
    }
}
