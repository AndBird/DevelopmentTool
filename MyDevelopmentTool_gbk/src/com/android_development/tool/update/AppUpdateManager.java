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


/*����apk*/
public class AppUpdateManager{
	private static final String TAG = AppUpdateManager.class.getName();
	public static final String UPDATE_CANCEL_EXIT_APP = "exit_app";
	
	private static final int CHECK_UPDATE_FAIL = 201;//���ʧ��
	private static final int CHECK_UPDATE_LATEST = 202;//������
	private static final int NO_NETWORK = 203;//û����
	
	public static final int Download_STATUS_FAILED = 1;
	public static final int Download_STATUS_SUCCESSFUL = 2;
	public static final int Download_TATUS_RUNNING = 3;
	
	private static AppUpdateManager instance = new AppUpdateManager();
	private Context mContext;
	
	//�������
	private String updateCheckUrl;//���¼���ַ
	private String saveDir;//���ر���Ŀ¼
    private String saveFilePath;//��������·��
    private String saveFileName;//�����ļ���
    private static Class mainActivity;
    
    //�����߳�
    private Thread downLoadThread;
    private boolean stopDownload = false; //��ֹ����
    
    private String downloadUrl = "";//�������ص�ַ
    private String serverVersionName;//���������°汾��
    private String lowVersionName; //Ҫ����Ͱ汾�ţ�����ǿ�Ƹ���
    private String curVersionName;//��ǰ�汾��
    private boolean force_update;//ǿ�Ƹ���
    private String updateLog;//��������
    private ArrayList<String> updateLogList;//��������
    private int appTotalSize = -1;// app���°��Ĵ�С
    
    private ProgressDialog downloadDialog;
    private boolean checkUpdating = false; //���ڼ�����
    private boolean canShowDialog = false; //�Ƿ���ʾ���ضԻ��������
    private boolean allowBreakPointDownload = false;//�Ƿ�����ϵ�����
    private boolean showCheckToast = false;//�Ƿ���ʾ�����Toast
    private boolean downloading = false;// ����������
    
    
    public final static String VERSION_SERVER = "server_version";//��������ǰ�İ汾��
    public final static String VERSION_IS_NOW = "verson_is_now";//����ķ������汾���Ƿ������»�ȡ��
    public final static String DOWNLOAD_URL = "download_url";//���ص�ַ
    
    private Handler mHandler;
    private Handler mUpdateHandler = null;//���ؽ��ȸ��£�����ҳ��
    
    private UpdateDataReceiveListener mUpdateDataReceiveListener = null;
    public interface UpdateDataReceiveListener{
    	public void analyzeVersionInfo(String data);//������Ϣ����
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
     * ��ֱ��������
     * @param downloadUrl : ���ص�ַ
     * @param saveDir: ���ر���Ŀ¼(��ϸ·��)
     * @param fileName: �����ļ���
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
					case Download_TATUS_RUNNING://���ؽ��ȸ���
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
     * ��ʼ�汾���
     * @param showCheckToast : �Ƿ���ʾ�����ʾ
     * @param updateCheckUrl : �����µ�ַ
     * @param updateDataReceiveListener: ���������ݷ����ص�
     * 
     * */
    public void startCheckUpdate(Activity context, boolean showCheckToast, String updateCheckUrl, UpdateDataReceiveListener updateDataReceiveListener){
    	try {
    		DebugUtils.debug(TAG, "startCheckUpdate");
    		initParams(context, showCheckToast, updateCheckUrl, updateDataReceiveListener);
    		 //������
            SharePreferenceTool.setPrefBoolean(context, VERSION_IS_NOW, false);//�����ñ���İ汾���Ƿ������±��
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
					
				SharePreferenceTool.setPrefString(mContext, VERSION_SERVER, serverVersionName);//����汾��
				SharePreferenceTool.setPrefBoolean(mContext, VERSION_IS_NOW, true);//�����ñ���İ汾���Ƿ������±��
				SharePreferenceTool.setPrefString(mContext, DOWNLOAD_URL, downloadUrl);//�������ص�ַ
					
				if((Float.compare(Float.valueOf(curVersionName), Float.valueOf(serverVersionName)) >= 0)){
					DebugUtils.debug(TAG, "checkUpdate() ����Ҫ����");
					mHandler.sendEmptyMessage(CHECK_UPDATE_LATEST);
				}else{
					DebugUtils.debug(TAG, "checkUpdate() ��Ҫ����");
					//�ж��ǲ��Ǳ������
					force_update = checkIsForceUpdate();
					if(force_update){
						DebugUtils.debug(TAG, "checkUpdate() ǿ�Ƹ���");
					}else{
						DebugUtils.debug(TAG, "checkUpdate() ��ǿ�Ƹ���");
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
					showUpdateLogWindow();//��ʾ������־
				}
			}else{	//������ʧ��  ��ʾ
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
	
	//��ȡ�������汾��Ϣ
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
	 * ���ø��°汾��Ϣ
	 * @param serverVersionName : ���������°汾��
	 * @param lowVersionName : ���ʹ�ð汾��(����ǿ��)
	 * @param curVersionName : ��ǰ�汾��(��һ����AndroidManifest.xml�е�versionNameһ��)
	 * @param downloadUrl: �������ص�ַ
	 * @param updateLog: ������־
	 * @param saveDir : ���ر���Ŀ¼(����)
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
	 * ���ø��°汾��Ϣ
	 * @param serverVersionName : ���������°汾��
	 * @param lowVersionName : ���ʹ�ð汾��(����ǿ��)
	 * @param curVersionName : ��ǰ�汾��(��һ����AndroidManifest.xml�е�versionNameһ��)
	 * @param downloadUrl: �������ص�ַ
	 * @param updateLog: ������־
	 * @param saveDir : ���ر���Ŀ¼(����)
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
	
	//�ж��Ƿ�ǿ�Ƹ���
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
	
	//��ʾ������־����
	private void showUpdateLogWindow(){
		if(updateLogList != null && updateLogList.size() > 0){
			UpdateLogDialogActivity.startActivitySelf(mContext, updateLogList, force_update);
		}else if(!TextUtils.isEmpty(updateLog)){
			UpdateLogDialogActivity.startActivitySelf(mContext, updateLog, "", force_update);
		}else{
			UpdateLogDialogActivity.startActivitySelf(mContext, "", "", force_update);
		}
	}
	
	//��ʾ���ؽ���
	public void showDownloadWindow(){
		UpdateDownLoadActivity.startActivitySelf(mContext, force_update);
	}
	
	/**
	 * ��ʼ��������
	 * */
	public void startDownload(){
		if(canShowDialog){
			if(checkApkFileIsAll()){//��װ�ļ����ڣ���ֱ�Ӱ�װ
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
					exitApp(mContext); //�����ǿ�Ƹ��£�ȡ������ʱǿ���˳�
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
				DebugUtils.debug(TAG, "����url=" + downloadUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setDoInput(true);
				//conn.setDoOutput(true); //�����������쳣,Ĭ��ֵΪfalse
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
			    	    //���½���
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
			    	}while(!stopDownload);//���ȡ����ֹͣ����
					
					fos.close();
					is.close();
				}else{
					if(checkApkFileIsAll()){
						stopDownload = false;//���صİ��������ģ������������
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
		    		//�������֪ͨ��װ
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
     * ����apk
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
     * ��װapk
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
	 * ���ý��ȸ���handler��
	 * ����������ҳ��ʱҳ���ȡ������
	 * */
	public void setDownloadUpdateHandler(Handler handler){
		this.mUpdateHandler = handler;
	}
	
	
	 /**
	  * ���������ɵ�apk�ļ��Ƿ�����
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
				 DebugUtils.debug(TAG, "checkApkFileIsAll ����apkʧ��");
			}
		}
		return flag;
  }
	
	/**
	 * ֹͣ����
	 * */
	public void stopDownload(){
		stopDownload = true;
	}
	
	//��ȡapk�Ĵ�С
	private int getApkFileSize(){
		HttpURLConnection conn = null;
		try{
			conn = (HttpURLConnection)(new URL(downloadUrl)).openConnection();
			conn.setConnectTimeout(3000);
			conn .setRequestProperty("Accept-Encoding", "identity"); 	//���ò���gzipѹ��  ��û�ȡ�Ĵ�СΪ-1
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
	
		//��ֹ����
		public void exitApp(){
			System.exit(0);
			//android.os.Process.killProcess(android.os.Process.myPid());
			instance = null;
		}
		
		/**
		 * ����App������Class(��һ�������Activity),����ǿ���˳�����(����ǿ��)
		 * */
		public void setMainActivityClass(Class mainActivity){
			this.mainActivity = mainActivity;
		}
		
		/**
		 * 
		 * �˳�����
		 * ͨ����ת�������׽���(���������棬��һ�������Activity)��ֹ����
		 * */
		public static void exitApp(Context activity){
			try {
				Intent intent = new Intent();   
				intent.setClass(activity, mainActivity);  
				//mainActivity��һ���ǳ�������,ֻ����������Ч
				//Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
				intent.putExtra(UPDATE_CANCEL_EXIT_APP, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //ע�Ȿ�е�FLAG����  
				activity.startActivity(intent);  
				//activity.finish();  
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//������ɵ����Ի����Ƿ�װ
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
