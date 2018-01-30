package com.android_development.jsontool;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 功能: 解析Json对象
 */
public class JsonTool{
    private final static String TAG = JsonTool.class.getName();
    /**
     * 一些读取失败的默认值*/
    public final static int intErrorValue = -1;
    public final static long longErrorValue = -1;
    public final static short shortErrorValue = (short)-1;
    public final static double doubleErrorValue = -1;
    public final static float floatErrorValue = -1;
    public final static String stringErrorValue = "";
    public final static boolean booleanDefaultValue = false;
    
    /*
        * return jsonObject or null
        * */
    public static JSONObject fromJsonObjGetJSONObject(String key, JSONObject obj){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getJSONObject(key);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
   * return jsonJSONArray or null
   * */
    public static JSONArray fromJsonObjGetJSONArray(String key, JSONObject obj){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getJSONArray(key);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


   /* *
   * return intValue or errorValue
   */
    public static int fromJsonObjGetInt(String key, JSONObject obj){
        return fromJsonObjGetInt(key, obj, intErrorValue);
    }

    /* *
  * return intValue or defaultValue
  */
    public static int fromJsonObjGetInt(String key, JSONObject obj, int defaultValue){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getInt(key);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }


    /* *
   * return longValue or errorValue
   */
    public static long fromJsonObjGetLong(String key, JSONObject obj){
        return fromJsonObjGetLong(key, obj, longErrorValue);
    }

    /* *
    * return longValue or defaultValue
    */
    public static long fromJsonObjGetLong(String key, JSONObject obj, long defaultValue){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getLong(key);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /* *
   * return doubleValue or errorValue
   */
    public static double fromJsonObjGetDouble(String key, JSONObject obj){
        return fromJsonObjGetDouble(key, obj, doubleErrorValue);
    }

    /* *
   * return doubleValue or defaultValue
   */
    public static double fromJsonObjGetDouble(String key, JSONObject obj, double defalutValue){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getDouble(key);
            }
            return defalutValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defalutValue;
    }

    /* *
   * return booleanValue or false
   */
    public static boolean fromJsonObjGetBoolean(String key, JSONObject obj){
        return fromJsonObjGetBoolean(key, obj, booleanDefaultValue);
    }

    /* *
   * return booleanValue or defaultValue
   */
    public static boolean fromJsonObjGetBoolean(String key, JSONObject obj, boolean defaultValue){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getBoolean(key);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /* *
  * return stringValue or errorValue
  */
    public static String fromJsonObjGetString(String key, JSONObject obj){
        return fromJsonObjGetString(key, obj, stringErrorValue);
    }

    /* *
    * return stringValue or defaultValue
    */
    public static String fromJsonObjGetString(String key, JSONObject obj, String defaultValue){
        try {
            if(obj != null && key != null && obj.has(key)){
                return obj.getString(key);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}
