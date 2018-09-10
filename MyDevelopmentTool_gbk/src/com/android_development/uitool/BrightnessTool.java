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

/**��Ļ���ȹ���
 * 
 * �޸�ϵͳ������ҪȨ�ޣ�<uses-permission android:name="android.permission.WRITE_SETTINGS"/>
 * 
 * */
public class BrightnessTool {
	
	/** �ж��Ƿ������Զ����ȵ��� */  
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
    
    /** ��ȡϵͳ������
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

    /** ֹͣ�Զ����ȵ��� */  
    public static boolean stopAutoBrightness(Context context) {     
    	return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  
     }  
   
  
    /** ���������Զ����� *    
    * @param activity */  
    public static boolean startAutoBrightness(Context context) {     
      return Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);  
    }  
      

    /** ����ϵͳ���� 
     * 
     * @param brightness : 0--255 
     * */  
    public static void saveBrightness(ContentResolver resolver, int brightness) {      
    	Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);     
    	android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);      
    	// resolver.registerContentObserver(uri, true, myContentObserver);     
    	resolver.notifyChange(uri, null);
	}  
    
   /** ����޸�ϵͳ����Ȩ���Ƿ�����*/
    public static boolean checkPermission(Activity activity){
		if(activity.checkCallingPermission(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
			return true;
		}
		return false;
    }
    
    
    /**��ϵͳ�������óɵ�ǰ��Ļ����*/
    public static void setWindowBrightness(Activity activity){
	    int systemBrightness = getScreenBrightness(activity.getApplicationContext());
		//Log.e(TAG, "ϵͳ����:" + systemBrightness);
	    Window window = activity.getWindow();
	    WindowManager.LayoutParams lp = window.getAttributes();
	    float b = systemBrightness * 1f / 255;
	    //Log.e(TAG, "ϵͳ����ת������Ļ����:" + b);
	    //�����Ǵ�0~1�е�һ��������
	    lp.screenBrightness =  Math.min(Math.max(b, 0.01f), 1);
	    //set Brightness
	    window.setAttributes(lp);
    }
    
    /** 
     * ���õ�ǰ��Ļ������
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
