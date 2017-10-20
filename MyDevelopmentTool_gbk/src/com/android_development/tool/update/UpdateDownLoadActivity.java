package com.android_development.tool.update;


import java.io.File;

import com.android_development.nettool.NetTool;
import com.android_development.tool.DebugUtils;
import com.android_development.uitool.CustomDialogUtils;
import com.android_development.uitool.ResourceLoadTool;
import com.android_development.uitool.CustomDialogUtils.CustomDialogClickListener;
import com.android_development.uitool.CustomDialogUtils.CustomDialogSingleClickListener;
import com.development.android.tool.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateDownLoadActivity extends Activity{
	private static final String TAG = UpdateDownLoadActivity.class.getName();
	
	public static final String INTENT_FORCE_UPDATE = "is_force_update";//是否是强制更新
	
	
	public static final int Download_STATUS_FAILED = 1;
	public static final int Download_STATUS_SUCCESSFUL = 2;
	public static final int Download_TATUS_RUNNING = 3;
	
	private Handler updateHandler;
	private TextView stateView, sizeView, progressView;
	private ProgressBar bar;
	private TextView restart;
	private boolean isDownLoadFinish = false; //是否下载完成

	private boolean forceUpdate = false;
	private AlertDialog dlg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_download);
		initView();
		initHandler();
		initData();
		startDownLoad();
	}
	
	
	private void initView(){
		try {
			stateView = (TextView) findViewById(R.id.download_state);
			bar = (ProgressBar) findViewById(R.id.download_bar);
			sizeView = (TextView) findViewById(R.id.download_size);
			progressView = (TextView) findViewById(R.id.download_progrss);
			restart = (TextView) findViewById(R.id.restart_download);
			
			restart.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					restart.setVisibility(View.INVISIBLE);
					AppUpdateManager.getInstance().startDownload();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initData(){
		try {
			    Intent in = getIntent();
			    forceUpdate = in.getBooleanExtra(INTENT_FORCE_UPDATE, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//开始下载
	private void startDownLoad(){
		try {	
			if(AppUpdateManager.getInstance().checkApkFileIsAll()){
				//已经下载完成，就直接安装
				isDownLoadFinish = true;
				Message msg = updateHandler.obtainMessage();
				msg.what = Download_STATUS_SUCCESSFUL;
				msg.arg1 = 100;
				msg.sendToTarget();
				showInstallConfirmDialog();
			}else{
				AppUpdateManager.getInstance().setDownloadUpdateHandler(updateHandler);
				AppUpdateManager.getInstance().startDownload();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initHandler(){
		updateHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(activityIsFinish(UpdateDownLoadActivity.this)){
					return ;
				}
				updateDownLoadState(msg.what, msg.arg1, msg.arg2);		
			}
		};
	}
	
 
	 //设置下载状态
	 private void updateDownLoadState(int state, int downloadSize, int totalSize){
		 try {
			    bar.setVisibility(View.VISIBLE);
				switch(state){
				case Download_STATUS_FAILED:
					DebugUtils.debug(TAG, "更新失败");
					if(!NetTool.isNetworkConnected(getApplicationContext())){
						showAlertDialog(R.string.no_network);
					}else if(getAvailableExternalMemorySize() < totalSize){
						showAlertDialog(R.string.available_memory_space_no_enough);
					}else{
						Toast.makeText(getApplicationContext(), R.string.download_fail, Toast.LENGTH_SHORT).show();
					}
					restart.setVisibility(View.VISIBLE);
					break;
				case Download_STATUS_SUCCESSFUL:
					DebugUtils.debug(TAG, "Download_STATUS_SUCCESSFUL");
					bar.setMax(100);
					bar.setProgress(100);
					sizeView.setText(Formatter.formatFileSize(getApplicationContext(), totalSize));
					progressView.setText("100%");
					isDownLoadFinish = true;
					showInstallConfirmDialog();
					break;
				case Download_TATUS_RUNNING:
					if(totalSize > 0){
						bar.setMax(100);
						int progress = downloadSize * 100 / totalSize;
						bar.setProgress(progress);
						sizeView.setText(Formatter.formatFileSize(getApplicationContext(), downloadSize) + "/" + Formatter.formatFileSize(getApplicationContext(), totalSize));
						progressView.setText(progress + "%");
					}
					break;
				default:
					break;
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
	
	 
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isDownLoadFinish){
			showInstallConfirmDialog();
			//showAlertDialog3(UpdateDownLoadActivity.this, "下载完成是否安装?");
		}
	}
	 
	@Override
	protected void onDestroy() {
		DebugUtils.debug(TAG, "in UpdateDownLoadActivity onDestroy");
		super.onDestroy();
		DebugUtils.debug(TAG, "in UpdateDownLoadActivity onDestroy end");
	}
	
	private void stopDownLoad(){
		try {
			AppUpdateManager.getInstance().stopDownload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		if(isDownLoadFinish){
			DebugUtils.debug(TAG, "onBackPressed");
			UpdateDownLoadActivity.this.finish();
		}else{
			if(forceUpdate){
				showStopDownloadDialog();
			}else{
				Toast.makeText(getApplicationContext(), R.string.download_in_background, Toast.LENGTH_SHORT).show();
				UpdateDownLoadActivity.this.finish();
			}
		}
	}
	
	
	 /**
     * 获取手机剩余存储空间
     * @return
     */
    public static long getAvailableExternalMemorySize(){
    	try {
    		File path = null;
        	StatFs stat = null;
        	if (externalMemoryAvailable()) {//sd卡存储控件
                path = Environment.getExternalStorageDirectory();
                stat = new StatFs(path.getPath());
            } else {//内部存储空间
            	path = Environment.getDataDirectory();
                stat = new StatFs(path.getPath());
            }
        
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            	 long blockSize = stat.getBlockSize();
                 long availableBlocks = stat.getAvailableBlocks();
                 return availableBlocks * blockSize;
            }else{
            	return stat.getAvailableBytes();
            }
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    //是否有内存卡
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
	
    
	private void showAlertDialog(int msgRes) {
		DebugUtils.debug(TAG, "showAlertDialog");
		if(dlg != null && dlg.isShowing()){
			dlg.dismiss();
			dlg = null;
		}
		CustomDialogUtils.showSingleButtonCustomDialog(UpdateDownLoadActivity.this, R.string.dlg_msg_notice, msgRes, new CustomDialogSingleClickListener() {

			@Override
			public void onClick(View v, AlertDialog dialog) {
				CustomDialogUtils.hideDialog(dialog);
				UpdateDownLoadActivity.this.finish();
			}
		});
		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(false);
	}
	
	//正在下载更新时按返回键
	private void showStopDownloadDialog() {
		DebugUtils.debug(TAG, "showAlertDialog");
		dlg = CustomDialogUtils.showCustomDialog(UpdateDownLoadActivity.this, R.string.dlg_msg_notice, R.string.stop_app_update_download, new CustomDialogClickListener() {
			
			@Override
			public void onOkClick(View v, AlertDialog dialog) {
				CustomDialogUtils.hideDialog(dialog);
				stopDownLoad(); //停止下载
				if(forceUpdate){
					UpdateDownLoadActivity.this.finish();
					AppUpdateManager.exitApp(UpdateDownLoadActivity.this);
				}
			}
			
			@Override
			public void onCancelClick(View v, AlertDialog dialog) {
				CustomDialogUtils.hideDialog(dialog);
			}
		});
		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(false);
	}
	

		public static void startActivitySelf(Context context, boolean force_update){
			try {
				Intent in = new Intent(context, UpdateDownLoadActivity.class);
				in.putExtra(INTENT_FORCE_UPDATE, force_update);
				context.startActivity(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static boolean activityIsFinish(Activity activity){
			try {
				if(activity == null){
					return true;
				}
	            if(Build.VERSION.SDK_INT < 17){
	                if(activity.isFinishing()){// WelecomeActivity.this.isDestroyed()
	                    return true;
	                }else{
	                	return false;
	                }
	            }else{
	                if(activity.isFinishing() || activity.isDestroyed()){
	                    return true;
	                }else{
	                	return false;
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			return true;
		}
		
		
		//下载完成弹出对话框是否安装
		private void showInstallConfirmDialog() {
			DebugUtils.debug(TAG + "/showInstallConfirmDialog", "showInstallConfirmDialog");
			AlertDialog dialog = CustomDialogUtils.showCustomDialog(UpdateDownLoadActivity.this, R.string.dlg_msg_notice, R.string.update_download_finish, 
					R.string.dlg_button_confirm, R.string.dlg_button_cancel, new CustomDialogClickListener() {
						
						@Override
						public void onOkClick(View v, AlertDialog dialog) {
							CustomDialogUtils.hideDialog(dialog);
							if(forceUpdate){
								AppUpdateManager.exitApp(UpdateDownLoadActivity.this);
							}else{
								UpdateDownLoadActivity.this.finish();
							}
						}
						
						@Override
						public void onCancelClick(View v, AlertDialog dialog) {
							CustomDialogUtils.hideDialog(dialog);
							AppUpdateManager.getInstance().installApk();
						}
					});
			
			dialog.setCanceledOnTouchOutside(!forceUpdate);
			dialog.setCancelable(!forceUpdate);
		}
}
