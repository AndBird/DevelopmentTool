package com.android_development.uitool;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * 及时显示Toast,无需排队
 * 
 * */
public class ToastInTime {
	public static final int LENGTH_LONG =  Toast.LENGTH_LONG;
	public static final int LENGTH_SHORT =  Toast.LENGTH_SHORT;
	private volatile static ToastInTime instance = null;
	
	private View v;
    private Toast it;
    private boolean isInited = false;
    
    private ToastInTime(Context context){
		init(context);
	}
    
    private ToastInTime(){
    	isInited = false;
	}
    
    /** 
	 * 
	 *  */
	public static ToastInTime getInstance(Context context){
		if (instance == null) {
			synchronized (ToastInTime.class) {
				if (instance == null) {
					instance = new ToastInTime(context);
				}
			}
		}
		return instance;
	}
	
	  /**
     * 此方法需要自行调用init方法初始化
     * 
     * */
    public static ToastInTime getInstance(){ 
    	if (instance == null) {
			synchronized (ToastInTime.class) {
				if (instance == null) {
					instance = new ToastInTime();
				}
			}
		}
		return instance;
    }
    
    /** 
   	 * 
   	 *  */
   	public static void initToast(Context context){
   		if(!isInited()){
   			getInstance().init(context);
   		}
   	}

	public static boolean isInited(){
		if(instance == null){
			return false;
		}
		return instance.isInited2();
	}
	
	private boolean isInited2(){
		return isInited;
	}
   
    private void init(Context context) {
        v = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        it = new Toast(context);
        it.setView(v);
        isInited = true;
    }
   
    /**
     * 显示String
     * */
    public void display(String text, int duration) {
        it.setText(text);
        it.setDuration(duration);
        it.show();
    }

   /**
    *  显示Resources String
    *  */
    public void display(int resId, int duration) {
        it.setText(resId);
        it.setDuration(duration);
        it.show();
    }
    
    public void destory(){
    	try {
			it = null;
			instance = null;
			v = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
