package com.android_development.tool;

import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import android.text.TextUtils;


public class OtherTool {
	private static final String TAG = OtherTool.class.getSimpleName();
	
	//组合参数
    public static String getMapParamsString(Map<String, String> paramsMap, String encoding){
		String data = "";
    	try {
			if (paramsMap == null || paramsMap.isEmpty()) {
				return "";
			}
			StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                if (null != encoding) {
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encoding));
                } else {
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                }
                sb.append("&");
            }
            data = sb.deleteCharAt(sb.length() - 1).toString();
		} catch (Exception e) {
			e.printStackTrace();
			data = "";
		}
    	DebugUtils.debug(TAG, "getMapParamsString paramstr=" + data);
		return data;
	}
    
    //获取网络请求地址
    public static String getRequestUrl(Map<String, String> paramsMap, String md5Param, String md5Key){
    	return getMapParamsString(paramsMap, null) + "&" + md5Param + "=" + getParamsMD5String(paramsMap, md5Key, "#");
    }
    
	
    //获取md5字符串
	public static String getParamsMD5String(Map<String, String> paramsMap, String md5Key, String splitStr){
		try {
			paramsMap = sortMapByKey(paramsMap); //先排序
			if (paramsMap == null || paramsMap.isEmpty()) {
				DebugUtils.debug(TAG, "sortMapByKey return null map");
				return "";
			}
			StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            	String value = entry.getValue();
            	if(!"0".equals(value) && !TextUtils.isEmpty(value)){//0和空串不进行md5计算
            		 sb.append(entry.getValue());
            		 sb.append(splitStr);
            	}
            }
            sb.append(md5Key);
            String data = sb.toString();
            DebugUtils.debug(TAG, "getParamsMD5String: " + data);
            //String data = sb.deleteCharAt(sb.length() - splitStr.length()).toString();
            data = MD5Tool.getMD5(data);
            DebugUtils.debug(TAG, "getParamsMD5String md5=" + data);
            return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		//Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
		Map<String, String> sortMap = new TreeMap<String, String>();//TreeMap默认是升序的
		sortMap.putAll(map);
		return sortMap;
	}
	
	static class MapKeyComparator implements Comparator<String>{
		@Override
		public int compare(String str1, String str2) {
			//return str1.compareTo(str2);//升序
			return str2.compareTo(str1);//降序
		}
	}
}
