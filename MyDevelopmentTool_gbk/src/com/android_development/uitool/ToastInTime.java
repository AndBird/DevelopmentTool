package com.android_development.uitool;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * ��ʱ��ʾToast,�����Ŷ�
 * 
 * */
public class ToastInTime {
	public static final int LENGTH_LONG =  Toast.LENGTH_LONG;
	public static final int LENGTH_SHORT =  Toast.LENGTH_SHORT;
	private static ToastInTime instance = null;
	
	private View v;
    private Toast it;
    
    public ToastInTime(Context context) {
		init(context);
	}
    
    public static ToastInTime getInstance(Context context){ 
    	if(instance == null){
    		instance = new ToastInTime(context);
    	}
    	return instance;
    }
    
    public ToastInTime() {
	}
    
    /**
     * �˷�����Ҫ���е���init������ʼ��
     * 
     * */
    public static ToastInTime getInstance(){ 
    	if(instance == null){
    		instance = new ToastInTime();
    	}
    	return instance;
    }
   
    public void init(Context context) {
        v = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        it = new Toast(context);
        it.setView(v);
    }
   
    /**
     * ��ʾString
     * */
    public void display(String text, int duration) {
        it.setText(text);
        it.setDuration(duration);
        it.show();
    }

   /**
    *  ��ʾResources String
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
