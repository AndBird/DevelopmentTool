package com.android_development.tool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import android.content.Context;

/**通过shell写入事件*/
public class ShellCmdTool {

	private static final String TAG = ShellCmdTool.class.getSimpleName();
	
	private volatile static ShellCmdTool instance = new ShellCmdTool();
	private boolean isInited = false;
	private Context mContext;
	
	private Process process = null;
	private DataOutputStream output = null;
	private DataInputStream input = null;
	
	
	/** Returns singleton class instance */
	public static ShellCmdTool getInstance(){
		if (instance == null) {
			synchronized (ShellCmdTool.class) {
				if (instance == null) {
					instance = new ShellCmdTool();
				}
			}
		}
		return instance;
	}
	
	private ShellCmdTool() {
		isInited = true;
	}
	
	public static void initShellInput(Context context){
		getInstance().init(context);
	}
	
	private void init(Context context){
		if(isInited()){
			DebugUtils.printInfo(TAG, "isInited, return");
			return ;
		}
		initShell();
		
		mContext = context;
		isInited = true;
	}
	
	public static boolean isInited(){
		if(instance == null){
			return false;
		}
		return instance.isInited2();
	}

	private boolean isInited2(){
		if(instance != null && isInited){
			return true;
		}
		return false;
	}
	
	public void release(){
		try {
			 instance = null;
			 mContext = null;
			 releaseShell();
			 DebugUtils.printInfo(TAG, "ShellInput destroy");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void releaseShell(){
		try {
			if(output != null){
				output.close();
				output = null;
			 }
			 if(input != null){
				input.close();
				input = null;
			 }
			 process = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initShell(){
	   DebugUtils.printInfo(TAG, "initShell");
		try {    
	    	 if(process == null || output == null || input == null){
	    		 process = Runtime.getRuntime().exec("su");
			     output = new DataOutputStream(process.getOutputStream());
			     input = new DataInputStream(process.getInputStream());
	    	 }
		} catch (Exception e) {
			e.printStackTrace();
			DebugUtils.printInfo(TAG, "出现异常:" + e.toString());
			releaseShell();
		}
	}

	  private void writeCmd(String cmd){
		    DebugUtils.printInfo(TAG, "writeCmd cmd=" + cmd);
			try {    
			    	 initShell();
			    	 output.writeBytes(cmd);
			    	 output.flush();
			    	 output.writeBytes("\n");
			         output.flush();
			         if (input.available() > 0){
			        	 while(input.available() > 0){ 
			        		 input.readLine();
			        		 DebugUtils.printInfo(TAG, input.readLine());
			        	 }
			         }
			} catch (Exception e) {
				DebugUtils.printInfo(TAG, "出现异常:" + e.toString());
				releaseShell();
			}
		}
	  

	  
	  /**命令*/
	  public static void sendCmd(String cmd){
		  if(isInited()){
			  getInstance().writeCmd(cmd);
		  }else{
			  DebugUtils.printInfo(TAG, "not init");
		  }
	  }
}
