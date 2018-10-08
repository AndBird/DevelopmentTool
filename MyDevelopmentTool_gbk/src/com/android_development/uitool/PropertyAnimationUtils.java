package com.android_development.uitool;


import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

/**属性动画合集*/
public class PropertyAnimationUtils {

	/**透明度属性动画(透明度由0～1表示。0表示完全透明，1表示不透明)
	 * 
	 * @param view ： 目标view
	 * @param fromAlpha :  起始透明度
	 * @param endAlpha ： 结束透明度
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator alphaAnim(View view, float fromAlpha, float endAlpha, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, endAlpha);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**透明度属性动画(透明度由0～1表示。0表示完全透明，1表示不透明)
	 * 
	 * @param view ： 目标view
	 * @param fromAlpha :  起始透明度
	 * @param toAlpha ：中间透明度
	 * @param endAlpha : 结束透明度
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator alphaAnim(View view, float fromAlpha, float toAlpha, float endAlpha, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha, endAlpha);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**透明度属性动画(透明度由0～1表示。0表示完全透明，1表示不透明)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param alpha : 透明度变化
	 * 
	 * */
	public static ObjectAnimator alphaAnim(View view, int duration, float... alpha){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", alpha);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	/**旋转属性动画(下个度数大于上个度数，顺时针旋转；下个度数小于上个度数，逆时针旋转)
	 * 
	 * @param view ： 目标view
	 * @param fromDegrees :  起始角度
	 * @param endDegrees ： 结束角度
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator rotateAnim(View view, float fromDegrees, float endDegrees, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", fromDegrees, endDegrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**旋转属性动画(下个度数大于上个度数，顺时针旋转；下个度数小于上个度数，逆时针旋转)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param degrees : 旋转角度
	 * 
	 * */
	public static ObjectAnimator rotateAnim(View view, int duration, float... degrees){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", degrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**旋转属性动画(下个度数大于上个度数，顺时针旋转；下个度数小于上个度数，逆时针旋转)
	 * 
	 * @param view ： 目标view
	 * @param fromDegrees :  起始角度
	 * @param endDegrees ： 结束角度
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator rotateXAnim(View view, float fromDegrees, float endDegrees, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", fromDegrees, endDegrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**旋转属性动画(下个度数大于上个度数，顺时针旋转；下个度数小于上个度数，逆时针旋转)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param degrees : 旋转角度
	 * 
	 * */
	public static ObjectAnimator rotateXAnim(View view, int duration, float... degrees){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationX", degrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	/**旋转属性动画(下个度数大于上个度数，顺时针旋转；下个度数小于上个度数，逆时针旋转)
	 * 
	 * @param view ： 目标view
	 * @param fromDegrees :  起始角度
	 * @param endDegrees ： 结束角度
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator rotateYAnim(View view, float fromDegrees, float endDegrees, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", fromDegrees, endDegrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**旋转属性动画(下个度数大于上个度数，顺时针旋转；下个度数小于上个度数，逆时针旋转)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param degrees : 旋转角度
	 * 
	 * */
	public static ObjectAnimator rotateYAnim(View view, int duration, float... degrees){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotationY", degrees);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**移动属性动画(translationX：下个位置大于上个上个位置时，向右移动，反之向左移动；translationY：下个位置大于上个上个位置时，向下移动，反之向上移动)
	 * 
	 * @param view ： 目标view
	 * @param fromX :  起始X位置
	 * @param endX ： 结束X位置
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator translateXAnim(View view, float fromX, float endX, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", fromX, endX);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**移动属性动画(translationX：下个位置大于上个上个位置时，向右移动，反之向左移动；translationY：下个位置大于上个上个位置时，向下移动，反之向上移动)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param values : X移动位置
	 * 
	 * */
	public static ObjectAnimator translateXAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**移动属性动画(translationX：下个位置大于上个上个位置时，向右移动，反之向左移动；translationY：下个位置大于上个上个位置时，向下移动，反之向上移动)
	 * 
	 * @param view ： 目标view
	 * @param fromY :  起始Y位置
	 * @param endY ： 结束Y位置
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator translateYAnim(View view, float fromY, float endY, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", fromY, endY);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**移动属性动画(translationX：下个位置大于上个上个位置时，向右移动，反之向左移动；translationY：下个位置大于上个上个位置时，向下移动，反之向上移动)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param values : Y移动位置
	 * 
	 * */
	public static ObjectAnimator translateYAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	/**缩放属性动画(scaleX scaleY：1f表示原来的大小，以此推类：2f表示两倍、3f表示三倍)
	 * 
	 * @param view ： 目标view
	 * @param fromScaleX :  缩放x比例
	 * @param endScaleX ： 缩放x比例
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator scaleXAnim(View view, float fromScaleX, float endScaleX, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", fromScaleX, endScaleX);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**缩放属性动画(scaleX scaleY：1f表示原来的大小，以此推类：2f表示两倍、3f表示三倍)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param values : 缩放x比例
	 * 
	 * */
	public static ObjectAnimator scaleXAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleX", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**缩放属性动画(scaleX scaleY：1f表示原来的大小，以此推类：2f表示两倍、3f表示三倍)
	 * 
	 * @param view ： 目标view
	 * @param fromScaleY :  缩放Y比例
	 * @param endScaleY ： 缩放Y比例
	 * @param duration ： 动画持续时间，单位毫秒
	 * 
	 * */
	public static ObjectAnimator scaleYAnim(View view, float fromScaleX, float endScaleX, int duration){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", fromScaleX, endScaleX);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	
	
	/**缩放属性动画(scaleX scaleY：1f表示原来的大小，以此推类：2f表示两倍、3f表示三倍)
	 * 
	 * @param view ： 目标view
	 * @param duration ： 动画持续时间，单位毫秒
	 * @param values : 缩放Y比例
	 * 
	 * */
	public static ObjectAnimator scaleYAnim(View view, int duration, float... values){
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "scaleY", values);
		animator.setDuration(duration);
		//animator.start();
		return animator;
	}
	
	/**改变view宽度的动画
	 * 
	 * @param startWidth : 起始宽度
	 * @param endWidth ： 结束宽度
	 * @param duration : 动画时间，单位毫秒
	 * */
	public static void changeViewWidth(final View view, final int startWidth, final int endWidth, final int duration){
		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);  
		  
	    valueAnimator.addUpdateListener(new AnimatorUpdateListener() {  
	        //持有一个IntEvaluator对象，方便下面估值的时候使用  
	        private IntEvaluator mEvaluator = new IntEvaluator();  
	  
	        @Override  
	        public void onAnimationUpdate(ValueAnimator animator) {  
	            //获得当前动画的进度值，整型，1-100之间  
	            int currentValue = (Integer)animator.getAnimatedValue();  
	           //Log.e(TAG, "performAnimte current value: " + currentValue);  
	  
	            //计算当前进度占整个动画过程的比例，浮点型，0-1之间  
	            float fraction = currentValue / 100f;  
	  
	            //这里我偷懒了，不过有现成的干吗不用呢  
	            //直接调用整型估值器通过比例计算出宽度，然后再设给Button  
	            view.getLayoutParams().width = mEvaluator.evaluate(fraction, startWidth, endWidth);  
	            view.requestLayout();  
	        }  
	    });  
	    valueAnimator.setDuration(duration).start();  
	}
}
