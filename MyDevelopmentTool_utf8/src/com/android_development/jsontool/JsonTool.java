package com.android_development.jsontool;

import com.android_development.tool.LogUtil;
import com.android_development.tool.ToolBase;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 功能: 解析Json对象
 */
public class JsonTool extends ToolBase{
    private final static String TAG = JsonTool.class.getName();
    private static boolean printLog = false; //打印日志

    public static void setPrintLog(boolean printLog) {
        JsonTool.printLog = printLog;
    }

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
    private String fromJsonObjGetString(String key, JSONObject obj){
        return fromJsonObjGetString(key, obj, stringErrorValue);
    }

    /* *
    * return stringValue or defaultValue
    */
    private String fromJsonObjGetString(String key, JSONObject obj, String defaultValue){
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

    private static void printLog(String msg){
        if(printLog){
            LogUtil.printLogE(TAG, msg);
        }
    }
}
