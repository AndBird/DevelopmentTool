package com.android_development.devicetool;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/4/11.
 * ����: ��Ļ������
 */
public class ScreenTool {
    private final static  String TAG = ScreenTool.class.getName();

    /**
     * pxתdp
     * */
    public static int convertPxToDp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float logicalDensity = metrics.density;
        int dp = Math.round(px / logicalDensity);
        return dp;
    }

    /**
     * dpתpx
     * */
    public static int convertDpToPx(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }
    
    /**������Ļ�ֱ���
     * 
     * @return int[2]{screenWidth, screenHeigth}
     * */
    public static int[] getScreenSize(Activity activity){
//           Display display = activity.getWindowManager().getDefaultDisplay();
//           int screenWidth = display.getWidth();
//           int screenHeight = display.getHeight();
//           return new int[]{screenWidth, screenHeight};

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        return new int[]{screenWidth, screenHeight};
    }

    /**������Ļ�ֱ���
     * 
     * @return int[2]{screenWidth, screenHeigth}
     * */
    public static int[] getScreenSize(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;
        return new int[]{screenWidth, screenHeight};
    }

    /**
     * spתpx
     */
    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
    
    /**
     * pxתsp
     */
    public static float px2sp(Context context, float px) {
        return (px / context.getResources().getDisplayMetrics().scaledDensity);
    }
    
    
    /**
     * spתdp
     */
    public static int sp2dp(Context context, int sp) {
    	int px = sp2px(context, sp);
    	return convertPxToDp(context, px);
    }
    
    /**
     * dpתsp
     */
    public static float dp2sp(Context context, int dp) {
    	int px = convertDpToPx(context, dp);
    	return px2sp(context, px);
    }

//    public static int dip2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }
//
//    public static int px2dip(Context context, float pxValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (pxValue / scale + 0.5f);
//    }
    
    /**
     * ��ȡ��ǰ���ڵ���ת�Ƕ�
     *
     * @param activity activity
     * @return  int
     */
    public static int getDisplayRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    
    /**
     * ��ǰ�Ƿ��Ǻ���
     *
     * @param context  context
     * @return  boolean
     */
    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    
    /**
     * ��ǰ�Ƿ�������
     *
     * @param context  context
     * @return   boolean
     */
    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    
    
    /**
     *  �������ڵ�͸����  1.0f,0.5f �䰵
     * @param from  from>=0&&from<=1.0f
     * @param to  to>=0&&to<=1.0f
     * @param context  ��ǰ��activity
     */
    public static void dimBackground(final float from, final float to, Activity context) {
        final Window window = context.getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        WindowManager.LayoutParams params = window.getAttributes();
                        params.alpha = (Float) animation.getAnimatedValue();
                        window.setAttributes(params);
                    }
                });
        valueAnimator.start();
    }
    
    //��ȡ״̬���߶�
    public static int getStatusBarHeight(Context context) {  
        int result = 0;  
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");  
        if (resourceId > 0) {  
            result = context.getResources().getDimensionPixelSize(resourceId);  
        }  
        return result;  
	 }
    
    //��ȡ״̬���߶�(activity��������Ҫ�Ƴ�һ��ʱ����ܻ�ȡ��)
    public static int getStatusBarHeight(Activity activity){
	    Rect frames = new Rect();  
	    View views =  activity.getWindow().getDecorView();  
	    views.buildDrawingCache();  
	    views.getWindowVisibleDisplayFrame(frames);  
	    int statusBarHeights = frames.top;  
	    return statusBarHeights;
    }
    
    /**
     * ����view������߶�(�������������߶�)
     * 
     * ����Ļ���Ϊ�����׼
     * 
     * */
    public static int autoFitViewHeightBaseScreenWidth(Context context, int height, int baseScreenWidth){
    	int[] size = getScreenSize(context);
    	float xScale = size[0] * 1.0f / baseScreenWidth;
    	if(xScale > 0){
    		height = (int) (height * xScale);
    	}
    	return height;
    }
    
    /**
     * ����view������߶�(�������������߶�)����ֱ����Ч
     * 
     * ����Ļ���Ϊ�����׼
     * 
     * */
    public static void autoFitViewHeightBaseScreenWidth(Context context, View v, int baseScreenWidth){
    	if(v != null){
	    	int height = autoFitViewHeightBaseScreenWidth(context, v.getMeasuredHeight(), baseScreenWidth);
	    	ViewGroup.LayoutParams lp = v.getLayoutParams();
	    	lp.height = height;
	    	v.setLayoutParams(lp);
    	}
    }
    
    /**
     * ����view������ߴ�(��߶���Ҫ����)
     * 
     * ����Ļ��Ⱥ͸߶�Ϊ�����׼��ȡ��С���ű�
     * 
     * @param viewSize : view �Ĵ�С
     * */
    public static void autoFitViewHeightBaseScreenSize(Context context, int[] viewSize, int baseScreenWidth, int baseScreenHeight){
    	int[] size = getScreenSize(context);
    	float xScale = size[0] * 1.0f / baseScreenWidth;
    	float yScale = size[1] * 1.0f / baseScreenHeight;
    	float fitScale = xScale > yScale ? yScale : xScale;
    	if(fitScale > 0){
    		viewSize[0] = (int) (viewSize[0] * fitScale);
    		viewSize[1] = (int) (viewSize[1] * fitScale);
    	}
    }
    
    /**
     * ����view������ߴ�(��߶���Ҫ����)
     * 
     * ����Ļ��Ⱥ͸߶�Ϊ�����׼��ȡ��С���ű�
     * 
     * @return int[2]{�����Ŀ�ȣ� �����߶�}
     * */
    public static int[] autoFitViewHeightBaseScreenSize(Context context, int width, int height, int baseScreenWidth, int baseScreenHeight){
    	int[] size = getScreenSize(context);
    	float xScale = size[0] * 1.0f / baseScreenWidth;
    	float yScale = size[1] * 1.0f / baseScreenHeight;
    	float fitScale = xScale > yScale ? yScale : xScale;
    	if(fitScale > 0){
    		width = (int) (width * fitScale);
    		height = (int) (height * fitScale);
    	}
    	return new int[]{width, height};
    }
    
    /**
     * ����view�ĳߴ�(��߶���Ҫ����),��ֱ����Ч
     * 
     * ����Ļ��Ⱥ͸߶�Ϊ�����׼��ȡ��С���ű�
     * 
     * */
    public static void autoFitViewHeightBaseScreenSize(Context context, View v, int baseScreenWidth, int baseScreenHeight){
    	if(v != null){
    		int[] size = new int[]{v.getMeasuredWidth(), v.getMeasuredHeight()};
	    	autoFitViewHeightBaseScreenSize(context, size, baseScreenWidth, baseScreenHeight);
	    	ViewGroup.LayoutParams lp = v.getLayoutParams();
	    	lp.width = size[0];
	    	lp.height = size[1];
	    	v.setLayoutParams(lp);
    	}
    }
}
