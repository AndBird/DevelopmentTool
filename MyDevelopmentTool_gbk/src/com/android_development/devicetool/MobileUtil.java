package com.android_development.devicetool;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android_development.tool.LogUtil;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class MobileUtil {
	private static final String TAG = MobileUtil.class.getName();
	
	/**
     * ��֤�ֻ�����
     */
    public static boolean isMobileNum(String mobiles) {
        if (TextUtils.isEmpty(mobiles))
            return false;
        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    
    /**
     * ��ȡ�ֻ�����Ӫ�����ͣ��ƶ�����ͨ������
     *
     * @param context
     * @return
     */
    public static String getProvider(Context context) {
        String provider = "δ֪";
        String IMSI = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            IMSI = telephonyManager.getSubscriberId();
            // IMSI��ǰ��3λ460�ǹ��ң������ź���2λ00 02 07���й��ƶ���01���й���ͨ��03���й����š�
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                        .getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    LogUtil.printLogE(TAG, "getProvider.operator:" + operator);
                    if (operator != null) {
                        if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                            provider = "�й��ƶ�";
                        } else if (operator.equals("46001")) {
                            provider = "�й���ͨ";
                        } else if (operator.equals("46003")) {
                            provider = "�й�����";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    provider = "�й��ƶ�";
                } else if (IMSI.startsWith("46001")) {
                    provider = "�й���ͨ";
                } else if (IMSI.startsWith("46003")) {
                    provider = "�й�����";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }
    
     /**
      * ��ȡΨһ���豸���
      * ��ҪȨ��:<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
      * �ο���ַ:http://blog.csdn.net/nugongahou110/article/details/47003257
      * */
  	public static String getUniqueID(Context context){
  		String imei = null;
  		try {
  			TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
  			imei = manager.getDeviceId();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		
  		if(imei == null){
			 String serial = null;
			 String m_szDevIDShort = "35" + 
	            Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
	            Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + 
	            Build.DISPLAY.length()%10 + Build.HOST.length()%10 + 
	            Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
	            Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + 
	            Build.TAGS.length()%10 + Build.TYPE.length()%10 + 
	            Build.USER.length()%10 ; //2λ + 13 λ

  		    try {
  		        serial = android.os.Build.class.getField("SERIAL").get(null).toString();
  		       //API>=9 ʹ��serial��
  		       imei = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
  		    } catch (Exception exception) {
  		        //serial��Ҫһ����ʼ��
  		        serial = "serial"; // ���һ����ʼ��
  		    }
	    	//ʹ��Ӳ����Ϣƴ�ճ�����15λ����
	    	imei= new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
		}
  		return imei;
  	}
}
