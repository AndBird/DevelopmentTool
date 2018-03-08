package com.android_development.apptool;

import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android_development.tool.DebugUtils;


/**
 * Created by Administrator on 2016/4/12.
 * ����: ��׿Ӧ�ð�װ�������Ͳ���״̬�ж�
 */
public class AppTool {
    private final static String TAG = AppTool.class.getName();

    /**
     * ��װapk
     */
    public static void installApk(Context context, File apkfile){
        //File apkfile = new File(file);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    /**
     *����: ��װӦ��
     * */
    public static boolean installApk(Context context, String fileName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(fileName);
        //Log.e(TAG, "��װӦ�ã�" + fileName);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + fileName), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }else{
            Toast.makeText(context, "��װ�ļ�������", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    /**
     *����: ��ȡӦ�ð汾��
     * @return version or null
     * */
    public static String getInstalledApkVersion(Context context, String packname){
        PackageInfo packageInfo = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(packname, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return null;
    }

    /**
     *���ܣ� �жϷ����Ƿ�������
     * @param  className : is service pack + name
     * */
    public static boolean isServiceRunning(Context context, String className){
        try {
            ActivityManager myManager=((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE));
            ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
            for(int i = 0 ; i<runningService.size();i++) {
                if(runningService.get(i).service.getClassName().toString().equals(className)) {
                    System.out.println("Tool  �����Ѿ�������");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * �ж�Ӧ���Ƿ��Ѿ�����
     * */
    public static  boolean appIsRunning(Context context, String pack) {
        boolean isAppRunning = false;
        try {
            //System.out.println("appisRunning");
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
            //System.out.println("list.size=" + list.size());
            for (ActivityManager.RunningAppProcessInfo info : list) {
                //System.out.println("info=" + info.processName);
                if (info.processName.equals(pack)) {
                    isAppRunning = true;
                    System.out.println("app is running");
                    break;
                }
            }
        } catch (Exception e) {
            isAppRunning = false;
            e.printStackTrace();
        }
        return isAppRunning;
    }

    /**
     * ����: �ж�һ��Ӧ���Ƿ�װ��
     * */
    public static boolean apkIsInstalled(Context context, String packname){
        PackageInfo packageInfo = null;
        boolean flag = false;
        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null){
            Log.e(TAG, packname + "û�а�װ");
            flag = false;
        }else{
            Log.e(TAG, packname + "�Ѿ���װ");
            flag = true;
        }
        return flag;
    }

    /**
     * ����Ӧ��
     * */
    public static void startApp(Context context, String pack){
        try {
            if(pack != null){
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(pack);
                try {
                    // ��������ĳЩԭ���³����޷�����������쳣�鱣֤��������
                    context.startActivity(intent);
                } catch (NullPointerException e) {
                    Toast.makeText(context, "�ó����޷�������", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��Ӧ�÷ŵ�ǰ����ʾ:
     * @param context �� ��activity���ܽ�Ӧ�ô���ǰ��
     * 
     * ��һ:��home���˳�����ͨ���˷���������5���ӵ��ӳ٣�����Ҫ��whileѭ�����ظ�����Ч,������intent�ص�����ģ�һ�μ��ɡ�
     * ����:���������ڷ�һ������
     */
    private void bringAppToFront(Context context, String pack){
        try {
//			//��һ,�л�����ջ����Ҫapi 11����
//			ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE) ;
//			//if(runningTaskInfos != null)
//			String pack = ServiceWindow.this.getApplication().getPackageName();
//			Log.e("", pack);
//			List<RunningTaskInfo> runningTaskInfos = null;
//			boolean isRunning = false;
//			while(true){
//				runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
//				//Log.e(TAG, "" + runningTaskInfos.size());
//				//int i = 0;
//				for(RunningTaskInfo info : runningTaskInfos){
//					//i++;
//					if(info.topActivity.getPackageName().equals(pack)){
//						//Log.e("", "" + info.topActivity.getPackageName() + "," + i);
//						isRunning = true;
//						//manager.moveTaskToFront(info.id, ActivityManager.MOVE_TASK_WITH_HOME);
//						manager.moveTaskToFront(info.id, 2);
//						break;
//					}
//				}
//
//				if(!isRunning){//��ֱ���½�
//					Log.e(TAG, "������ջ������");
//					Toast.makeText(getApplicationContext(), "��ջ���˳�������������", Toast.LENGTH_SHORT).show();
//					break;
//				}
//
//				//�ж�Ӧ���Ƿ���ǰ����ʾ
//				runningTaskInfos = manager.getRunningTasks(1);
//				if (runningTaskInfos.size() > 0 && TextUtils.equals(getPackageName(), runningTaskInfos.get(0).topActivity.getPackageName())) {
//					break;
//				}
//				Thread.sleep(5);
//			}
//			Log.e(TAG, "���");

//			//����������activity
//			Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
//			intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		    startActivity(intent);

            //������ͨ���㲥����


//			//����
            Intent in = context.getPackageManager().getLaunchIntentForPackage(pack);
            in.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            context.startActivity(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * �ж�Ӧ���Ƿ���ǰ����ʾ
     * */
    public static boolean appIsFrontRunning(Context context, String pack){
        try {
            List<RunningTaskInfo> runningTaskInfos = null;
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE) ;
            runningTaskInfos = manager.getRunningTasks(1);
            if (runningTaskInfos.size() > 0 && TextUtils.equals(pack, runningTaskInfos.get(0).topActivity.getPackageName())) {
                return true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *�����ֻ�Home
     * */
    public static void goPhoneHome(Context context){
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(home);
    }
    
   /**
    *  �жϸ��������Ƿ���
    *  @param context : ���ڻ�ȡ������������Ӧ�õİ���
    *  @param serviceClass : ����������
    *  */
    public static boolean isAccessibilitySettingsOn(Context context, Class serviceClass) {
		final String service = context.getPackageName() + "/" + serviceClass.getName();
		DebugUtils.debug(TAG, "service:" + service);
		return isAccessibilitySettingsOn(context, service);
	}
    
    
    /**
     *  �жϸ��������Ƿ���
     *  @param context : ���ڻ�ȡ������������Ӧ�õİ���
     *  @param accessibilityServiceString : ����������string ,��ʽ��Ӧ�ð���/AccessibilityService��İ���.AccessibilityService����
     *  
     *  �磺com.accessibilityservices.demo./com.test.accessibilityservice.TestAccessibilityService
     *  */
     public static boolean isAccessibilitySettingsOn(Context context, String accessibilityServiceString) {
 		int accessibilityEnabled = 0;
 		// TestServiceΪ��Ӧ�ķ���
 		DebugUtils.debug(TAG, "serviceString:" + accessibilityServiceString);
 		try {
 			accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
 					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
 			DebugUtils.debug(TAG, "accessibilityEnabled = " + accessibilityEnabled);
 		} catch (Settings.SettingNotFoundException e) {
 			DebugUtils.debug(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
 		}
 		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

 		if (accessibilityEnabled == 1) {//ϵͳ�������и�����������δ�����κθ�������ʱ���ص���0
 			DebugUtils.debug(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
 			String settingValue = Settings.Secure.getString(context.getContentResolver(),
 					Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
 			DebugUtils.debug(TAG, "settingValue=" + settingValue);
 			if (settingValue != null) {
 				mStringColonSplitter.setString(settingValue);
 				while (mStringColonSplitter.hasNext()) {
 					String accessibilityService = mStringColonSplitter.next();

 					DebugUtils.debug(TAG, "accessibilityService=" + accessibilityService + " , " + accessibilityServiceString);
 					if (accessibilityService.equalsIgnoreCase(accessibilityServiceString)) {
 						DebugUtils.debug(TAG, "We've found the correct setting - accessibility is switched on!");
 						return true;
 					}
 				}
 			}
 		} else {
 			DebugUtils.debug(TAG, "***ACCESSIBILITY IS DISABLED***");
 		}
 		return false;
 	}
}
