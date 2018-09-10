package com.android_development.uitool;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Window;
import android.view.WindowManager;

/**屏幕亮度工具
 * 
 * 修改系统设置需要权限：<uses-permission android:name="android.permission.WRITE_SETTINGS"/>
 * 
 * */
public class BrightnessTool {
	
	/** 判断是否开启了自动亮度调节 */  
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {      
    	boolean automicBrightness = false;      
    	try{          
    		automicBrightness = Settings.System.getInt(aContentResolver,                  
    				Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;     
    	}catch(SettingNotFoundException e){         
    		e.printStackTrace();    
        }      
    	return automicBrightness;  
    }  
    
    /** 获取系统的亮度
     * 
     * @return 0--255 
     *  */  
    public static int getScreenBrightness(Context context) {     
    	int nowBrightnessValue = 0;      
    	ContentResolver resolver = context.getContentResolver();      
    	try{          
    		nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);    
    	}catch(Exception e) {         
    		e.printStackTrace();    
    	}      
    	return nowBrightnessValue;  
    }  

    /** 停止自动亮度调节 */  
    public static boolean stopAutoBrightness(Context context) {     
    	return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  
     }  
   
  
    /** 开启亮度自动调节 *    
    * @param activity */  
    public static boolean startAutoBrightness(Context context) {     
      return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);  
    }  
      

    /** 设置系统亮度 
     * 
     * @param brightness : 0--255 
     * */  
    public static void saveBrightness(ContentResolver resolver, int brightness) {      
    	Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);     
    	android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);      
    	// resolver.registerContentObserver(uri, true, myContentObserver);     
    	resolver.notifyChange(uri, null);
	}  
    
   /** 检测修改系统设置权限是否被允许*/
    public static boolean checkPermission(Activity activity){
		if(activity.checkCallingPermission(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
			return true;
		}
		return false;
    }
    
    
    /**将系统亮度设置成当前屏幕亮度*/
    public static void setWindowBrightness(Activity activity){
	    int systemBrightness = getScreenBrightness(activity.getApplicationContext());
		//Log.e(TAG, "系统亮度:" + systemBrightness);
	    Window window = activity.getWindow();
	    WindowManager.LayoutParams lp = window.getAttributes();
	    float b = systemBrightness * 1f / 255;
	    //Log.e(TAG, "系统亮度转换成屏幕亮度:" + b);
	    //亮度是从0~1中的一个浮点数
	    lp.screenBrightness =  Math.min(Math.max(b, 0.01f), 1);
	    //set Brightness
	    window.setAttributes(lp);
    }
    
    /** 
     * 设置当前屏幕的亮度
     * 
     * @param screenBrightness : 0-255
     */  
     private void WindowBrightness(Window window, int screenBrightness){  
       WindowManager.LayoutParams lp = window.getAttributes();  
       float f = screenBrightness / 255.0F;  
       lp.screenBrightness = f;  
       window.setAttributes(lp);  
     }  
}
