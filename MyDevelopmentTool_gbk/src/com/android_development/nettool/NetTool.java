package com.android_development.nettool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android_development.tool.LogUtil;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/4/11.
 * ����: ���繤��
 *
 * ��Ľ�: �������ļ��ϴ�����
 */
public class NetTool {
    private static final String TAG = NetTool.class.getName();
    
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    private static final int NETWORK_CLASS_4_G = 3;

    // ����Ͱ汾�ֻ�
    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;    
    
    /**
     * The Ethernet data connection.  When active, all data traffic
     * will use this network type's interface by default
     * (it has a default route).
     */
    public static final int TYPE_ETHERNET    = 9;
    
    
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    

    /**
     * post��������
     * @param encoding : "UTF-8" or null
     * */
    public static InputStream getInputStreamByPost(String destUrl, Map<String, String> params, String encoding) {
        try {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (null != encoding) {
                    //System.out.println("Tool �ϴ�utf8ǰ:"+entry.getKey()+","+entry.getValue());
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encoding));
                    //	System.out.println("Tool �ϴ�utf8��:"+entry.getKey()+","+URLEncoder.encode(entry.getValue(),encoding));
                } else {
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                }
                sb.append("&");
            }
            String data = sb.deleteCharAt(sb.length() - 1).toString();
            URL url;
            System.out.println("urlPath:" + destUrl);
            url = new URL(destUrl);
            // ������
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            // �����ύ��ʽ
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // post��ʽ����ʹ�û���
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // �������ӳ�ʱ
            conn.setConnectTimeout(6 * 1000);
        	conn.setReadTimeout(6 * 1000);
            // ���ñ������ӵ�content-type
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // ά�ֳ�����
            //conn.setRequestProperty("Connection", "Keep-Alive");
            // �������������
            conn.setRequestProperty("Charset", "UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // ������������ݷ��͵�������
            dos.writeBytes(data);
            dos.flush();
            dos.close();
            if (200 == conn.getResponseCode()) {
                // ��÷������������
                return conn.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * post��������
     * */
    public static InputStream getInputStreamByPost(String destUrl, String data) {
        try {
            URL url;
            System.out.println("urlPath:" + destUrl);
            url = new URL(destUrl);
            // ������
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            // �����ύ��ʽ
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // post��ʽ����ʹ�û���
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // �������ӳ�ʱ
            conn.setConnectTimeout(6 * 1000);
        	conn.setReadTimeout(6 * 1000);
            // ���ñ������ӵ�content-type
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // ά�ֳ�����
            conn.setRequestProperty("Connection", "Keep-Alive");
            // �������������
            conn.setRequestProperty("Charset", "UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // ������������ݷ��͵�������
            dos.writeBytes(data);
            dos.flush();
            dos.close();
            if (200 == conn.getResponseCode()) {
                // ��÷������������
                return conn.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    /**
     * get��������
     * */
    public static InputStream getInputStreamByGet(String destUrl) {
        try {
            URL url;
            InputStream stream = null;
            url = new URL(destUrl);
            // ������
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            // �����ύ��ʽ
            //�������ʹ�� URL ���ӽ���������� DoOutput ��־����Ϊ true(��ʱsetRequestMethod("GET")ʧЧ)�����������ʹ�ã�������Ϊ false��Ĭ��ֵΪ false��
            conn.setDoOutput(false);
            //�������ʹ�� URL ���ӽ������룬�� DoInput ��־����Ϊ true�����������ʹ�ã�������Ϊ false��Ĭ��ֵΪ true��  
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("User-agent", "");
            // post��ʽ����ʹ�û���
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // �������ӳ�ʱ,
            conn.setConnectTimeout(6 * 1000);
        	conn.setReadTimeout(6 * 1000);
            // ���ñ������ӵ�content-type
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // ά�ֳ�����
            //conn.setRequestProperty("Connection", "Keep-Alive");
            // �������������
            conn.setRequestProperty("Charset", "UTF-8");
            if (200 == conn.getResponseCode()) {
                // ��÷������������
                stream = conn.getInputStream();
                return stream;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ��InputStream ����ȡ utf-8
    * */
    public static String InputStreamToString2(InputStream in) {
    	if(in == null){
    		return null;
    	}
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        String content = null;
        try {
            while ((count = in.read(data, 0, 1024)) != -1) {
                outStream.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            content = null;
        }
        data = null;
        try {
            content = new String(outStream.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
        	if(outStream != null){
        		outStream.close();
        		outStream = null;
        	}
        	if(in != null){
            	in.close();
            	in = null;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        return content;
    }
    
    /**
     * ��InputStream ����ȡ utf-8
    * */
    public static String InputStreamToString(InputStream in) {
		if(in == null){
			return null;
		}
		StringBuffer sbBuffer = new StringBuffer();
		byte[] data = new byte[1024];
		int count = -1;
		try {
			while ((count = in.read(data, 0, 1024)) != -1) {
				sbBuffer.append(new String(data, 0, count));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			data = null;
			try {
				if(in != null){
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sbBuffer.toString();
	}


    //��������ʱ���Ĳ���ת��
    public void zh(String data){
        if(TextUtils.isEmpty(data)){
            return ;
        }
        try {
            String data1 = URLEncoder.encode(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * ��������Ƿ����
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
       // return ni != null && ni.isConnectedOrConnecting();
        return ni != null && ni.isConnected();// && ni.isAvailable();
    }

    /**
     * ���wifi�����Ƿ����
     */
    public static boolean isWifiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if((null != ni) && ni.getType() == ConnectivityManager.TYPE_WIFI){
            return true;
        }else{
            return false;
        }
    }

//    public static final int NETWORN_NONE = 0;
//    public static final int NETWORN_WIFI = 1;
//    public static final int NETWORN_MOBILE = 2;
//    public static int getNetworkState(Context context) {
//        try {
//            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            // Wifi
//            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if(wifiInfo != null){
//                State state = wifiInfo.getState();
//                if (state == State.CONNECTED || state == State.CONNECTING) {
//                    return NETWORN_WIFI;
//                }
//            }
//
//            // 3G
//            NetworkInfo _3gInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            if(_3gInfo != null){
//                State state = _3gInfo.getState();
//                if (state == State.CONNECTED || state == State.CONNECTING) {
//                    return NETWORN_MOBILE;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return NETWORN_NONE;
//    }


    /**
     * ��ȡ�����ַ��������������,���ߺ��ӷ���NetWorkType Ϊ9��
     * return ip or null
     * */
    public String getlocalIP(){
        String ip = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ){
                NetworkInterface intf = en.nextElement();
                if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")){
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if(!ipaddress.contains("::")){//ipV6�ĵ�ַ
                                LogUtil.printLogE(TAG, "local ip=" + ip);
                                ip = ipaddress;
                                return ip;
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            LogUtil.printLogE(TAG, "getlocalIP �쳣:" + e.toString());
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * ��ȡwifi����ip��ַ
     * return ip or null
     * */
    public static String getWifiNetIP(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if(ipAddress==0)
            return null;
        return ((ipAddress & 0xff) + "." + (ipAddress>>8 & 0xff) + "." +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
    }

//    private static final String TAG = "uploadFile";
//    private static final int TIME_OUT = 120*1000;   //��ʱʱ��
//    private static final String CHARSET = "utf-8"; //���ñ���
//    public static final String SUCCESS="1";
//    public static final String FAILURE="0";
//    /**
//    * �ϴ�С�ļ�(δ���Թ�)
//     * file �ϴ����ļ�
//     * fileName Ϊ�ļ�������
//     * RequestURL �ϴ���ַ
//     * */
//    public static String uploadFile(File file, String fileName, String RequestURL) {
//        String  BOUNDARY =  UUID.randomUUID().toString();  //�߽��ʶ   �������
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //��������
//        int res = 0;
//        try {
//            URL url = new URL(RequestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(TIME_OUT);
//            conn.setConnectTimeout(TIME_OUT);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Charset", CHARSET);
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            if(file!=null)
//            {
//                OutputStream outputSteam=conn.getOutputStream();
//                DataOutputStream dos = new DataOutputStream(outputSteam);
//                StringBuffer sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+fileName+"\"" + LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
//                sb.append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while((len=is.read(bytes))!=-1) {
//                    dos.write(bytes, 0, len);
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
//                dos.write(end_data);
//                dos.flush();
//
//                res = conn.getResponseCode();
//                Log.e(TAG, "response code: " + res);
//                if(res==200) {
//                    return SUCCESS;
//                }
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return FAILURE;
//    }
    
    
    /**
     * ��ȡ�ֻ���������:wifi, 2g, 3g, 4g
     *
     * @param context
     * @return
     */
    public static String getPhoneCurrentNetworkType(Context context) {
        int networkClass = getNetworkClass(context);
        String type = "δ֪";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "��";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi_Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "δ֪";
                break;
        }
        return type;
    }

    private static int getNetworkClass(Context context) {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);
    }
    
    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    //��ȡ��������(����)
    public static String getNetworkType(Context context) {
    	Integer networkType = getActiveNetworkType(context);
        if (networkType == null) {
            return "NETWORK_NO_CONNECTION";
        }
        switch (networkType) {
            case ConnectivityManager.TYPE_MOBILE:
                return "�ƶ�����";
            case ConnectivityManager.TYPE_WIFI:
                return "WIFI";
            case ConnectivityManager.TYPE_ETHERNET: //��������׿���ܵ�����������
                return "��������";
            default:
                return "UnKnown";
        }
    }
    
    private static Integer getActiveNetworkType(Context context) {
    	ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (connectivity == null) {
    	    LogUtil.printLogE(TAG, "couldn't get connectivity manager");
    	    return null;
    	}

    	NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
    	if (activeInfo == null) {
    		LogUtil.printLogE(TAG, "network is not available");
    	    return null;
    	}
    	return activeInfo.getType();
     }
}
