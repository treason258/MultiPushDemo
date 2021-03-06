package com.mjiayou.multipushdemo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;

/**
 * Created by treason on 2016/11/22.
 */

public class AppUtil {

    /**
     * 获取指定包名APP的签名指纹的MD5
     */
    public static String getSignMD5(Context context, String packageName) {
        try {
            PackageInfo packageInfo = getPackageInfo(context, packageName);
            Signature[] signatures = packageInfo.signatures;

            StringBuilder builder = new StringBuilder();
            builder.append(md5(signatures[0].toByteArray()));
            return builder.toString();
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
        return null;
    }

    /**
     * 获得PackageInfo对象
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) throws PackageManager.NameNotFoundException {
        return getPackageManager(context).getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
    }

    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context, context.getPackageName());
    }

    /**
     * 获取PackageManager
     */
    public static PackageManager getPackageManager(Context context) {
        return context.getPackageManager();
    }

    public static String md5(byte[] value) {
        return encode("MD5", value);
    }

    /**
     * encode
     */
    private static String encode(String algorithm, byte[] value) {
        if (value == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value);
            byte[] arrayOfByte = messageDigest.digest();
            int length = arrayOfByte.length;

            StringBuilder hex = new StringBuilder(length * 2);
            char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            for (byte b : arrayOfByte) {
                hex.append(HEX_DIGITS[(b >> 4) & 0x0f]);
                hex.append(HEX_DIGITS[b & 0x0f]);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得IMEI（telephonyManager.getDeviceId）
     */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = getTelephonyManager(context);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            LogUtil.printStackTrace(e);
        }
        return null;
    }

    /**
     * 获得TelephonyManager对象
     */
    private static TelephonyManager getTelephonyManager(Context context) throws Exception {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager;
    }
}