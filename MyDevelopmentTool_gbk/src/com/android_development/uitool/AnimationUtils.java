package com.android_development.uitool;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * ����: ����������
 * */
public class AnimationUtils {
	
	/**
     * Don't let anyone instantiate this class.
     */
    private AnimationUtils() {
        throw new Error("Do not need instantiate!");
    }


    /**
     * Ĭ�϶�������ʱ��
     */
    public static final long DEFAULT_ANIMATION_DURATION = 400;


    /**
     * ��ȡһ����ת����
     *
     * @param fromDegrees       ��ʼ�Ƕ�
     * @param toDegrees         �����Ƕ�
     * @param pivotXType        ��ת���ĵ�X�������������
     * @param pivotXValue       ��ת���ĵ�X������
     * @param pivotYType        ��ת���ĵ�Y�������������
     * @param pivotYValue       ��ת���ĵ�Y������
     * @param durationMillis    ����ʱ��
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
     * @param durationMillis    ��������ʱ��
     * @param animationListener ����������
     * @return һ���������ĵ���ת�Ķ���
     */
    public static RotateAnimation getRotateAnimationByCenter(long durationMillis, AnimationListener animationListener) {
        return getRotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, durationMillis,
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
     * ��ȡһ��������ͼ�������ĵ���ת�Ķ���
     *
     * @param animationListener ����������
     * @return һ���������ĵ���ת�Ķ���
     */
    public static RotateAnimation getRotateAnimationByCenter(AnimationListener animationListener) {
        return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION,
                animationListener);
    }


    /**
     * ��ȡһ���������ĵ���ת�Ķ���
     *
     * @return һ���������ĵ���ת�Ķ�����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static RotateAnimation getRotateAnimationByCenter() {
        return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION, null);
    }


    /**
     * ��ȡһ��͸���Ƚ��䶯��
     *
     * @param fromAlpha         ��ʼʱ��͸����
     * @param toAlpha           ����ʱ��͸���ȶ�
     * @param durationMillis    ����ʱ��
     * @param animationListener ����������
     * @return һ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis, AnimationListener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(durationMillis);
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
     * @param durationMillis ����ʱ��
     * @return һ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
        return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, null);
    }


    /**
     * ��ȡһ��͸���Ƚ��䶯��
     *
     * @param fromAlpha         ��ʼʱ��͸����
     * @param toAlpha           ����ʱ��͸���ȶ�
     * @param animationListener ����������
     * @return һ��͸���Ƚ��䶯����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, AnimationListener animationListener) {
        return getAlphaAnimation(fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION,
                animationListener);
    }


    /**
     * ��ȡһ��͸���Ƚ��䶯��
     *
     * @param fromAlpha ��ʼʱ��͸����
     * @param toAlpha   ����ʱ��͸���ȶ�
     * @return һ��͸���Ƚ��䶯����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha) {
        return getAlphaAnimation(fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION,
                null);
    }


    /**
     * ��ȡһ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     *
     * @param durationMillis    ����ʱ��
     * @param animationListener ����������
     * @return һ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     */
    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis, AnimationListener animationListener) {
        return getAlphaAnimation(1.0f, 0.0f, durationMillis, animationListener);
    }


    /**
     * ��ȡһ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     *
     * @param durationMillis ����ʱ��
     * @return һ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     */
    public static AlphaAnimation getHiddenAlphaAnimation(long durationMillis) {
        return getHiddenAlphaAnimation(durationMillis, null);
    }


    /**
     * ��ȡһ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     *
     * @param animationListener ����������
     * @return һ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static AlphaAnimation getHiddenAlphaAnimation(AnimationListener animationListener) {
        return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION,
                animationListener);
    }


    /**
     * ��ȡһ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯��
     *
     * @return һ������ȫ��ʾ��Ϊ���ɼ���͸���Ƚ��䶯����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static AlphaAnimation getHiddenAlphaAnimation() {
        return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, null);
    }


    /**
     * ��ȡһ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     *
     * @param durationMillis    ����ʱ��
     * @param animationListener ����������
     * @return һ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getShowAlphaAnimation(long durationMillis, AnimationListener animationListener) {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, animationListener);
    }


    /**
     * ��ȡһ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     *
     * @param durationMillis ����ʱ��
     * @return һ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     */
    public static AlphaAnimation getShowAlphaAnimation(long durationMillis) {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, null);
    }


    /**
     * ��ȡһ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     *
     * @param animationListener ����������
     * @return һ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static AlphaAnimation getShowAlphaAnimation(AnimationListener animationListener) {
        return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION,
                animationListener);
    }


    /**
     * ��ȡһ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯��
     *
     * @return һ���ɲ��ɼ���Ϊ��ȫ��ʾ��͸���Ƚ��䶯����Ĭ�ϳ���ʱ��ΪDEFAULT_ANIMATION_DURATION
     */
    public static AlphaAnimation getShowAlphaAnimation() {
        return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, null);
    }


    /**
     * ��ȡһ����С����
     *
     * @param durationMillis   ʱ��
     * @param animationListener  ����
     * @return һ����С����
     */
    public static ScaleAnimation getLessenScaleAnimation(long durationMillis, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                0.0f, ScaleAnimation.RELATIVE_TO_SELF,
                ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);

        return scaleAnimation;
    }


    /**
     * ��ȡһ����С����
     *
     * @param durationMillis ʱ��
     * @return һ����С����
     */
    public static ScaleAnimation getLessenScaleAnimation(long durationMillis) {
        return getLessenScaleAnimation(durationMillis, null);

    }


    /**
     * ��ȡһ����С����
     *
     * @param animationListener  ����
     * @return ����һ����С�Ķ���
     */
    public static ScaleAnimation getLessenScaleAnimation(AnimationListener animationListener) {
        return getLessenScaleAnimation(DEFAULT_ANIMATION_DURATION,
                animationListener);

    }


    /**
     * ��ȡһ���Ŵ󶯻�
     * @param durationMillis   ʱ��
     * @param animationListener  ����
     *
     * @return ����һ���Ŵ��Ч��
     */
    public static ScaleAnimation getAmplificationAnimation(long durationMillis, AnimationListener animationListener) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
                1.0f, ScaleAnimation.RELATIVE_TO_SELF,
                ScaleAnimation.RELATIVE_TO_SELF);
        scaleAnimation.setDuration(durationMillis);
        scaleAnimation.setAnimationListener(animationListener);
        return scaleAnimation;
    }


    /**
     * ��ȡһ���Ŵ󶯻�
     *
     * @param durationMillis   ʱ��
     *
     * @return ����һ���Ŵ��Ч��
     */
    public static ScaleAnimation getAmplificationAnimation(long durationMillis) {
        return getAmplificationAnimation(durationMillis, null);

    }


    /**
     * ��ȡһ���Ŵ󶯻�
     *
     * @param animationListener  ����
     * @return ����һ���Ŵ��Ч��
     */
    public static ScaleAnimation getAmplificationAnimation(AnimationListener animationListener) {
        return getAmplificationAnimation(DEFAULT_ANIMATION_DURATION,
                animationListener);

    }
}
