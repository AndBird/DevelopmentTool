package com.android_development.tool.update;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

//强制更新时退出程序用
public class UpdateEmptyActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("UpdateEmptyActivity", "onCreate");
		finish();
		AppUpdateManager.getInstance().exitApp();
	}
}
