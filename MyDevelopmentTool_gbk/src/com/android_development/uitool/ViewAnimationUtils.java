package com.android_development.uitool;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * ����: ��ͼ(����)����������
 * */
public class ViewAnimationUtils {

    /**
     * ��ȡһ����ת����
     *
     * @param fromDegrees       ��ʼ�Ƕ�
     * @param toDegrees         �����Ƕ�
     * @param pivotXType        ��ת���ĵ�X�������������
     * @param pivotXValue       ��ת���ĵ�X������
     * @param pivotYType        ��ת���ĵ�Y�������������
     * @param pivotYValue       ��ת���ĵ�Y������
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ����ת����
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
     * ��ȡһ����ת����(�����Ͻ�Ϊ���յ�)
     *
     * @param fromDegrees       ��ʼ�Ƕ�
     * @param toDegrees         �����Ƕ�
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ����ת����
     */
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, long duration, AnimationListener animationListener) {
       return getRotateAnimation(fromDegrees, toDegrees, 0, 0, duration, animationListener);
    }
    
    /**
     * ��ȡһ����ת����(�����Ͻ�Ϊ���յ�)
     *
     * @param fromDegrees  ��ʼ�Ƕ�
     * @param toDegrees    �����Ƕ�
     * @param pivotXValue  ��ת���ĵ�X������
     * @param pivotYValue  ��ת���ĵ�Y������
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ����ת����
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
     * ��ȡһ��������ͼ�������ĵ���ת�Ķ���
     *
     * @param fromDegrees       ��ʼ�Ƕ�
     * @param toDegrees         �����Ƕ�
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ���������ĵ���ת�Ķ���
     */
    public static RotateAnimation getRotateAnimationByCenter(float fromDegrees, float toDegrees, long duration, AnimationListener animationListener) {
        //(0.5f��ʾ�����ԭͼ��0.5��)
    	return getRotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, duration,
                animationListener);
    }


    /**
     * ��ȡһ���������ĵ���ת�Ķ���
     *
     * @param fromDegrees       ��ʼ�Ƕ�
     * @param toDegrees         �����Ƕ�
     * @param duration ��������ʱ��
     * @return һ���������ĵ���ת�Ķ���
     */
    public static RotateAnimation getRotateAnimationByCenter(float fromDegrees, float toDegrees, long duration) {
        return getRotateAnimationByCenter(fromDegrees, toDegrees, duration, null);
    }


    /**
     * ��ȡһ��͸���Ƚ��䶯��(0.0Ϊȫ͸����1.0Ϊ��͸����Ĭ��Ϊ1.0)
     *
     * @param fromAlpha         ��ʼʱ��͸����
     * @param toAlpha           ����ʱ��͸���ȶ�
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ��͸���Ƚ��䶯��
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
     * ��ȡһ��͸���Ƚ��䶯��(0.0Ϊȫ͸����1.0Ϊ��͸����Ĭ��Ϊ1.0)
     *
     * @param fromAlpha      ��ʼʱ��͸����
     * @param toAlpha        ����ʱ��͸���ȶ�
     * @param duration :   ʱ��,����
     * @return һ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long duration) {
        return getAlphaAnimation(fromAlpha, toAlpha, duration, null);
    }

    /**
     * ��ȡһ�����Ŷ���(0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     *
     * @param fromScaleX :  ����X����
	 * @param endScaleX �� ����X����
     * @param fromScaleY :  ����Y����
	 * @param endScaleY �� ����Y����
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleAnimation(float fromScaleX, float endScaleX, float fromScaleY, float endScaleY, float pivotX, float pivotY, long duration, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromScaleX,endScaleX, fromScaleY, endScaleY,
                pivotX, pivotY);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }

    /**
     * ��ȡһ�����Ŷ���(�����Լ�)
     * 
     * (0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     *
     * @param fromScaleX :  ����X����
	 * @param endScaleX �� ����X����
     * @param fromScaleY :  ����Y����
	 * @param endScaleY �� ����Y����
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleAnimationBySelf(float fromScaleX, float endScaleX, float fromScaleY, float endScaleY, long duration, AnimationListener animationListener) {
       return getScaleAnimation(fromScaleX, endScaleX, fromScaleY, endScaleY, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF, duration, animationListener);
    }

    /**
     * ��ȡһ�����Ŷ���(�����Լ�)
     * (0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     * @param fromScaleX :  ����X����
	 * @param endScaleX �� ����X����
     * @param duration :   ʱ��,����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleXAnimationBySelf(float fromScaleX, float endScaleX, long duration) {
        return getScaleAnimationBySelf(fromScaleX, endScaleX, 1, 1, duration, null);

    }
    
    /**
     * ��ȡһ�����Ŷ���(�����Լ�)
     * (0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     * @param fromScaleY :  ����Y����
	 * @param endScaleY �� ����Y����
     * @param duration :   ʱ��,����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleYAnimationBySelf(float fromScaleY, float endScaleY, long duration) {
        return getScaleAnimationBySelf(1, 1, fromScaleY, endScaleY, duration, null);
    }
    
    /**
     * ��ȡһ�����Ŷ���(����parent)
     * (0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     *
     * @param fromScaleX :  ����X����
	 * @param endScaleX �� ����X����
     * @param fromScaleY :  ����Y����
	 * @param endScaleY �� ����Y����
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleAnimationByParent(float fromScaleX, float endScaleX, float fromScaleY, float endScaleY, long duration, AnimationListener animationListener) {
       return getScaleAnimation(fromScaleX, endScaleX, fromScaleY, endScaleY, Animation.RELATIVE_TO_PARENT, Animation.RELATIVE_TO_PARENT, duration, animationListener);
    }

    /**
     * ��ȡһ�����Ŷ���(����parent)
     * (0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     * @param fromScaleX :  ����X����
	 * @param endScaleX �� ����X����
     * @param duration :   ʱ��,����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleXAnimationByParent(float fromScaleX, float endScaleX, long duration) {
        return getScaleAnimationByParent(fromScaleX, endScaleX, 1, 1, duration, null);

    }
    
    /**
     * ��ȡһ�����Ŷ���(����parent)
     * (0.0��ʾ���ŵ�û�У�1.0��ʾ���������ţ�С��1.0��ʾ����������1.0��ʾ�Ŵ�)
     * @param fromScaleY :  ����Y����
	 * @param endScaleY �� ����Y����
     * @param duration :   ʱ��,����
     * @return һ����С����
     */
    public static ScaleAnimation getScaleYAnimationByParent(float fromScaleY, float endScaleY, long duration) {
        return getScaleAnimationByParent(1, 1, fromScaleY, endScaleY, duration, null);

    }
    
    /**
     * ��ȡһ��ƫ�ƶ���(ͼƬ���Ͻ�Ϊ���յ㣬�ƶ�����ֵ)
     *
     * @param fromX :  x���(����ֵ)
	 * @param endX �� x�յ�
     * @param fromY :  y���
	 * @param endY �� x�յ�
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
     */
    public static TranslateAnimation getTranslateAnimation(float fromX, float endX, float fromY, float endY, long duration, AnimationListener animationListener) {
    	TranslateAnimation animation = new TranslateAnimation(fromX, endX, fromY, endY);
        animation.setDuration(duration);
        animation.setAnimationListener(animationListener);
        return animation;
    }
    
    /**
     * ��ȡһ��ƫ�ƶ���(�����Լ�,�ƶ��ٷֱ�)
     *
     * @param fromX :  x���(0.0 ��1.0)
	 * @param endX �� x�յ�(0.0 ��1.0)
     * @param fromY :  y���(0.0 ��1.0)
	 * @param endY �� x�յ�(0.0 ��1.0)
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
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
     * ��ȡһ��ƫ�ƶ���(����Parent���ƶ��ٷֱ�)
     * 
     * @param fromX :  x���(0.0 ��1.0)
	 * @param endX �� x�յ�(0.0 ��1.0)
     * @param fromY :  y���(0.0 ��1.0)
	 * @param endY �� x�յ�(0.0 ��1.0)
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
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
