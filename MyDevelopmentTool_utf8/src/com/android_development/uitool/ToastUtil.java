package com.android_development.uitool;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	 /**
     * 短时间显示字符串
     *
     * @param context
     * @param showInfo
     */
    public static void showStringShort(Context context, String showInfo) {
        Toast.makeText(context, showInfo, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示字符串
     *
     * @param context
     * @param showInfo
     */
    public static void showStringLong(Context context, String showInfo) {
        Toast.makeText(context, showInfo, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Resources String
     *
     * @param context
     * @param resId
     */
    public static void showResourcesShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Resources String
     *
     * @param context
     * @param resId
     */
    public static void showResourcesLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }
}
