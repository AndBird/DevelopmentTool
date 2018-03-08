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
 * 功能: 安卓应用安装，启动和部分状态判断
 */
public class AppTool {
    private final static String TAG = AppTool.class.getName();

    /**
     * 安装apk
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
     *功能: 安装应用
     * */
    public static boolean installApk(Context context, String fileName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(fileName);
        //Log.e(TAG, "安装应用：" + fileName);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + fileName), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }else{
            Toast.makeText(context, "安装文件不存在", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    /**
     *功能: 获取应用版本号
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
     *功能： 判断服务是否在运行
     * @param  className : is service pack + name
     * */
    public static boolean isServiceRunning(Context context, String className){
        try {
            ActivityManager myManager=((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE));
            ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(30);
            for(int i = 0 ; i<runningService.size();i++) {
                if(runningService.get(i).service.getClassName().toString().equals(className)) {
                    System.out.println("Tool  服务已经在运行");
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
     * 判断应用是否已经启动
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
     * 功能: 判断一个应用是否安装过
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
            Log.e(TAG, packname + "没有安装");
            flag = false;
        }else{
            Log.e(TAG, packname + "已经安装");
            flag = true;
        }
        return flag;
    }

    /**
     * 启动应用
     * */
    public static void startApp(Context context, String pack){
        try {
            if(pack != null){
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(pack);
                try {
                    // 可能由于某些原因导致程序无法启动，添加异常块保证程序不死掉
                    context.startActivity(intent);
                } catch (NullPointerException e) {
                    Toast.makeText(context, "该程序无法启动！", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将应用放到前端显示:
     * @param context ： 用activity不能将应用带到前端
     * 
     * 法一:按home键退出，再通过此方法启动有5秒钟的延迟，且需要用while循环来重复才有效,对于用intent回到桌面的，一次即可。
     * 法二:法二不存在法一的问题
     */
    private void bringAppToFront(Context context, String pack){
        try {
//			//法一,切换任务栈，需要api 11以上
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
//				if(!isRunning){//需直接新建
//					Log.e(TAG, "该任务栈不存在");
//					Toast.makeText(getApplicationContext(), "该栈已退出，需重新运行", Toast.LENGTH_SHORT).show();
//					break;
//				}
//
//				//判断应用是否处于前端显示
//				runningTaskInfos = manager.getRunningTasks(1);
//				if (runningTaskInfos.size() > 0 && TextUtils.equals(getPackageName(), runningTaskInfos.get(0).topActivity.getPackageName())) {
//					break;
//				}
//				Thread.sleep(5);
//			}
//			Log.e(TAG, "完成");

//			//法二，启动activity
//			Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
//			intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		    startActivity(intent);

            //法三，通过广播启动


//			//法四
            Intent in = context.getPackageManager().getLaunchIntentForPackage(pack);
            in.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            context.startActivity(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断应用是否处于前端显示
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
     *返回手机Home
     * */
    public static void goPhoneHome(Context context){
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(home);
    }
    
   /**
    *  判断辅助服务是否开启
    *  @param context : 用于获取辅助服务所在应用的包名
    *  @param serviceClass : 辅助服务类
    *  */
    public static boolean isAccessibilitySettingsOn(Context context, Class serviceClass) {
		final String service = context.getPackageName() + "/" + serviceClass.getName();
		DebugUtils.debug(TAG, "service:" + service);
		return isAccessibilitySettingsOn(context, service);
	}
    
    
    /**
     *  判断辅助服务是否开启
     *  @param context : 用于获取辅助服务所在应用的包名
     *  @param accessibilityServiceString : 辅助服务类string ,格式：应用包名/AccessibilityService类的包名.AccessibilityService类名
     *  
     *  如：com.accessibilityservices.demo./com.test.accessibilityservice.TestAccessibilityService
     *  */
     public static boolean isAccessibilitySettingsOn(Context context, String accessibilityServiceString) {
 		int accessibilityEnabled = 0;
 		// TestService为对应的服务
 		DebugUtils.debug(TAG, "serviceString:" + accessibilityServiceString);
 		try {
 			accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
 					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
 			DebugUtils.debug(TAG, "accessibilityEnabled = " + accessibilityEnabled);
 		} catch (Settings.SettingNotFoundException e) {
 			DebugUtils.debug(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
 		}
 		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

 		if (accessibilityEnabled == 1) {//系统设置中有辅助服务开启，未开启任何辅助服务时返回的是0
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
