package com.android_development.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class BaseGridView extends GridView{

	/**���ڿ���gridView��adapter��getView��ε�������
	 * 
	 * 
	 *  //gridview���ε���getView
	 *  public View getView(final int pos, View convertView, ViewGroup parent) {
	 * 		if(gridView.isOnMeasure){
 *		            //�����onMeasure���õľ���������
 *		            return convertView;
	 *		}
	 *		//else����ʼ��item
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
            return true;  //��ֹGridView����
        }*/
        return super.dispatchTouchEvent(ev);
    }
}
