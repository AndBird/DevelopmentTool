package com.android_development.filetool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/12.
 * 功能: SharePreference工具
 */
public class SharePreferenceTool {
        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static String getPrefString(Context context, String key, final String defaultValue) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getString(key, defaultValue);
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void setPrefString(Context context, final String key, final String value) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().putString(key, value).commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static boolean getPrefBoolean(Context context, final String key, final boolean defaultValue) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getBoolean(key, defaultValue);
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static boolean hasKey(Context context, final String key) {
            return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void setPrefBoolean(Context context, final String key, final boolean value) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().putBoolean(key, value).commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void setPrefInt(Context context, final String key, final int value) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().putInt(key, value).commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static int getPrefInt(Context context, final String key, final int defaultValue) {
            final SharedPreferences settings = PreferenceManager
                    .getDefaultSharedPreferences(context);
            return settings.getInt(key, defaultValue);
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void setPrefFloat(Context context, final String key, final float value) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().putFloat(key, value).commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static float getPrefFloat(Context context, final String key, final float defaultValue) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getFloat(key, defaultValue);
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void setSettingLong(Context context, final String key, final long value) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().putLong(key, value).commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static long getPrefLong(Context context, final String key, final long defaultValue) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getLong(key, defaultValue);
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void removePrefKey(Context context, final String key) {
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            settings.edit().remove(key).commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * */
        public static void clearPreference(Context context) {
            final SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.clear();
            editor.commit();
        }

        /**
         * PreferenceManager.getDefaultSharedPreferences
         * 获取所有的数据，
         * */
        public static Map<String, ?> getAll(Context context){
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            return settings.getAll();
        }


    /**
     * 自定义SharePreference文件
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param fileName
     * @param key
     * @param object
     */
    public static void setParam(Context context, String fileName, String key, Object object){
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }
        editor.commit();
    }


    /**
     * 自定义SharePreference文件
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param fileName
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String fileName, String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }
        return null;
    }

    /**
     * 自定义SharePreference文件
     * @param context
     * @param fileName
     * @param key
     */
    public static void removePrefKey(Context context, String fileName, String key){
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).commit();
    }

    /**
     * 自定义SharePreference文件
     * @param context
     * @param fileName
     * @param key
     */
    public static boolean hasKey(Context context, String fileName, String key){
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 自定义SharePreference文件
     * 清空SharePreference
     * @param context
     * @param fileName
     */
    public static void clearPref(Context context, String fileName){
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }

    /**
     * 自定义SharePreference文件
     * getAll from SharePreference
     * @param context
     * @param fileName
     */
    public static Map<String, ?> getAll(Context context, String fileName){
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
