package com.android_development.uitool;

import android.app.Activity;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class ActivityTool {
	public static boolean activityIsFinish(Activity activity){
		try {
			if(activity == null){
				return true;
			}
            if(Build.VERSION.SDK_INT < 17){
                if(activity.isFinishing()){// WelecomeActivity.this.isDestroyed()
                    return true;
                }else{
                	return false;
                }
            }else{
                if(activity.isFinishing() || activity.isDestroyed()){
                    return true;
                }else{
                	return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return true;
	}
	
	//设置字符串的颜色
	public static SpannableStringBuilder setStringColor(String text, int startIndex, int newColor){
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(new ForegroundColorSpan(newColor), startIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}
}
