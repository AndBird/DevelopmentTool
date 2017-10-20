package com.android_development.dbtool;

import android.database.Cursor;
import android.text.TextUtils;

import com.android_development.tool.LogUtil;
import com.android_development.tool.ToolBase;

/**
 * 功能：该类是用于读取数据库字段的
 * 意义：在读取安卓系统的数据表时，由于不同API版本的出入，数据表字段也不尽一致，对于一些不调用API类成员字段去操作数据库的程序段
 *       ，通过工具(如android lint)去检测API差异很难，人工比较版本差异也难，因此容易出现异常
 *
 * */
public class DBReaderTool extends ToolBase{
	private static final String TAG = DBReaderTool.class.getName();
	private  static boolean printLog = true;//打印日志

	public static void setPrintLog(boolean printLog) {
		DBReaderTool.printLog = printLog;
	}

	/**
	 * return intValue or errorValue
	 * */
	public static int getInt(Cursor cursor, String column_name){
		return getInt(cursor, column_name, intErrorValue);
	}

	/**
	 * return intValue or defaultValue
	 * */
	public static int getInt(Cursor cursor, String column_name, final int defaultValue){
		try {
			if(cursor == null || TextUtils.isEmpty(column_name)){
				printLog("cursor is null or column_name is empty");
				return defaultValue;
			}
			int index = cursor.getColumnIndex(column_name);
			if(index < 0){
				printLog("getColumnIndex() < 0, this Column=" + column_name);
				return defaultValue;
			}
			return cursor.getInt(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * return longValue or errorValue
	 * */
	public static long getLong(Cursor cursor, String column_name){
		return getLong(cursor, column_name, longErrorValue);
	}

	/**
	 * return longValue or defaultValue
	 * */
	public static long getLong(Cursor cursor, String column_name, final long defaultValue){
		try {
			if(cursor == null || TextUtils.isEmpty(column_name)){
				printLog("cursor is null or column_name is empty");
				return defaultValue;
			}
			int index = cursor.getColumnIndex(column_name);
			if(index < 0){
				printLog("getColumnIndex() < 0, this Column=" + column_name);
				return defaultValue;
			}
			return cursor.getLong(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * return stringValue or errorValue
	 * */
	public static String getString(Cursor cursor, String column_name){
		return getString(cursor, column_name, stringErrorValue);
	}

	/**
	 * return stringValue or defaultValue
	 * */
	public static String getString(Cursor cursor, String column_name, final String defaultValue){
		try {
			if(cursor == null || TextUtils.isEmpty(column_name)){
				printLog("cursor is null or column_name is empty");
				return defaultValue;
			}
			int index = cursor.getColumnIndex(column_name);
			if(index < 0){
				printLog("getColumnIndex() < 0, this Column=" + column_name);
				return defaultValue;
			}
			return cursor.getString(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * return shortValue or errorValue
	 * */
	public static short getShort(Cursor cursor, String column_name){
		return getShort(cursor, column_name, shortErrorValue);
	}

	/**
	 * return shortValue or defaultValue
	 * */
	public static short getShort(Cursor cursor, String column_name, final short defaultValue){
		try {
			if(cursor == null || TextUtils.isEmpty(column_name)){
				printLog("cursor is null or column_name is empty");
				return defaultValue;
			}
			int index = cursor.getColumnIndex(column_name);
			if(index < 0){
				printLog("getColumnIndex() < 0, this Column=" + column_name);
				return defaultValue;
			}
			return cursor.getShort(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * return doubleValue or errorValue
	 * */
	public static double getDouble(Cursor cursor, String column_name){
		return getDouble(cursor, column_name, doubleErrorValue);
	}


	/**
	 * return doubleValue or defaultValue
	 * */
	public static double getDouble(Cursor cursor, String column_name, final double defaultValue){
		try {
			if(cursor == null || TextUtils.isEmpty(column_name)){
				printLog("cursor is null or column_name is empty");
				return defaultValue;
			}
			int index = cursor.getColumnIndex(column_name);
			if(index < 0){
				printLog("getColumnIndex() < 0, this Column=" + column_name);
				return defaultValue;
			}
			return cursor.getDouble(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * return floatValue or errorValue
	 * */
	public static float getFloat(Cursor cursor, String column_name){
		return getFloat(cursor, column_name, floatErrorValue);
	}

	/**
	 * return floatValue or defaultValue
	 * */
	public static float getFloat(Cursor cursor, String column_name, final float defaultValue){
		try {
			if(cursor == null || TextUtils.isEmpty(column_name)){
				printLog("cursor is null or column_name is empty");
				return defaultValue;
			}
			int index = cursor.getColumnIndex(column_name);
			if(index < 0){
				printLog("getColumnIndex() < 0, this Column=" + column_name);
				return defaultValue;
			}
			return cursor.getFloat(index);
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
