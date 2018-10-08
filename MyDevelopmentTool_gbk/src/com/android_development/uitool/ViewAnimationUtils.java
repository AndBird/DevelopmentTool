package com.android_development.uitool;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 功能: 视图(补间)动画工具类
 * */
public class ViewAnimationUtils {

    /**
     * 获取一个旋转动画
     *
     * @param fromDegrees       开始角度
     * @param toDegrees         结束角度
     * @param pivotXType        旋转中心点X轴坐标相对类型
     * @param pivotXValue       旋转中心点X轴坐标
     * @param pivotYType        旋转中心点Y轴坐标相对类型
     * @param pivotYValue       旋转中心点Y轴坐标
     * @param duration :   时间,毫秒
     * @param animationListener 动画监听器
     * @return 一个旋转动画
     */
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long duration, AnimationListener animationListener) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,
                toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        rotateAnimation.setDuration(duration);
        if (animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener);
        }
        return rotateAnimation;
    }

    /**
     * 获取一个旋转动画(以左上角为参照点)
     *
     * @param fromDegrees       开始角度
     * @param toDegrees         结束角度
     * @param duration :   时间,毫秒
     * @param animationListener 动画监听器
     * @return 一个旋转动画
     */
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, long duration, AnimationListener animationListener) {
       return getRotateAnimation(fromDegrees, toDegrees, 0, 0, duration, animationListener);
    }
    
    /**
     * 获取一个旋转动画(以左上角为参照点)
     *
     * @param fromDegrees  开始角度
     * @param toDegrees    结束角度
     * @param pivotXValue  旋转中心点X轴坐标
     * @param pivotYValue  旋转中心点Y轴坐标
     * @param duration :   时间,毫秒
     * @param animationListener 动画监听器
     * @return 一个旋转动画
     */
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, float pivotXValue, float pivotYValue, long duration, AnimationListener animationListener) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, pivotXValue, pivotYValue);
        rotateAnimation.setDuration(duration);
        if (animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener);
        }
        return rotateAnimation;
    }


    /**
     * 获取一个根据视图自身中心点旋转的动画
     *
     * @param fromDegrees       开始角度
     * @param toDegrees         结束角度
     * @param duration :   时间,毫秒
     * @param animationListener 动画监听器
     * @return 一个根据中心点旋转的动画
     */
    public static RotateAnimation getRotateAnimationByCenter(float fromDegrees, float toDegrees, long duration, AnimationListener animationListener) {
        //(0.5f表示相对与原图的0.5倍)
    	return getRotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, duration,
                animationListener);
    }


    /**
     * 获取一个根据中心点旋转的动画
     *
     * @param fromDegrees       开始角度
     * @param toDegrees         结束角度
     * @param duration 动画持续时间
     * @return 一个根据中心点旋转的动画
     */
    public static RotateAnimation getRotateAnimationByCenter(float fromDegrees, float toDegrees, long duration) {
        return getRotateAnimationByCenter(fromDegrees, toDegrees, duration, null);
    }


    /**
     * 获取一个透明度渐变动画(0.0为全透明，1.0为不透明，默认为1.0)
     *
     * @param fromAlpha         开始时的透明度
     * @param toAlpha           结束时的透明度都
     * @param duration :   时间,毫秒
     * @param animationListener 动画监听器
     * @return 一个透明度渐变动画
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long duration, AnimationListener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(duration);
        if (animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener);
        }
        return alphaAnimation;
    }


    /**
     * 获取一个透明度渐变动画(0.0为全透明，1.0为不透明，默认为1.0)
     *
     * @param fromAlpha      开始时的透明度
     * @param toAlpha        结束时的透明度都
     * @param duration :   时间,毫秒
     * @return 一个透明度渐变动画
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long duration) {
        return getAlphaAnimation(fromAlpha, toAlpha, duration, null);
    }

    /**
     * 获取一个缩放动画(0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     *
     * @param fromScaleX :  缩放X比例
	 * @param endScaleX ： 缩放X比例
     * @param fromScaleY :  缩放Y比例
	 * @param endScaleY ： 缩放Y比例
     * @param duration :   时间,毫秒
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleAnimation(float fromScaleX, float endScaleX, float fromScaleY, float endScaleY, float pivotX, float pivotY, long duration, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromScaleX,endScaleX, fromScaleY, endScaleY,
                pivotX, pivotY);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    /**
     * 获取一个缩放动画(参照自己)
     * 
     * (0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     *
     * @param fromScaleX :  缩放X比例
	 * @param endScaleX ： 缩放X比例
     * @param fromScaleY :  缩放Y比例
	 * @param endScaleY ： 缩放Y比例
     * @param duration :   时间,毫秒
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleAnimationBySelf(float fromScaleX, float endScaleX, float fromScaleY, float endScaleY, long duration, AnimationListener animationListener) {
       return getScaleAnimation(fromScaleX, endScaleX, fromScaleY, endScaleY, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, duration, animationListener);
    }

    /**
     * 获取一个缩放动画(参照自己)
     * (0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     * @param fromScaleX :  缩放X比例
	 * @param endScaleX ： 缩放X比例
     * @param duration :   时间,毫秒
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleXAnimationBySelf(float fromScaleX, float endScaleX, long duration) {
        return getScaleAnimationBySelf(fromScaleX, endScaleX, 1, 1, duration, null);

    }
    
    /**
     * 获取一个缩放动画(参照自己)
     * (0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     * @param fromScaleY :  缩放Y比例
	 * @param endScaleY ： 缩放Y比例
     * @param duration :   时间,毫秒
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleYAnimationBySelf(float fromScaleY, float endScaleY, long duration) {
        return getScaleAnimationBySelf(1, 1, fromScaleY, endScaleY, duration, null);
    }
    
    /**
     * 获取一个缩放动画(参照parent)
     * (0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     *
     * @param fromScaleX :  缩放X比例
	 * @param endScaleX ： 缩放X比例
     * @param fromScaleY :  缩放Y比例
	 * @param endScaleY ： 缩放Y比例
     * @param duration :   时间,毫秒
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleAnimationByParent(float fromScaleX, float endScaleX, float fromScaleY, float endScaleY, long duration, AnimationListener animationListener) {
       return getScaleAnimation(fromScaleX, endScaleX, fromScaleY, endScaleY, Animation.RELATIVE_TO_PARENT, Animation.RELATIVE_TO_PARENT, duration, animationListener);
    }

    /**
     * 获取一个缩放动画(参照parent)
     * (0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     * @param fromScaleX :  缩放X比例
	 * @param endScaleX ： 缩放X比例
     * @param duration :   时间,毫秒
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleXAnimationByParent(float fromScaleX, float endScaleX, long duration) {
        return getScaleAnimationByParent(fromScaleX, endScaleX, 1, 1, duration, null);

    }
    
    /**
     * 获取一个缩放动画(参照parent)
     * (0.0表示缩放到没有，1.0表示正常无缩放，小于1.0表示收缩，大于1.0表示放大)
     * @param fromScaleY :  缩放Y比例
	 * @param endScaleY ： 缩放Y比例
     * @param duration :   时间,毫秒
     * @return 一个缩小动画
     */
    public static ScaleAnimation getScaleYAnimationByParent(float fromScaleY, float endScaleY, long duration) {
        return getScaleAnimationByParent(1, 1, fromScaleY, endScaleY, duration, null);

    }
    
    /**
     * 获取一个偏移动画(图片左上角为参照点，移动绝对值)
     *
     * @param fromX :  x起点(绝对值)
	 * @param endX ： x终点
     * @param fromY :  y起点
	 * @param endY ： x终点
     * @param duration :   时间,毫秒
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    public static TranslateAnimation getTranslateAnimation(float fromX, float endX, float fromY, float endY, long duration, AnimationListener animationListener) {
    	TranslateAnimation animation = new TranslateAnimation(fromX, endX, fromY, endY);
        animation.setDuration(duration);
        animation.setAnimationListener(animationListener);
        return animation;
    }
    
    /**
     * 获取一个偏移动画(参照自己,移动百分比)
     *
     * @param fromX :  x起点(0.0 到1.0)
	 * @param endX ： x终点(0.0 到1.0)
     * @param fromY :  y起点(0.0 到1.0)
	 * @param endY ： x终点(0.0 到1.0)
     * @param duration :   时间,毫秒
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    public static TranslateAnimation getTranslateAnimationBySelf(float fromX, float endX, float fromY, float endY, long duration, AnimationListener animationListener) {
    	TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, 
    			Animation.RELATIVE_TO_SELF, endX,
    			Animation.RELATIVE_TO_SELF, fromY,
    			Animation.RELATIVE_TO_SELF, endY);
        animation.setDuration(duration);
        animation.setAnimationListener(animationListener);
        return animation;
    }
    
    /**
     * 获取一个偏移动画(参照Parent，移动百分比)
     * 
     * @param fromX :  x起点(0.0 到1.0)
	 * @param endX ： x终点(0.0 到1.0)
     * @param fromY :  y起点(0.0 到1.0)
	 * @param endY ： x终点(0.0 到1.0)
     * @param duration :   时间,毫秒
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    public static TranslateAnimation getTranslateAnimationByParent(float fromX, float endX, float fromY, float endY, long duration, AnimationListener animationListener) {
    	TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fromX,
    			Animation.RELATIVE_TO_PARENT, endX, 
    			Animation.RELATIVE_TO_PARENT, fromY,
    			Animation.RELATIVE_TO_PARENT, endY);
        animation.setDuration(duration);
        animation.setAnimationListener(animationListener);
        return animation;
    }
}
