package com.development.tool.test;


import com.android_development.tool.update.AppUpdateManager;
import com.development.android.tool.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AppUpdateMainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_update);
		
		AppUpdateManager.getInstance().startCheckUpdate(AppUpdateMainActivity.this, false);
		
		//直接下载
		//AppUpdateManager.getInstance().startDownload(MainActivity.this, Config.downloadUrl, "test.apk");
	}
	
	
	//退出整个程序,添加到程序的入口Activity处 ,且入口要加 android:launchMode="singleTop"
	@Override  
	protected void onNewIntent(Intent intent) {  
		super.onNewIntent(intent);  
		 if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0 && intent.hasExtra(AppUpdateManager.UPDATE_CANCEL_EXIT_APP) && intent.getBooleanExtra(AppUpdateManager.UPDATE_CANCEL_EXIT_APP, false)) {  
			 AppUpdateMainActivity.this.finish();  
			 AppUpdateManager.getInstance().exitApp();
		 }  
	}  
	
}
