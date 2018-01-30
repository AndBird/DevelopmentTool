package com.development.tool.test;


import java.util.ArrayList;

import com.android_development.tool.DebugUtils;
import com.android_development.tool.update.AppUpdateManager;
import com.android_development.tool.update.AppUpdateManager.UpdateDataReceiveListener;
import com.development.android.tool.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AppUpdateMainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_update);
		
		AppUpdateManager.getInstance().startCheckUpdate(AppUpdateMainActivity.this, false, AppUpdateConfig.APP_VERSION_CHECK_URL, new UpdateDataReceiveListener(){
			@Override
			public void analyzeVersionInfo(String data) {
				try {
					//解析数据这里就不写了
					String serverVersion = "2.1";
					String low_version = "2.0";
					String downloadUrl = AppUpdateConfig.downloadUrl;
					String updateLog = "";
					//AppUpdateManager.getInstance().setAnalyzeVersionInfo(serverVersion, low_version, AppUpdateConfig.APP_CUR_VERSION, downloadUrl, updateLog, AppUpdateConfig.filePath);
				
					ArrayList<String> list = new ArrayList<String>();
					list.add("更新内容1");
					list.add("更新内容2更新内容更新内容\n更新内容更新内容\n更新内容\n更新内容更新内容更新内容更新内容更新内容");
					list.add("更新内容3更新内容更新内容\n更新内容更新内容\n更新内容\n更新内容更新内容更新内容\n更新内容更新内容更新内容更新内容更新内容更新内容");
					AppUpdateManager.getInstance().setAnalyzeVersionInfo2(serverVersion, low_version, AppUpdateConfig.APP_CUR_VERSION, downloadUrl, list, AppUpdateConfig.filePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//直接下载
		//AppUpdateManager.getInstance().startDownload(AppUpdateMainActivity.this, AppUpdateConfig.downloadUrl, AppUpdateConfig.filePath, "test.apk");
	}
	
	
	/**
	 * 对于强制更新来用:
	 * 退出整个程序,添加到程序的入口Activity处 ,且入口要加 android:launchMode="singleTop"
	 * 
	 * */
	@Override  
	protected void onNewIntent(Intent intent) {  
		super.onNewIntent(intent);  
		 DebugUtils.debug("AppUpdateMainActivity", "onNewIntent");
		 if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0 && intent.hasExtra(AppUpdateManager.UPDATE_CANCEL_EXIT_APP) && intent.getBooleanExtra(AppUpdateManager.UPDATE_CANCEL_EXIT_APP, false)) {  
			 AppUpdateMainActivity.this.finish(); //避免重启  
			 AppUpdateManager.getInstance().exitApp();
		 }  
	}  
	
}
