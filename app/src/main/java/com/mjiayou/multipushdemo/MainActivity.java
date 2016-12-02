package com.mjiayou.multipushdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mjiayou.multipushdemo.common.Configs;
import com.mjiayou.multipushdemo.util.AppUtil;
import com.mjiayou.multipushdemo.util.LogUtil;
import com.mjiayou.multipushdemo.util.RomUtil;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Activity mActivity;
    private Context mContext;

    private TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;

        mTvInfo = (TextView) findViewById(R.id.tv_info);

        StringBuilder builder = new StringBuilder();
        builder.append("签名MD5 -> ").append(AppUtil.getSignMD5(this, getPackageName())).append("\n");
        builder.append("设备IMEI -> ").append(AppUtil.getIMEI(this)).append("\n");
        if (AppUtil.getIMEI(this) == null) {
            builder.append("注：").append("小米推送和华为推送需要用到IMEI值，请到应用管理查看权限是否开启").append("\n");
        }
        builder.append("\n");
        builder.append("是否是小米系统 -> ").append(RomUtil.isMIUI()).append("\n");
        builder.append("是否是华为系统 -> ").append(RomUtil.isEMUI()).append("\n");
        builder.append("是否是魅族系统 -> ").append(RomUtil.isFLYME()).append("\n");
        builder.append("当前手机系统 -> ").append(RomUtil.getRom().toString()).append("\n");
        builder.append("\n");
        builder.append("推送策略 -> ").append(Configs.PUSH_RULE).append("\n");
        builder.append("\n");
        builder.append("当前使用的推送平台 -> ").append(Configs.PUSH_PLATFORM).append("\n");
        LogUtil.i(TAG, builder.toString());
        mTvInfo.setText(builder.toString());
    }
}
