package com.android_development.uitool;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

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
    public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, long durationMillis, AnimationListener animationListener) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,
                toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        rotateAnimation.setDuration(durationMillis);
        if (animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener);
        }
        return rotateAnimation;
    }


    /**
     * ��ȡһ��������ͼ�������ĵ���ת�Ķ���
     *
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ���������ĵ���ת�Ķ���
     */
    public static RotateAnimation getRotateAnimationByCenter(long duration, AnimationListener animationListener) {
        return getRotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, duration,
                animationListener);
    }


    /**
     * ��ȡһ���������ĵ���ת�Ķ���
     *
     * @param duration ��������ʱ��
     * @return һ���������ĵ���ת�Ķ���
     */
    public static RotateAnimation getRotateAnimationByCenter(long duration) {
        return getRotateAnimationByCenter(duration, null);
    }


    /**
     * ��ȡһ��͸���Ƚ��䶯��
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
     * ��ȡһ��͸���Ƚ��䶯��
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
     * ��ȡһ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     *
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     */
    public static AlphaAnimation getHiddenAlphaAnimation(long duration, AnimationListener animationListener) {
        return getAlphaAnimation(1.0f, 0.0f, duration, animationListener);
    }


    /**
     * ��ȡһ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     *
     * @param duration :   ʱ��,����
     * @return һ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     */
    public static AlphaAnimation getHiddenAlphaAnimation(long duration) {
        return getHiddenAlphaAnimation(duration, null);
    }


    /**
     * ��ȡһ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     *
     * @param duration :   ʱ��,����
     * @param animationListener ����������
     * @return һ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getShowAlphaAnimation(long duration, AnimationListener animationListener) {
        return getAlphaAnimation(0.0f, 1.0f, duration, animationListener);
    }


    /**
     * ��ȡһ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     *
     * @param duration :   ʱ��,����
     * @return һ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getShowAlphaAnimation(long duration) {
        return getAlphaAnimation(0.0f, 1.0f, duration, null);
    }

    /**
     * ��ȡһ����С����
     *
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     * @return һ����С����
     */
    public static ScaleAnimation getLessenScaleAnimation(long duration, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                0.0f, ScaleAnimation.RELATIVE_TO_SELF,
                ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setAnimationListener(animationListener);

        return scaleAnimation;
    }


    /**
     * ��ȡһ����С����
     *
     * @param duration :   ʱ��,����
     * @return һ����С����
     */
    public static ScaleAnimation getLessenScaleAnimation(long duration) {
        return getLessenScaleAnimation(duration, null);

    }


    /**
     * ��ȡһ���Ŵ󶯻�
     * @param duration :   ʱ��,����
     * @param animationListener  ����
     *
     * @return ����һ���Ŵ��Ч��
     */
    public static ScaleAnimation getAmplificationAnimation(long duration, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
                1.0f, ScaleAnimation.RELATIVE_TO_SELF,
                ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }


    /**
     * ��ȡһ���Ŵ󶯻�
     *
     * @param duration :   ʱ��,����
     *
     * @return ����һ���Ŵ��Ч��
     */
    public static ScaleAnimation getAmplificationAnimation(long duration) {
        return getAmplificationAnimation(duration, null);

    }
}
