# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/treason/Archives/dev/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

## ******************************** 极光推送 ********************************

-keep class com.mjiayou.multipushdemo.MyJPushReceiver {*;}

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

## ******************************** 小米推送 ********************************

-keep class com.mjiayou.multipushdemo.MyMiPushReceiver {*;}

#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-dontwarn com.xiaomi.push.**

## ******************************** 华为推送 ********************************

-keep class com.mjiayou.multipushdemo.MyHWPushReceiver {*;}
