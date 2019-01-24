package com.android_development.tool.update;


import java.io.File;

import com.android_development.nettool.NetTool;
import com.android_development.tool.DebugUtils;
import com.android_development.uitool.CustomDialogUtils;
import com.android_development.uitool.CustomDialogUtils.CustomDialogClickListener;
import com.android_development.uitool.CustomDialogUtils.CustomDialogSingleClickListener;
import com.development.android.tool.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateDownLoadActivity extends Activity{
	private final String TAG = UpdateDownLoadActivity.class.getName();
	
	public static final String INTENT_FORCE_UPDATE = "is_force_update";//�Ƿ���ǿ�Ƹ���
	
	private Handler updateHandler;
	private TextView stateView, sizeView, progressView;
	private ProgressBar bar;
	private TextView restart;
	private boolean isDownLoadFinish = false; //�Ƿ��������

	private boolean forceUpdate = false;
	private AlertDialog dlg = null;
	
	//��ʾ֪ͨ��
	private NotificationManager notificationManager = null;
	private NotificationCompat.Builder mBuilder = null;
	private final int Notification_Id = 0;
	/**��һ�θ���ʱ�Ľ���ֵ*/
	private int oldPercent = 0;
	/**��һ�θ��µ�ʱ��*/
	private long lastUpdateTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		DebugUtils.printInfo(TAG, "onCreate");
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
					oldPercent = 0;
					lastUpdateTime = 0;
					showNotification(getString(R.string.dlg_msg_notice), 0, 100);
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
	
	//��ʼ����
	private void startDownLoad(){
		try {	
			if(AppUpdateManager.getInstance().checkApkFileIsAll()){
				//�Ѿ�������ɣ���ֱ�Ӱ�װ
				isDownLoadFinish = true;
				Message msg = updateHandler.obtainMessage();
				msg.what = AppUpdateManager.Download_STATUS_SUCCESSFUL;
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
	
 
	 //��������״̬
	 private void updateDownLoadState(int state, int downloadSize, int totalSize){
		 try {
			    bar.setVisibility(View.VISIBLE);
				switch(state){
				case AppUpdateManager.Download_STATUS_FAILED:
					DebugUtils.debug(TAG, "����ʧ��");
					if(!NetTool.isNetworkConnected(getApplicationContext())){
						showAlertDialog(R.string.no_network);
					}else if(getAvailableExternalMemorySize() < totalSize){
						showAlertDialog(R.string.available_memory_space_no_enough);
					}else{
						Toast.makeText(getApplicationContext(), R.string.download_fail, Toast.LENGTH_SHORT).show();
					}
					restart.setVisibility(View.VISIBLE);
					hideNotification();
					break;
				case AppUpdateManager.Download_STATUS_SUCCESSFUL:
					DebugUtils.debug(TAG, "Download_STATUS_SUCCESSFUL");
					bar.setMax(100);
					bar.setProgress(100);
					sizeView.setText(Formatter.formatFileSize(getApplicationContext(), totalSize));
					progressView.setText("100%");
					isDownLoadFinish = true;
					showInstallConfirmDialog();
					showNotification(getString(R.string.update_dlg_title), 100, 100);
					break;
				case AppUpdateManager.Download_TATUS_RUNNING:
					if(totalSize > 0){
						bar.setMax(100);
						int progress = (int) (downloadSize * 1.0 / totalSize * 100);
						bar.setProgress(progress);
						sizeView.setText(Formatter.formatFileSize(getApplicationContext(), downloadSize) + "/" + Formatter.formatFileSize(getApplicationContext(), totalSize));
						progressView.setText(progress + "%");
						showNotification(getString(R.string.update_dlg_title), progress, 100);
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
			//showAlertDialog3(UpdateDownLoadActivity.this, "��������Ƿ�װ?");
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
     * ��ȡ�ֻ�ʣ��洢�ռ�
     * @return
     */
    public static long getAvailableExternalMemorySize(){
    	try {
    		File path = null;
        	StatFs stat = null;
        	if (externalMemoryAvailable()) {//sd���洢�ؼ�
                path = Environment.getExternalStorageDirectory();
                stat = new StatFs(path.getPath());
            } else {//�ڲ��洢�ռ�
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
    
    //�Ƿ����ڴ濨
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
	
	/**�������ظ���ʱ�����ؼ�*/
	private void showStopDownloadDialog() {
		DebugUtils.debug(TAG, "showAlertDialog");
		dlg = CustomDialogUtils.showCustomDialog(UpdateDownLoadActivity.this, R.string.dlg_msg_notice, R.string.stop_app_update_download, new CustomDialogClickListener() {
			
			@Override
			public void onOkClick(View v, AlertDialog dialog) {
				CustomDialogUtils.hideDialog(dialog);
				stopDownLoad(); //ֹͣ����
				if(forceUpdate){
					hideNotification();
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
		
		
		/**������ɵ����Ի����Ƿ�װ*/
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
		
		/**��ʾ״̬������(�Ƚ���)
		 * 
		 * Ϊ�˷�ֹƵ����֪ͨ����Ӧ�óԽ����ٷֱ�����һ����ֵ��֪ͨһ��
		 * 
		 * */
		private void showNotification(String title, int progress, int maxProgress) {
			try {
				if(notificationManager == null){
					notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				    mBuilder = new NotificationCompat.Builder(getApplicationContext());
					//RemoteViews views = new RemoteViews(m_Context.getPackageName(), R.layout.upgrade_notification_layout);
					//views.setImageViewResource(R.id.notification_large_icon, R.drawable.ic_launcher);
					//views.setTextViewText(R.id.notification_title, title);
					//views.setTextViewText(R.id.notification_text, content);

					mBuilder.setContentTitle(title);
					// ��һ����ʾ��Ϣ��ʱ����ʾ��֪ͨ����
					mBuilder.setTicker(title);
					mBuilder.setSmallIcon(R.drawable.ic_launcher);
					// �Լ�ά��֪ͨ����ʧ
					mBuilder.setAutoCancel(true);
					 //����Ϊ�������ģʽ
					mBuilder.setOngoing(false);
					//�����Զ���view
					//mBuilder.setContent(views);
					//views.setProgressBar(R.id.progressBar, maxProgress, progress, false);
				}
				
				//Ϊ�˷�ֹƵ����֪ͨ����Ӧ�óԽ����ٷֱ�����10��ʱ�䳬��1000�����֪ͨһ��
				if(0 == progress || 100 == progress || 0 == lastUpdateTime || progress - 3 > oldPercent && (System.currentTimeMillis() - lastUpdateTime) >= 1000){
					mBuilder.setProgress(maxProgress, progress, false);
					oldPercent = progress;
					lastUpdateTime = System.currentTimeMillis();
					
					if(progress == maxProgress){
						mBuilder.setContentText(getString(R.string.click_to_install));
					}else{
						mBuilder.setContentText(getString(R.string.downloading));
					}
					
					if(progress == maxProgress){
						File apkfile = new File(AppUpdateManager.getInstance().getDownloadFilePath());
						if (apkfile.exists()) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
							PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
									intent, PendingIntent.FLAG_UPDATE_CURRENT);
							mBuilder.setContentIntent(pendingIntent);
						}
					}else{
						mBuilder.setContentIntent(null);
					}
					notificationManager.notify(Notification_Id, mBuilder.build());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**����֪ͨ��*/
		private void hideNotification(){
			if(notificationManager != null){
				notificationManager.cancel(Notification_Id);
			}
		}
}
