package com.android_development.tool;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import android.text.TextUtils;


public class OtherTool {
	private static final String TAG = OtherTool.class.getSimpleName();
	
	//��ϲ���
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
    
    //��ȡ���������ַ
    public static String getRequestUrl(Map<String, String> paramsMap, String md5Param, String md5Key){
    	return getMapParamsString(paramsMap, null) + "&" + md5Param + "=" + getParamsMD5String(paramsMap, md5Key, "#");
    }
    
	
    //��ȡmd5�ַ���
	public static String getParamsMD5String(Map<String, String> paramsMap, String md5Key, String splitStr){
		try {
			paramsMap = sortMapByKey(paramsMap); //������
			if (paramsMap == null || paramsMap.isEmpty()) {
				DebugUtils.debug(TAG, "sortMapByKey return null map");
				return "";
			}
			StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            	String value = entry.getValue();
            	if(!"0".equals(value) && !TextUtils.isEmpty(value)){//0�Ϳմ�������md5����
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
	 * ʹ�� Map��key��������
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		//Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
		Map<String, String> sortMap = new TreeMap<String, String>();//TreeMapĬ���������
		sortMap.putAll(map);
		return sortMap;
	}
	
	static class MapKeyComparator implements Comparator<String>{
		@Override
		public int compare(String str1, String str2) {
			//return str1.compareTo(str2);//����
			return str2.compareTo(str1);//����
		}
	}
	
	
	//ȥ��С��������0
	public static String getPriceString(float price){
		String string = "";
		try {
			DecimalFormat df = new DecimalFormat("#####0.00");//0.12���"0.12"
			string = df.format(price);
			if(string.endsWith("0")){
				if(string.endsWith(".00")){
					//��2��0
					string = string.split("\\.")[0];
				}else{
					//��1��0
					string = string.substring(0, string.length() - 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
}
