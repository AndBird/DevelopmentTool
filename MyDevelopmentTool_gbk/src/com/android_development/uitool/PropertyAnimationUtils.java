package com.android_development.uitool;


import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

/**���Զ����ϼ�*/
public class PropertyAnimationUtils {

	/**͸�������Զ���(͸������0��1��ʾ��0��ʾ��ȫ͸����1��ʾ��͸��)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromAlpha :  ��ʼ͸����
	 * @param endAlpha �� ����͸����
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator alphaAnim(View view, float fromAlpha, float endAlpha, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, endAlpha);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**͸�������Զ���(͸������0��1��ʾ��0��ʾ��ȫ͸����1��ʾ��͸��)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromAlpha :  ��ʼ͸����
	 * @param toAlpha ���м�͸����
	 * @param endAlpha : ����͸����
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator alphaAnim(View view, float fromAlpha, float toAlpha, float endAlpha, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha, endAlpha);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**͸�������Զ���(͸������0��1��ʾ��0��ʾ��ȫ͸����1��ʾ��͸��)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param alpha : ͸���ȱ仯
	 * 
	 * */
	public static ObjectAnimator alphaAnim(View view, int duration, float... alpha){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", alpha);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	/**��ת���Զ���(�¸����������ϸ�������˳ʱ����ת���¸�����С���ϸ���������ʱ����ת)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromDegrees :  ��ʼ�Ƕ�
	 * @param endDegrees �� �����Ƕ�
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator rotateAnim(View view, float fromDegrees, float endDegrees, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", fromDegrees, endDegrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**��ת���Զ���(�¸����������ϸ�������˳ʱ����ת���¸�����С���ϸ���������ʱ����ת)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param degrees : ��ת�Ƕ�
	 * 
	 * */
	public static ObjectAnimator rotateAnim(View view, int duration, float... degrees){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", degrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**��ת���Զ���(�¸����������ϸ�������˳ʱ����ת���¸�����С���ϸ���������ʱ����ת)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromDegrees :  ��ʼ�Ƕ�
	 * @param endDegrees �� �����Ƕ�
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator rotateXAnim(View view, float fromDegrees, float endDegrees, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", fromDegrees, endDegrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**��ת���Զ���(�¸����������ϸ�������˳ʱ����ת���¸�����С���ϸ���������ʱ����ת)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param degrees : ��ת�Ƕ�
	 * 
	 * */
	public static ObjectAnimator rotateXAnim(View view, int duration, float... degrees){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", degrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	/**��ת���Զ���(�¸����������ϸ�������˳ʱ����ת���¸�����С���ϸ���������ʱ����ת)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromDegrees :  ��ʼ�Ƕ�
	 * @param endDegrees �� �����Ƕ�
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator rotateYAnim(View view, float fromDegrees, float endDegrees, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", fromDegrees, endDegrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**��ת���Զ���(�¸����������ϸ�������˳ʱ����ת���¸�����С���ϸ���������ʱ����ת)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param degrees : ��ת�Ƕ�
	 * 
	 * */
	public static ObjectAnimator rotateYAnim(View view, int duration, float... degrees){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", degrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**�ƶ����Զ���(translationX���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ���translationY���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ�)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromX :  ��ʼXλ��
	 * @param endX �� ����Xλ��
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator translateXAnim(View view, float fromX, float endX, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", fromX, endX);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**�ƶ����Զ���(translationX���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ���translationY���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ�)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param values : X�ƶ�λ��
	 * 
	 * */
	public static ObjectAnimator translateXAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**�ƶ����Զ���(translationX���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ���translationY���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ�)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromY :  ��ʼYλ��
	 * @param endY �� ����Yλ��
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator translateYAnim(View view, float fromY, float endY, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", fromY, endY);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**�ƶ����Զ���(translationX���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ���translationY���¸�λ�ô����ϸ��ϸ�λ��ʱ�������ƶ�����֮�����ƶ�)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param values : Y�ƶ�λ��
	 * 
	 * */
	public static ObjectAnimator translateYAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	/**�������Զ���(scaleX scaleY��1f��ʾԭ���Ĵ�С���Դ����ࣺ2f��ʾ������3f��ʾ����)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromScaleX :  ����x����
	 * @param endScaleX �� ����x����
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator scaleXAnim(View view, float fromScaleX, float endScaleX, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", fromScaleX, endScaleX);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**�������Զ���(scaleX scaleY��1f��ʾԭ���Ĵ�С���Դ����ࣺ2f��ʾ������3f��ʾ����)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param values : ����x����
	 * 
	 * */
	public static ObjectAnimator scaleXAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**�������Զ���(scaleX scaleY��1f��ʾԭ���Ĵ�С���Դ����ࣺ2f��ʾ������3f��ʾ����)
	 * 
	 * @param view �� Ŀ��view
	 * @param fromScaleY :  ����Y����
	 * @param endScaleY �� ����Y����
	 * @param duration �� ��������ʱ�䣬��λ����
	 * 
	 * */
	public static ObjectAnimator scaleYAnim(View view, float fromScaleX, float endScaleX, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", fromScaleX, endScaleX);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**�������Զ���(scaleX scaleY��1f��ʾԭ���Ĵ�С���Դ����ࣺ2f��ʾ������3f��ʾ����)
	 * 
	 * @param view �� Ŀ��view
	 * @param duration �� ��������ʱ�䣬��λ����
	 * @param values : ����Y����
	 * 
	 * */
	public static ObjectAnimator scaleYAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**�ı�view��ȵĶ���
	 * 
	 * @param startWidth : ��ʼ���
	 * @param endWidth �� �������
	 * @param duration : ����ʱ�䣬��λ����
	 * */
	public static void changeViewWidth(final View view, final int startWidth, final int endWidth, final int duration){
		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);  
		  
	    valueAnimator.addUpdateListener(new AnimatorUpdateListener() {  
	        //����һ��IntEvaluator���󣬷��������ֵ��ʱ��ʹ��  
	        private IntEvaluator mEvaluator = new IntEvaluator();  
	  
	        @Override  
	        public void onAnimationUpdate(ValueAnimator animator) {  
	            //��õ�ǰ�����Ľ���ֵ�����ͣ�1-100֮��  
	            int currentValue = (Integer)animator.getAnimatedValue();  
	           //Log.e(TAG, "performAnimte current value: " + currentValue);  
	  
	            //���㵱ǰ����ռ�����������̵ı����������ͣ�0-1֮��  
	            float fraction = currentValue / 100f;  
	  
	            //������͵���ˣ��������ֳɵĸ�������  
	            //ֱ�ӵ������͹�ֵ��ͨ�������������ȣ�Ȼ�������Button  
	            view.getLayoutParams().width = mEvaluator.evaluate(fraction, startWidth, endWidth);  
	            view.requestLayout();  
	        }  
	    });  
	    valueAnimator.setDuration(duration).start();  
	}
}
