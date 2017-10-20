package com.android_development.tool;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2016/4/11.
 * 功能: 日志打印类
 */
public class LogUtil {

    public static void printLogE(String TAG, String msg){
        if(!TextUtils.isEmpty(TAG) && !TextUtils.isEmpty(msg)){
            Log.e(TAG, msg);
        }
    }

    public static void printLogI(String TAG, String msg){
        if(!TextUtils.isEmpty(TAG) && !TextUtils.isEmpty(msg)){
            Log.i(TAG, msg);
        }
    }

    public static void printLogW(String TAG, String msg){
        if(!TextUtils.isEmpty(TAG) && !TextUtils.isEmpty(msg)){
            Log.w(TAG, msg);
        }
    }

    public static void printLogD(String TAG, String msg){
        if(!TextUtils.isEmpty(TAG) && !TextUtils.isEmpty(msg)){
            Log.d(TAG, msg);
        }
    }

    public static void printLogV(String TAG, String msg){
        if(!TextUtils.isEmpty(TAG) && !TextUtils.isEmpty(msg)){
            Log.v(TAG, msg);
        }
    }
}
