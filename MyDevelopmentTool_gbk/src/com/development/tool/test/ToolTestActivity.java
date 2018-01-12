package com.development.tool.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android_development.nettool.NetDataRequest;
import com.android_development.stringtool.AesEncryptor;
import com.development.android.tool.R;

public class ToolTestActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private NetDataRequest netDataRequest;
    
    private Button update;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        netDataRequest = new NetDataRequest(this, "http://www.baidu.com", new NetDataRequest.RequestDataListener() {
            @Override
            public void onRequestDataStart() {
                Log.e("onRequestDataStart", "onRequestDataStart");
            }

            @Override
            public void onRequestCacheDataFinish(String cacheData) {
                Log.e("cache", cacheData);
            }

            @Override
            public void onRequestNetDataFinish(String netData) {
                Log.e("net", netData);
            }

            @Override
            public void onRequestDataFail(int error) {
                Log.e("Fail", "Fail");
            }
        });

        netDataRequest.requestDataByGet(true);
        
        findViewById(R.id.update).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ToolTestActivity.this, AppUpdateMainActivity.class));
				ToolTestActivity.this.finish();
			}
		});
        
        AesEncryptor aes = new AesEncryptor();
        aes.setKey("1234567890123456");
        Log.e("aes", aes.encrypt("123"));
    }
}
