package com.android_development.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class BaseGridView extends GridView{

	/**用于控制gridView的adapter的getView多次调用问题
	 * 
	 * 
	 *  //gridview会多次调用getView
	 *  public View getView(final int pos, View convertView, ViewGroup parent) {
	 * 		if(gridView.isOnMeasure){
 *		            //如果是onMeasure调用的就立即返回
 *		            return convertView;
	 *		}
	 *		//else，初始化item
	 * }
	 * */
	public boolean isOnMeasure;
	
	
	public BaseGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public BaseGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	public BaseGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		isOnMeasure = false;
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
       /*  if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;  //禁止GridView滑动
        }*/
        return super.dispatchTouchEvent(ev);
    }
}
