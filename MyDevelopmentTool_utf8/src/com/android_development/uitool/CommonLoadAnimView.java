package com.android_development.uitool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CommonLoadAnimView  extends FrameLayout{
    private Context mContext;

    private TextView loadNothingView;
    private TextView loadFailView;
    private ProgressBar progressBar;
    private OnClickListener listener;
    
    public CommonLoadAnimView(Context context){
        super(context);
        init(context);

    }
    public CommonLoadAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonLoadAnimView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        initView();
    }

    /*初始化时必须调用*/
    public void attachView(OnClickListener listener){
        try {
            this.listener = listener;
            this.loadFailView.setOnClickListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initView(){
    	try {
    		  this.loadNothingView = new TextView(this.mContext);
    		  FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    		  lp.gravity = Gravity.CENTER;
    		  this.loadNothingView.setLayoutParams(lp);
    		  this.loadNothingView.setGravity(Gravity.CENTER);
    		  this.loadNothingView.setText("这里什么也没有");
    		  
    		  this.loadFailView = new TextView(this.mContext);
    		  FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    		  lp2.gravity = Gravity.CENTER;
    		  this.loadFailView.setLayoutParams(lp2);
    		  this.loadFailView.setGravity(Gravity.CENTER);
    		  this.loadFailView.setText("加载失败，请点击重新加载");
    		  
              this.progressBar = new ProgressBar(this.mContext);
              FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    		  lp3.gravity = Gravity.CENTER;
              lp3.width = 80;
              lp3.height = 80;
              this.progressBar.setLayoutParams(lp3);
              this.progressBar.setIndeterminate(true);
             
    		 addView(this.loadNothingView);
    		 addView(this.loadFailView);
    		 addView(this.progressBar);
		}catch (Exception e){
			e.printStackTrace();
		}
    }


    public void startLoadingAnim(){
        try {
            setVisibility(VISIBLE);
           
                this.loadFailView.setVisibility(INVISIBLE);
                this.loadNothingView.setVisibility(INVISIBLE);
                this.progressBar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLoadingAnim(){
        setVisibility(INVISIBLE);
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    //显示加载失败view
    public void showFailView(){
        setVisibility(VISIBLE);
        if(this.loadFailView != null){
            this.loadFailView.setVisibility(VISIBLE);
            this.loadNothingView.setVisibility(INVISIBLE);
            this.progressBar.setVisibility(INVISIBLE);
        }
    }


    public void hideFailView(){
        setVisibility(INVISIBLE);
        if(this.loadFailView != null){
            this.loadFailView.setVisibility(INVISIBLE);
        }
    }

   //显示nothingview
    public void showNothingView(){
        setVisibility(VISIBLE);
        if(this.loadNothingView != null){
            this.loadFailView.setVisibility(INVISIBLE);
            this.progressBar.setVisibility(INVISIBLE);
            this.loadNothingView.setVisibility(VISIBLE);
        }
    }

    //隐藏nothingview
    public void hideNothingView(){
        setVisibility(INVISIBLE);
        if(this.loadNothingView != null){
            this.loadNothingView.setVisibility(INVISIBLE);
        }
    }
}


