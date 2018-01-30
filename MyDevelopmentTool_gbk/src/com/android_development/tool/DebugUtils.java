package com.android_development.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 * ����: ��־��ӡ����־�ļ�����
 * ʹ�÷���: �̳�ToolBaseApplication or call init() , debug(), exitDebug()
 * */
public class DebugUtils {
	private static final String TAG = DebugUtils.class.getName();
	private static String logPath;//��־�ļ�Ŀ¼·��
	private static boolean printLog = true; //��ӡ������־
	private static boolean writeFileLog = false; //������־�ļ�
	public static final String logRootDir = "DelelopmentTool"; //��־�ļ���Ŀ¼
	private static SimpleDateFormat sd;
	private static boolean logFileOpened = false; //��־�ļ��Ƿ��Ѵ�������
	private static BufferedWriter bw;
	private static FileWriter fw;

	public static void init(Context context){
		if(logPath == null) {
			logPath = Environment.getExternalStorageDirectory().toString() + File.separator + logRootDir + File.separator +
					context.getPackageName() + File.separator + "log" + File.separator;
		}
		sd = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	}

	private static void createLog(){
		try {
			if(TextUtils.isEmpty(logPath)){
				LogUtil.printLogE(TAG, "logDir is empty, please make sure call init(Context context) mothod");
				return ;
			}
			File logPathFile = new File(logPath);
			if(!logPathFile.exists()) {
				logPathFile.mkdirs();
			}
			//����������־�ļ�
			Date date = new Date();
			String str1 = sd.format(date);

			final String savePath = logPath + str1 +  "_log.txt";
			File file = new File(savePath);
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					LogUtil.printLogE(TAG, "createLog :" + e.toString());
					e.printStackTrace();
				}
			}
			try {
				fw = new FileWriter(savePath);
				bw = new BufferedWriter(fw);
				logFileOpened = true;
			} catch (IOException e) {
				LogUtil.printLogE(TAG, "createLog :" + e.toString());
				e.printStackTrace();
				logFileOpened = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logFileOpened = false;
		}
	}

	public static void exitDebug(){
		try {
			if(writeFileLog){
				logFileOpened = false;
				if(bw != null){
					bw.flush();
					bw.close();
				}
				if(fw != null){
					fw.close();
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public static void debug(String TAG, String logStr){
		if(printLog){
			LogUtil.printLogE(TAG, logStr);
		}
		if(writeFileLog){
			if(!logFileOpened){
				createLog();
				sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			if(logFileOpened) {//��־�ļ��Ѵ���
				Date date = new Date();
				String str1 = sd.format(date);
				try {
					bw.write("��" + str1 + "�� " + TAG + ":  msg=" + logStr);
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public  static void debug(String TAG, Throwable ex) {
		try {
			ex.printStackTrace();
			debug(TAG, getThrowableInfo(ex));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printInfo(String TAG, String msg) {
		try {
			debug(TAG, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String getThrowableInfo(Throwable ex) {
		String str = null;
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			str = stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	

	/*
	* printLog �Ƿ��ӡ��־  true ��ӡ��־��default is true
	* */
	public static void setPrintLog(boolean printLog) {
		DebugUtils.printLog = printLog;
	}

	/**
	 * writeFileLog �Ƿ񱣴���־  true ����, default is false
	 * */
	public static void setWriteFileLog(boolean writeFileLog) {
		DebugUtils.writeFileLog = writeFileLog;
	}

	/**
	 * printLog �Ƿ��ӡ��־  true ��ӡ��־��default is true
	 * writeFileLog �Ƿ񱣴���־  true ����, default is false
	 * */
	public static void setLog(boolean printLog, boolean writeFileLog) {
		DebugUtils.printLog = printLog;
		DebugUtils.writeFileLog = writeFileLog;
	}
}
