package com.android_development.nettool;

import java.util.Random;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;

public class UserAgent {
	public static String[] userAgent = new String[]{
		//
		"Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.9 Mobile Safari/537.36",
		"Mozilla/5.0 (Linux; Android 8.1.0; ALP-AL00 Build/HUAWEIALP-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.1.0)",
		"Mozilla/5.0 (Linux; Android 6.0.1; OPPO A57 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.10 (Baidu; P1 6.0.1)",
		"Mozilla/5.0 (Linux; Android 8.0; MHA-AL00 Build/HUAWEIMHA-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044304 Mobile Safari/537.36 MicroMessenger/6.7.3.1360(0x26070333) NetType/4G Language/zh_CN Process/tools",
		"Mozilla/5.0 (Linux; Android 5.1.1; vivo X6S A Build/LMY47V) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/35.0.1916.138 Mobile Safari/537.36 T7/6.3 baiduboxapp/7.3.1 (Baidu; P1 5.1.1)",
		"Mozilla/5.0 (Linux; U; Android 5.1.1; zh-CN; MX4 Pro Build/LMY48W) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.10.0.800 U3/0.8.0 Mobile Safari/534.30",
		"Mozilla/5.0 (Linux; U; Android 5.1; zh-cn; m2 note Build/LMY47D) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/6.6 Mobile Safari/537.36",
		"Mozilla/5.0 (Linux; Android 5.1.1; KIW-CL00 Build/HONORKIW-CL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/35.0.1916.138 Mobile Safari/537.36 T7/7.1 baidubrowser/7.1.12.0 (Baidu; P1 5.1.1)",
		"Mozilla/5.0 (Linux; Android 5.1.1; SM-J3109 Build/LMY47X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36 V1_AND_SQ_6.3.6_372_YYB_D QQ/6.3.6.2790 NetType/WIFI WebP/0.4.1 Pixel/720",
		"Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; Coolpad 8297-T01 Build/KTU84P) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025477 Mobile Safari/533.1 V1_AND_SQ_5.9.0_270_YYB_D QQ/5.9.0.2530 NetType/WIFI WebP/0.3.0 Pixel/720",
		"Mozilla/5.0 (Linux; Android 4.4.4; HM 2A Build/KTU84Q) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.2 TBS/036215 Safari/537.36 V1_AND_SQ_6.3.1_350_YYB_D QQ/6.3.1.2735 NetType/4G WebP/0.3.0 Pixel/720"
	};
	
	public static String makeUserAgent(){
		if(userAgent != null && userAgent.length > 0){
			int x = new Random().nextInt(userAgent.length);
			return userAgent[x];
		}
		return "User-Agent: NOKIA5700/ UCWEB7.0.2.37/28/999";
	}
	
	public static String getUserAgent(Context context){
	        String userAgent = "";
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
	            try {
	                userAgent = WebSettings.getDefaultUserAgent(context);
	            } catch (Exception e) {
	                userAgent = System.getProperty("http.agent");
	            }
	        } else {
	            userAgent = System.getProperty("http.agent");
	        }
	        //对返回结果进行过滤
	        //okhttp3.Headers$Builder.checkNameAndValue进到这个方法里面可以看到okhttp对中文进行了过滤，如果不符合条件就抛出异常IllegalArgumentException
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0, length = userAgent.length(); i < length; i++) {
	            char c = userAgent.charAt(i);
	            if (c <= '\u001f' || c >= '\u007f') {
	                sb.append(String.format("\\u%04x", (int) c));
	            } else {
	                sb.append(c);
	            }
	        }
	        return sb.toString();
	}
}
