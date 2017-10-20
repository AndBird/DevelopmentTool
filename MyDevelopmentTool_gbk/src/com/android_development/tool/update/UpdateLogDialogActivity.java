package com.android_development.tool.update;

import java.util.ArrayList;
import java.util.List;

import com.android_development.uitool.CustomDialogUtils;
import com.android_development.uitool.CustomDialogUtils.CustomDialogClickListener;
import com.development.android.tool.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateLogDialogActivity extends Activity implements OnClickListener{
	private static final String TAG = UpdateLogDialogActivity.class.getName();
	
	
	private static final String INTENT_UPDATE_FORCE = "force_update";//强制更新
	private static final String INTENT_UPDATE_LOG = "update_log";//更新内容(字符串)
	private static final String INTENT_UPDATE_LOG_SPLIT = "update_log_split";//与INTENT_UPDATE_LOG一起
	private static final String INTENT_UPDATE_LOG_LIST = "update_log_list";//更新内容List
	
	private ListView listView;
	private TextView download;
	
	private List<String> logList = new ArrayList<String>();
	private boolean forceUpdate = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_log);
		initView();
		initData();
	}
	
	private void initView(){
		try {
			listView = (ListView) findViewById(R.id.logListView);
			download = (TextView) findViewById(R.id.start_download);
			
			download.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initData(){
		try {
			Intent in = getIntent();
			forceUpdate = in.getBooleanExtra(INTENT_UPDATE_FORCE, false);
			if(in.hasExtra(INTENT_UPDATE_LOG)){
				String log = in.getStringExtra(INTENT_UPDATE_LOG);
				if(!TextUtils.isEmpty(log)){
						String split = in.getStringExtra(INTENT_UPDATE_LOG_SPLIT);
						if(TextUtils.isEmpty(split)){
							logList.add(log);
						}else{
							String[] s = log.split(split);
							if(s != null && s.length > 0){
								for(String tmp : s){
									logList.add(tmp);
								}
							}
						}
				}else{
					logList.add("更新日志暂无");
				}
			}else if(in.hasExtra(INTENT_UPDATE_LOG_LIST)){
				List<String> list = in.getStringArrayListExtra(INTENT_UPDATE_LOG_LIST);
				if(list != null){
					logList.addAll(list);
				}
			}
			
			LogAdapter adapter = new LogAdapter(getApplicationContext());
			listView.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onBackPressed() {
		if(forceUpdate){
			//Toast.makeText(getApplicationContext(), R.string.force_update, Toast.LENGTH_SHORT).show();
			showExitDialog();
			return ;
		}
		super.onBackPressed();
		
	}
	
	private void showExitDialog(){
		try {
			CustomDialogUtils.showCustomDialog(UpdateLogDialogActivity.this, R.string.dlg_msg_notice, R.string.exit_force_update, R.string.dlg_button_exit, R.string.update_now, new CustomDialogClickListener() {
				@Override
				public void onOkClick(View v, AlertDialog dialog) {
					CustomDialogUtils.hideDialog(dialog);
					startUpdateApp();
				}
				
				@Override
				public void onCancelClick(View v, AlertDialog dialog) {
					CustomDialogUtils.hideDialog(dialog);
					AppUpdateManager.exitApp(UpdateLogDialogActivity.this);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	class LogAdapter extends BaseAdapter{
		private  String TAG = LogAdapter.class.getName();
		private Context mContext;
	
		
		public LogAdapter(Context context){
			this.mContext = context;
		}

		@Override
		public int getCount() {
			if(logList == null){
				return 0;
			}
			return logList.size();
		}

		@Override
		public Object getItem(int position) {
			return logList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try{
					 ViewHolder holder = null;
			         if (convertView == null) {
			             convertView = LayoutInflater.from(mContext).inflate(R.layout.update_log_item, parent, false);
			             holder = new ViewHolder();
			             holder.logIndexView = (TextView) convertView.findViewById(R.id.log_index);
			             holder.logMsgView = (TextView) convertView.findViewById(R.id.log_msg);
			             convertView.setTag(holder);
			         } else {
			             holder = (ViewHolder) convertView.getTag();
			         }
			        if(getCount() > 1){
			        	holder.logIndexView.setText((position + 1) + ".");
			        }
			        holder.logMsgView.setText(logList.get(position));
		     } catch (Exception e) {
		         e.printStackTrace();
		     }
		     return convertView;
		}
		
		 class ViewHolder {
		    TextView logIndexView;
		    TextView logMsgView;
		}
	}
	
	/**
	 * 功能:启动更新日志显示界面
	 * @param updateLog : 更新日志
	 * @param split : 日志条数分割符
	 * */
	public static void startActivitySelf(Context context, String updateLog, String split, boolean forceUpdate){
		try {
			Intent in = new Intent(context, UpdateLogDialogActivity.class);
			in.putExtra(INTENT_UPDATE_FORCE, forceUpdate);
			in.putExtra(INTENT_UPDATE_LOG, updateLog);
			in.putExtra(INTENT_UPDATE_LOG_SPLIT, split);
			context.startActivity(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能:启动更新日志显示界面
	 * @param updateLog : 更新日志
	 * */
	public static void startActivitySelf(Context context, ArrayList<String> updateLog , boolean forceUpdate){
		try {
			Intent in = new Intent(context, UpdateLogDialogActivity.class);
			in.putExtra(INTENT_UPDATE_FORCE, forceUpdate);
			in.putStringArrayListExtra(INTENT_UPDATE_LOG_LIST, updateLog);
			context.startActivity(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v){
		try {
			switch (v.getId()) {
			case R.id.start_download:
				startUpdateApp();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startUpdateApp(){
		try {
			AppUpdateManager.getInstance().showDownloadWindow();
			UpdateLogDialogActivity.this.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
