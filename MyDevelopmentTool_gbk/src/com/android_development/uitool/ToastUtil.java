package com.android_development.uitool;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	 /**
     * ��ʱ����ʾ�ַ���
     *
     * @param context
     * @param showInfo
     */
    public static void showStringShort(Context context, String showInfo) {
        Toast.makeText(context, showInfo, Toast.LENGTH_SHORT).show();
    }

    /**
     * ��ʱ����ʾ�ַ���
     *
     * @param context
     * @param showInfo
     */
    public static void showStringLong(Context context, String showInfo) {
        Toast.makeText(context, showInfo, Toast.LENGTH_LONG).show();
    }

    /**
     * ��ʱ����ʾResources String
     *
     * @param context
     * @param resId
     */
    public static void showResourcesShort(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * ��ʱ����ʾResources String
     *
     * @param context
     * @param resId
     */
    public static void showResourcesLong(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }
}
