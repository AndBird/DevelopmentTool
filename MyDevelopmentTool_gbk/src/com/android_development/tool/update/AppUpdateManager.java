package com.android_development.tool.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.android_development.filetool.SharePreferenceTool;
import com.android_development.nettool.NetTool;
import com.android_development.tool.DebugUtils;
import com.android_development.uitool.CustomDialogUtils;
import com.android_development.uitool.CustomDialogUtils.CustomDialogClickListener;
import com.development.android.tool.R;
import com.development.tool.test.AppUpdateMainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


/*下载apk*/
public class AppUpdateManager{
	private static final String TAG = AppUpdateManager.class.getName();
	public static final String UPDATE_CANCEL_EXIT_APP = "exit_app";
	
	private static final int CHECK_UPDATE_FAIL = 201;//检测失败
	private static final int CHECK_UPDATE_LATEST = 202;//最新了
	private static final int NO_NETWORK = 203;//没有网
	
	public static final int Download_STATUS_FAILED = 1;
	public static final int Download_STATUS_SUCCESSFUL = 2;
	public static final int Download_TATUS_RUNNING = 3;
	
	private static AppUpdateManager instance = new AppUpdateManager();
	private Context mContext;
	
	//更新相关
	private String updateCheckUrl;//更新检测地址
	private String saveDir;//下载保存目录
    private String saveFilePath;//下载完整路径
    private String saveFileName;//下载文件名
    private static Class mainActivity;
    
    //下载线程
    private Thread downLoadThread;
    private boolean stopDownload = false; //终止下载
    
    private String downloadUrl = "";//更新下载地址
    private String serverVersionName;//服务器最新版本号
    private String lowVersionName; //要求最低版本号，用于强制更新
    private String curVersionName;//当前版本号
    private boolean force_update;//强制更新
    private String updateLog;//更新内容
    private ArrayList<String> updateLogList;//更新内容
    private int appTotalSize = -1;// app更新包的大小
    
    private ProgressDialog downloadDialog;
    private boolean checkUpdating = false; //正在检测更新
    private boolean canShowDialog = false; //是否显示下载对话框，这个是
    private boolean allowBreakPointDownload = false;//是否允许断点下载
    private boolean showCheckToast = false;//是否显示检测结果Toast
    private boolean downloading = false;// 正在下载中
    
    
    public final static String VERSION_SERVER = "server_version";//服务器当前的版本号
    public final static String VERSION_IS_NOW = "verson_is_now";//保存的服务器版本号是否是最新获取的
    public final static String DOWNLOAD_URL = "download_url";//下载地址
    
    private Handler mHandler;
    private Handler mUpdateHandler = null;//下载进度更新，其他页面
    
    private UpdateDataReceiveListener mUpdateDataReceiveListener = null;
    public interface UpdateDataReceiveListener{
    	public void analyzeVersionInfo(String data);//更新信息分析
    }
    
    public static AppUpdateManager getInstance(){
    	if(instance == null){
    		instance = new AppUpdateManager();
    	}
    	return instance;
    }
    
    private AppUpdateManager(){
    	init();
    	initHandler();
    }
    
    /**
     * 供直接下载用
     * @param downloadUrl : 下载地址
     * @param saveDir: 下载保存目录(详细路径)
     * @param fileName: 保存文件名
     * */
	public void startDownload(Context context, String downloadUrl, String saveDir, String fileName){
		this.mContext = context;
		this.downloadUrl = downloadUrl;
		this.saveDir = saveDir;
		this.saveFileName = fileName;
		if(saveDir.endsWith("/")){
			saveFilePath =  saveDir + saveFileName;
		}else{
			saveFilePath =  saveDir + File.separator + saveFileName;
		}
		DebugUtils.debug(TAG, "this.downloadUrl = " + this.downloadUrl);
		DebugUtils.debug(TAG, "saveFilePath = " + saveFilePath);
		this.canShowDialog = true;
		startDownload();
	}
	
	private void init(){
		this.checkUpdating = false;
		this.canShowDialog = false;
		this.downloading = false;
		this.allowBreakPointDownload = false;
	}
	
	private void initParams(Context context, boolean showCheckToast, String updateCheckUrl, UpdateDataReceiveListener updateDataReceiveListener){
		this.mContext = context;
		this.showCheckToast = showCheckToast;
		this.updateCheckUrl = updateCheckUrl;
		this.mUpdateDataReceiveListener = updateDataReceiveListener;
	}
	
	private void initHandler(){
		mHandler = new Handler(){
	    	public void handleMessage(Message msg){
	    		switch (msg.what) {
					case Download_TATUS_RUNNING://下载进度更新
						int progress =(byte)(((float)msg.arg1 / appTotalSize) * 100);
						if(downloadDialog != null){
							downloadDialog.setProgress(progress);
						}
						break;
					case Download_STATUS_SUCCESSFUL:
						if(downloadDialog != null){
							downloadDialog.dismiss();
							downloadDialog = null;
						}
						if(canShowDialog){
							showInstallDialog();
						}
						break;
					case Download_STATUS_FAILED:
						if(downloadDialog != null){
							downloadDialog.dismiss();
							downloadDialog = null;
						}
						Toast.makeText(mContext, R.string.download_fail, Toast.LENGTH_SHORT).show();
						break;
					case CHECK_UPDATE_FAIL:
						if(showCheckToast){
							Toast.makeText(mContext, R.string.update_fail, Toast.LENGTH_SHORT).show();
						}
						break;
					case CHECK_UPDATE_LATEST:
						if(showCheckToast){
							Toast.makeText(mContext, R.string.current_version_is_newest, Toast.LENGTH_SHORT).show();
						}
						break;
					case NO_NETWORK:
						Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_SHORT).show();
						break;
				}
	    	}
	    };
	}
	
    /**
     * 
     * 开始版本检测
     * @param showCheckToast : 是否显示检测提示
     * @param updateCheckUrl : 检测更新地址
     * @param updateDataReceiveListener: 检测更新数据分析回调
     * 
     * */
    public void startCheckUpdate(Activity context, boolean showCheckToast, String updateCheckUrl, UpdateDataReceiveListener updateDataReceiveListener){
    	try {
    		DebugUtils.debug(TAG, "startCheckUpdate");
    		initParams(context, showCheckToast, updateCheckUrl, updateDataReceiveListener);
    		 //检测更新
            SharePreferenceTool.setPrefBoolean(context, VERSION_IS_NOW, false);//先重置保存的版本号是否是最新标记
            if(NetTool.isNetworkConnected(context)){
            	if(checkUpdating){
    				DebugUtils.debug(TAG, "startCheckUpdate: has another Thread checkUpdating");
    				return;
    			}
            	new Thread(new Runnable() {
        			@Override
        			public void run() {
        				checkUpdate();
        			}
        		}).start();
            }else{
            	Toast.makeText(context, R.string.no_network, Toast.LENGTH_SHORT).show();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	private void checkUpdate(){
		try{
			DebugUtils.debug(TAG,"checkUpdate");
			checkUpdating = true;
			String versionInfo = getVersionInfoFromServer();
			DebugUtils.debug(TAG, "ServerVersionInfo:" + versionInfo);
			if(versionInfo != null){
				DebugUtils.debug(TAG, "getupdateinfo = " + versionInfo);
				if(mUpdateDataReceiveListener != null){
					mUpdateDataReceiveListener.analyzeVersionInfo(versionInfo);
				}
				DebugUtils.debug(TAG, "serverVersionName = " + serverVersionName + " lowVersionName=" + lowVersionName);
					
				if(serverVersionName == null || lowVersionName == null || downloadUrl == null){
					checkUpdating = false;
					return;
				}
					
				SharePreferenceTool.setPrefString(mContext, VERSION_SERVER, serverVersionName);//保存版本号
				SharePreferenceTool.setPrefBoolean(mContext, VERSION_IS_NOW, true);//先重置保存的版本号是否是最新标记
				SharePreferenceTool.setPrefString(mContext, DOWNLOAD_URL, downloadUrl);//保存下载地址
					
				if((Float.compare(Float.valueOf(curVersionName), Float.valueOf(serverVersionName)) >= 0)){
					DebugUtils.debug(TAG, "checkUpdate() 不需要更新");
					mHandler.sendEmptyMessage(CHECK_UPDATE_LATEST);
				}else{
					DebugUtils.debug(TAG, "checkUpdate() 需要更新");
					//判断是不是必须更新
					force_update = checkIsForceUpdate();
					if(force_update){
						DebugUtils.debug(TAG, "checkUpdate() 强制更新");
					}else{
						DebugUtils.debug(TAG, "checkUpdate() 非强制更新");
					}
					if(TextUtils.isEmpty(saveFilePath) || TextUtils.isEmpty(saveFileName)){
						this.saveFileName = mContext.getPackageName() + "_" + serverVersionName + ".apk";
						if(saveDir.endsWith("/")){
							saveFilePath =  saveDir + saveFileName;
						}else{
							saveFilePath =  saveDir + File.separator + saveFileName;
						}
					}
					DebugUtils.debug(TAG, "checkUpdate() saveFilePath=" + saveFilePath);
					showUpdateLogWindow();//显示更新日志
				}
			}else{	//检测更新失败  提示
				DebugUtils.debug(TAG, "check fail info is null");
				checkUpdating = false;
				if(showCheckToast){
					mHandler.sendEmptyMessage(CHECK_UPDATE_FAIL);
				}
			}
			versionInfo = null;
		}catch(Exception e){
			checkUpdating = false;
			e.printStackTrace();
			DebugUtils.debug(TAG, "Exception err = " + e.toString());
		}finally{
			checkUpdating = false;
		}
	}
	
	//获取服务器版本信息
	private String getVersionInfoFromServer(){
		try{
			String url = updateCheckUrl;
			InputStream in = NetTool.getInputStreamByGet(url);
			return  NetTool.InputStreamToString(in);
		}catch(Exception e){
			DebugUtils.debug(TAG, "getVersionInfoFromServer err:" + e.toString());
			return null;
		}
	}
	
	/**
	 * 
	 * 设置更新版本信息
	 * @param serverVersionName : 服务器最新版本号
	 * @param lowVersionName : 最低使用版本号(用于强更)
	 * @param curVersionName : 当前版本号(不一定与AndroidManifest.xml中的versionName一致)
	 * @param downloadUrl: 更新下载地址
	 * @param updateLog: 更新日志
	 * @param saveDir : 下载保存目录(完整)
	 * 
	 * */
	public void setAnalyzeVersionInfo(String serverVersionName, String lowVersionName, String curVersionName, String downloadUrl, String updateLog, String saveDir){
		this.serverVersionName = serverVersionName;
		this.lowVersionName = lowVersionName;
		this.curVersionName = curVersionName;
		this.downloadUrl = downloadUrl;
		this.updateLog = updateLog;
		this.saveDir = saveDir;
	}
	
	/**
	 * 
	 * 设置更新版本信息
	 * @param serverVersionName : 服务器最新版本号
	 * @param lowVersionName : 最低使用版本号(用于强更)
	 * @param curVersionName : 当前版本号(不一定与AndroidManifest.xml中的versionName一致)
	 * @param downloadUrl: 更新下载地址
	 * @param updateLog: 更新日志
	 * @param saveDir : 下载保存目录(完整)
	 * 
	 * */
	public void setAnalyzeVersionInfo2(String serverVersionName, String lowVersionName, String curVersionName, String downloadUrl, ArrayList<String> updateLogList, String saveDir){
		this.serverVersionName = serverVersionName;
		this.lowVersionName = lowVersionName;
		this.curVersionName = curVersionName;
		this.downloadUrl = downloadUrl;
		this.updateLogList = updateLogList;
		this.saveDir = saveDir;
	}
	
	//判断是否强制更新
	private boolean checkIsForceUpdate(){
		try{
			if(Float.valueOf(lowVersionName) > Float.valueOf(curVersionName)){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	//显示更新日志界面
	private void showUpdateLogWindow(){
		if(updateLogList != null && updateLogList.size() > 0){
			UpdateLogDialogActivity.startActivitySelf(mContext, updateLogList, force_update);
		}else if(!TextUtils.isEmpty(updateLog)){
			UpdateLogDialogActivity.startActivitySelf(mContext, updateLog, "", force_update);
		}else{
			UpdateLogDialogActivity.startActivitySelf(mContext, "", "", force_update);
		}
	}
	
	//显示下载界面
	public void showDownloadWindow(){
		UpdateDownLoadActivity.startActivitySelf(mContext, force_update);
	}
	
	/**
	 * 开始更新下载
	 * */
	public void startDownload(){
		if(canShowDialog){
			if(checkApkFileIsAll()){//安装文件存在，则直接安装
				showInstallDialog();
			}else{
				showDownloadProgressDialog();
				downloadApk();
			}
		}else{
			downloadApk();
		}
	}

	private void showDownloadProgressDialog(){	
		downloadDialog = new ProgressDialog(mContext);
		downloadDialog.setTitle(mContext.getString(R.string.current_version_is_old));
		downloadDialog.setMax(100);
		downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadDialog.setCancelable(true);
		downloadDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.dlg_button_cancel), new OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				downloadDialog.dismiss();
				downloadDialog = null;
				stopDownload();
				if(force_update){
					exitApp(mContext); //如果是强制更新，取消下载时强制退出
				}
			}
			
		});
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
	}
	
	private Runnable mDownloadRunnable = new Runnable(){			 
		public void run() {
			try {
				downloading = true;
				File file = new File(saveDir);
				if(!file.exists()){
					file.mkdirs();
				}
				File downloadFile = new File(saveFilePath);
				int saveFileLength = 0;
				if (downloadFile != null && downloadFile.exists()) {
					if(allowBreakPointDownload){
						 saveFileLength = (int) downloadFile.length();
					}else{
						downloadFile.delete();
						downloadFile.createNewFile();
					}
				}
				DebugUtils.debug(TAG + "/Runnable", "saveFileLength = " + saveFileLength);
				
				//int len = getApkFileSize();
				//DebugUtils.printInfo(TAG, "get size from url=" + len);
				
				URL url = new URL(downloadUrl);			
				DebugUtils.debug(TAG, "下载url=" + downloadUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setDoInput(true);
				//conn.setDoOutput(true); //开启后下载异常,默认值为false
				conn.setUseCaches(false);
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.setRequestProperty("Range", "bytes=" + saveFileLength + "-");
				conn.setConnectTimeout(6000);
				conn.setReadTimeout(6000);
				conn.connect();
				
				int length = conn.getContentLength();
				DebugUtils.debug(TAG + "/Runnable", "download file size = " + length);
				if(-1 != length){
					InputStream is = conn.getInputStream();
					RandomAccessFile fos = new RandomAccessFile(downloadFile, "rwd");
					fos.seek(saveFileLength);
					
					appTotalSize = saveFileLength + length;
					int count = saveFileLength;
					byte buf[] = new byte[1024];
					
					stopDownload = false;
					do{   		   		
			    		int numread = is.read(buf);
			    		if(numread < 0){
			    			break;
			    		}
			    		count += numread;
			    	    //更新进度
			    	    Message msg = new Message();
			    	    msg.what = Download_TATUS_RUNNING;
			    	    msg.arg1 = count;
			    	    msg.arg2 = appTotalSize;
			    	    if(mUpdateHandler != null){
			    	    	mUpdateHandler.sendMessage(msg);
			    	    }else{
			    	    	mHandler.sendMessage(msg);
			    	    }
			    		fos.write(buf, 0, numread);
			    	}while(!stopDownload);//点击取消就停止下载
					
					fos.close();
					is.close();
				}else{
					if(checkApkFileIsAll()){
						stopDownload = false;//下载的包是完整的，就是下载完成
					}else{
						stopDownload = true;
						if(mUpdateHandler != null){
			    	    	mUpdateHandler.sendEmptyMessage(Download_STATUS_FAILED);
						}else{
							mHandler.sendEmptyMessage(Download_STATUS_FAILED);
						}
					}
					return ;
				}
				
				if(!stopDownload){
		    		//下载完成通知安装
					Message msg = new Message();
	    	    	msg.what = Download_STATUS_SUCCESSFUL;
	    	    	msg.arg1 = appTotalSize;
	    	    	msg.arg2 = appTotalSize;
					if(mUpdateHandler != null){
		    	    	mUpdateHandler.sendMessage(msg);
					}else{
						mHandler.sendMessage(msg);
					}
		    		return ;
		    	}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				stopDownload = true;
				if(mUpdateHandler != null){
					mUpdateHandler.sendEmptyMessage(Download_STATUS_FAILED);
				}else{
					mHandler.sendEmptyMessage(Download_STATUS_FAILED);
				}
			} catch(IOException e){
				e.printStackTrace();
				stopDownload = true;
				if(mUpdateHandler != null){
					mUpdateHandler.sendEmptyMessage(Download_STATUS_FAILED);
				}else{
					mHandler.sendEmptyMessage(Download_STATUS_FAILED);
				}
			}finally{
				downloading = false;
			}
		}
	};
	
	/**
     * 下载apk
     */
	private void downloadApk(){
		if(downloading){
			DebugUtils.debug(TAG, "downloadApk: has another Thread downloading");
			return ;
		}
		
		downLoadThread = new Thread(mDownloadRunnable);
		downLoadThread.start();
	}
	
	/**
     * 安装apk
     */
	public void installApk(){
		File apkfile = new File(saveFilePath);
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        DebugUtils.debug(TAG + "/installApk", "filename = " + "file://" + apkfile.toString());
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	}
	
	/**
	 * 
	 * 设置进度更新handler：
	 * 用于在其他页面时页面获取到进度
	 * */
	public void setDownloadUpdateHandler(Handler handler){
		this.mUpdateHandler = handler;
	}
	
	
	 /**
	  * 检测下载完成的apk文件是否完整
	  * */
	public boolean checkApkFileIsAll(){
		boolean flag = false;
		try {
			if(TextUtils.isEmpty(saveFilePath)){
				flag = false;
				return flag;
			}
			File file = new File(saveFilePath); 
			if(!file.exists()){
				flag = false;
				return flag;
			}
			PackageManager pm = mContext.getPackageManager();     
	        PackageInfo packInfo = pm.getPackageArchiveInfo(saveFilePath, PackageManager.GET_ACTIVITIES);     
	        if(packInfo != null){
	        	flag = true;
		   }else{
			   flag = false;
		   }
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}finally{
			if(flag == false){
				 DebugUtils.debug(TAG, "checkApkFileIsAll 解析apk失败");
			}
		}
		return flag;
  }
	
	/**
	 * 停止下载
	 * */
	public void stopDownload(){
		stopDownload = true;
	}
	
	//获取apk的大小
	private int getApkFileSize(){
		HttpURLConnection conn = null;
		try{
			conn = (HttpURLConnection)(new URL(downloadUrl)).openConnection();
			conn.setConnectTimeout(3000);
			conn .setRequestProperty("Accept-Encoding", "identity"); 	//设置不做gzip压缩  免得获取的大小为-1
			conn.setRequestMethod("GET");
			int size = 0;
			if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
				size = conn.getContentLength();
			}
			conn.disconnect();
			conn = null;
			return size;
		}catch(Exception e){
			return 0;
		}finally{
			if(conn != null){
				conn.disconnect();
				conn = null;
			}
		}
	}
	
		//终止程序
		public void exitApp(){
			System.exit(0);
			//android.os.Process.killProcess(android.os.Process.myPid());
			instance = null;
		}
		
		/**
		 * 设置App主界面Class(不一定是入口Activity),用于强制退出程序(对于强更)
		 * */
		public void setMainActivityClass(Class mainActivity){
			this.mainActivity = mainActivity;
		}
		
		/**
		 * 
		 * 退出程序
		 * 通过跳转到更新首界面(程序主界面，不一定是入口Activity)终止程序
		 * */
		public static void exitApp(Context activity){
			try {
				Intent intent = new Intent();   
				intent.setClass(activity, mainActivity);  
				//mainActivity不一定是程序的入口,只对主界面生效
				//Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
				intent.putExtra(UPDATE_CANCEL_EXIT_APP, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG设置  
				activity.startActivity(intent);  
				//activity.finish();  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//下载完成弹出对话框是否安装
		private void showInstallDialog() {
			DebugUtils.debug(TAG + "/showInstallConfirmDialog", "showInstallConfirmDialog");
			AlertDialog dialog = CustomDialogUtils.showCustomDialog(mContext, R.string.dlg_msg_notice, R.string.update_download_finish, 
					R.string.dlg_button_confirm, R.string.dlg_button_cancel, new CustomDialogClickListener() {
						
						@Override
						public void onOkClick(View v, AlertDialog dialog) {
							CustomDialogUtils.hideDialog(dialog);
							AppUpdateManager.getInstance().installApk();
						}
						
						@Override
						public void onCancelClick(View v, AlertDialog dialog) {
							CustomDialogUtils.hideDialog(dialog);
							if(force_update){
								exitApp(mContext);
							}
						}
					});
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
		}
}
