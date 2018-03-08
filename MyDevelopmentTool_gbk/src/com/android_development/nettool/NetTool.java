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
 * 功能: 网络工具
 *
 * 需改进: 需完善文件上传功能
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

    // 适配低版本手机
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
     * post网络请求
     * @param encoding : "UTF-8" or null
     * */
    public static InputStream getInputStreamByPost(String destUrl, Map<String, String> params, String encoding) {
        try {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (null != encoding) {
                    //System.out.println("Tool 上传utf8前:"+entry.getKey()+","+entry.getValue());
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encoding));
                    //	System.out.println("Tool 上传utf8后:"+entry.getKey()+","+URLEncoder.encode(entry.getValue(),encoding));
                } else {
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                }
                sb.append("&");
            }
            String data = sb.deleteCharAt(sb.length() - 1).toString();
            URL url;
            System.out.println("urlPath:" + destUrl);
            url = new URL(destUrl);
            // 打开连接
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            // 设置提交方式
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // post方式不能使用缓存
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 设置连接超时
            conn.setConnectTimeout(6 * 1000);
        	conn.setReadTimeout(6 * 1000);
            // 配置本地连接的content-type
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 维持长连接
            //conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置浏览器编码
            conn.setRequestProperty("Charset", "UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 将请求参数数据发送到服务器
            dos.writeBytes(data);
            dos.flush();
            dos.close();
            if (200 == conn.getResponseCode()) {
                // 获得服务器的输出流
                return conn.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * post网络请求
     * */
    public static InputStream getInputStreamByPost(String destUrl, String data) {
        try {
            URL url;
            System.out.println("urlPath:" + destUrl);
            url = new URL(destUrl);
            // 打开连接
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            // 设置提交方式
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            // post方式不能使用缓存
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 设置连接超时
            conn.setConnectTimeout(6 * 1000);
        	conn.setReadTimeout(6 * 1000);
            // 配置本地连接的content-type
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置浏览器编码
            conn.setRequestProperty("Charset", "UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 将请求参数数据发送到服务器
            dos.writeBytes(data);
            dos.flush();
            dos.close();
            if (200 == conn.getResponseCode()) {
                // 获得服务器的输出流
                return conn.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    /**
     * get网络请求
     * */
    public static InputStream getInputStreamByGet(String destUrl) {
        try {
            URL url;
            InputStream stream = null;
            url = new URL(destUrl);
            // 打开连接
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            // 设置提交方式
            //如果打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true(这时setRequestMethod("GET")失效)；如果不打算使用，则设置为 false。默认值为 false。
            conn.setDoOutput(false);
            //如果打算使用 URL 连接进行输入，则将 DoInput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 true。  
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("User-agent", "");
            // post方式不能使用缓存
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 设置连接超时,
            conn.setConnectTimeout(6 * 1000);
        	conn.setReadTimeout(6 * 1000);
            // 配置本地连接的content-type
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // 维持长连接
            //conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置浏览器编码
            conn.setRequestProperty("Charset", "UTF-8");
            if (200 == conn.getResponseCode()) {
                // 获得服务器的输出流
                stream = conn.getInputStream();
                return stream;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将InputStream 流读取 utf-8
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
     * 将InputStream 流读取 utf-8
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


    //网络请求时中文参数转换
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
     * 检测网络是否可用
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
       // return ni != null && ni.isConnectedOrConnecting();
        return ni != null && ni.isConnected();// && ni.isAvailable();
    }

    /**
     * 检测wifi网络是否可用
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
     * 获取网络地址（包括有线网络,有线盒子返回NetWorkType 为9）
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
                            if(!ipaddress.contains("::")){//ipV6的地址
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
            LogUtil.printLogE(TAG, "getlocalIP 异常:" + e.toString());
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取wifi网络ip地址
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
//    private static final int TIME_OUT = 120*1000;   //超时时间
//    private static final String CHARSET = "utf-8"; //设置编码
//    public static final String SUCCESS="1";
//    public static final String FAILURE="0";
//    /**
//    * 上传小文件(未测试过)
//     * file 上传的文件
//     * fileName 为文件保存名
//     * RequestURL 上传地址
//     * */
//    public static String uploadFile(File file, String fileName, String RequestURL) {
//        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //内容类型
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
     * 获取手机网络类型:wifi, 2g, 3g, 4g
     *
     * @param context
     * @return
     */
    public static String getPhoneCurrentNetworkType(Context context) {
        int networkClass = getNetworkClass(context);
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "无";
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
                type = "未知";
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

    //获取网络类型(盒子)
    public static String getNetworkType(Context context) {
    	Integer networkType = getActiveNetworkType(context);
        if (networkType == null) {
            return "NETWORK_NO_CONNECTION";
        }
        switch (networkType) {
            case ConnectivityManager.TYPE_MOBILE:
                return "移动网络";
            case ConnectivityManager.TYPE_WIFI:
                return "WIFI";
            case ConnectivityManager.TYPE_ETHERNET: //新增，安卓智能电视网络类型
                return "有线网络";
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
