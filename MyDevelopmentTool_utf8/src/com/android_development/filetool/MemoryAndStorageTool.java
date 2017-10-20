package com.android_development.filetool;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/4/12.
 * 功能: 内存和存储空间
 */
public class MemoryAndStorageTool {
    private static  final String TAG = MemoryAndStorageTool.class.getName();
    public static final int ERROR = -1;

    /**
     * SDCARD是否存
     * 注意:部分手机上不可行(如三星)，三星手机Environment.getExternalStorageDirectory()默认指向内部存储
     */
    @Deprecated
    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    

    /**
     * SDCARD是否存在
     * 加强版，未能大量测试
     */
    public static boolean externalMemoryAvailable(final Context context) {
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
	    	 File[] list = context.getExternalCacheDirs();
	         boolean hasSd = list != null && list.length > 1;
	    	 return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && hasSd;
        }else{
       	 return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
    }

    /**
     * 获取手机内部剩余存储空间
     * @return
     */
    public static long getAvailableInternalMemorySize(final Context context) {
    	String root = getStoragePath(context, false);
        if(TextUtils.isEmpty(root)){
	    	File path = Environment.getDataDirectory();
	        return getDirAvailableBytes(path);
        }else{
        	return getDirAvailableBytes(new File(root));
        }
    }

    /**
     * 获取手机内部总的存储空间
     * @return
     */
    public static long getTotalInternalMemorySize(final Context context) {
        try {
            String root = getStoragePath(context, false);
            if(TextUtils.isEmpty(root)){
    	    	File path = Environment.getDataDirectory();
    	        return getDirTotalBytes(path);
            }else{
            	return getDirTotalBytes(new File(root));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 获取SDCARD剩余存储空间
     * @return
     */
    public static long getAvailableExternalMemorySize(final Context context) {
    	String root = getStoragePath(context, true);
        if(TextUtils.isEmpty(root)){
        	File path = Environment.getExternalStorageDirectory();
	        return getDirAvailableBytes(path);
        }else{
        	return getDirAvailableBytes(new File(root));
        }
    }

    /**
     * 获取SDCARD总的存储空间
     * @return
     */
    public static long getTotalExternalMemorySize(final Context context) {
    	 String root = getStoragePath(context, true);
         if(TextUtils.isEmpty(root)){
        	 File path = Environment.getExternalStorageDirectory();
 	        return getDirTotalBytes(path);
         }else{
         	return getDirTotalBytes(new File(root));
         }
    }

    /**
     * 功能： 获取手机运行内存
     * @return long[可用内存， 最大内存] or long[0, 0]
     * */
    public static long[] getRamMemoryInfo(Context context){
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mActivityManager.getMemoryInfo(mi);
            long availMem = mi.availMem;   //系统可用内存
            long maxMem = 0;
            long threshold = mi.threshold; //系统内存不足的阀值
            String str1 = "/proc/meminfo";
            String str2;
            String[] arrayOfString;
            try {
                FileReader localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();
                arrayOfString = str2.split("\\s+");
                maxMem = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
                localBufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
           Log.e(TAG, "meminfo total:" + Formatter.formatFileSize(context, maxMem) + " avail:" + Formatter.formatFileSize(context, availMem));
            return new long[]{availMem, maxMem};
        }catch (Exception e){
            e.printStackTrace();
        }
        return new long[]{0, 0};
    }

    /**
     *功能: 释放内存，需要权限
     *<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
     *<uses-permission android:name="android.permission.GET_TASKS" />
     * */
    //释放内存
    public static void releaseMemory(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PackageManager pm = context.getPackageManager();
                    ApplicationInfo app = null;
                    String packName = context.getPackageName();
                    ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
                    List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
                        // 进程ID号
                        int pid = appProcessInfo.pid;
                        // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
                        int uid = appProcessInfo.uid;
                        // 进程名，默认是包名或者由属性android：process=""指定
                        String processName = appProcessInfo.processName;
                        // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
                        String[] packageList = appProcessInfo.pkgList;
                        for (String pkg : packageList) {
                            app = pm.getApplicationInfo(pkg, 0);
                            if(app != null){
                                if((app.flags & ApplicationInfo.FLAG_SYSTEM) > 0){//系统进程不杀
                                    continue;
                                }else{
                                    if(!pkg.equals(packName)){
                                        //System.out.println("杀进程  包名:" + pkg + ",pid=" + pid);
                                        mActivityManager.killBackgroundProcesses(pkg);

                                        //要系统权限
//					            		 Method forceStopPackage = mActivityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
//					                     forceStopPackage.setAccessible(true);
//					                     forceStopPackage.invoke(mActivityManager, pkg);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
     /**
      *  获取内部存储空间路径(待修改,可用于获取所有的存储空间)
      *  */
  	private String getInternalStorageDirectory(Context context){
  		 List<String> paths = new ArrayList<String>();
           File extFile = Environment.getExternalStorageDirectory();
           try {
               // obtain executed result of command line code of 'mount', to judge
               // whether tfCard exists by the result
               Runtime runtime = Runtime.getRuntime();
               Process process = runtime.exec("mount");
               InputStream is = process.getInputStream();
               InputStreamReader isr = new InputStreamReader(is);
               BufferedReader br = new BufferedReader(isr);
               String line = null;
               int mountPathIndex = 1;
               while ((line = br.readLine()) != null) {
                   // format of sdcard file system: vfat/fuse
                   if ((!line.contains("fat") && !line.contains("fuse") && !line
                           .contains("storage"))
                           || line.contains("secure")
                           || line.contains("asec")
                           || line.contains("firmware")
                           || line.contains("shell")
                           || line.contains("obb")
                           || line.contains("legacy") || line.contains("data")) {
                       continue;
                   }
                   String[] parts = line.split(" ");
                   int length = parts.length;
                   if (mountPathIndex >= length) {
                       continue;
                   }
                   String mountPath = parts[mountPathIndex];
                   if (!mountPath.contains("/") || mountPath.contains("data")
                           || mountPath.contains("Data")) {
                       continue;
                   }
                   File mountRoot = new File(mountPath);
                   if (!mountRoot.exists() || !mountRoot.isDirectory()
                           || !mountRoot.canWrite()) {
                       continue;
                   }
//                   boolean equalsToPrimarySD = mountPath.equals(extFile.getAbsolutePath());
//                   if (equalsToPrimarySD) {
//                       continue;
//                   }
                   Log.e(TAG, "list :" + mountPath);
                   paths.add(mountPath);
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
           if(paths != null && paths.size() > 0){
          	 return paths.get(0);
           }else{
          	 return null;
           }
  	}
  	
  	
  	/**
  	 * 获取存储路径
  	 * external true 为外置sd卡
  	 * 			false 为内存存储卡
  	 * 注意: 获取失败时返回null,故调用者需要判断并使用Environment.getExternalStorageDirectory()
  	 * */
  	public static String getStoragePath(Context mContext, boolean external) {  
  	      StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
  	        Class<?> storageVolumeClazz = null;
  	        try {
  	            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
  	            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
  	            Method getPath = storageVolumeClazz.getMethod("getPath");
  	            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
  	            Object result = getVolumeList.invoke(mStorageManager);
  	            final int length = Array.getLength(result);
  	            for (int i = 0; i < length; i++) {
  	                Object storageVolumeElement = Array.get(result, i);
  	                String path = (String) getPath.invoke(storageVolumeElement);
  	                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
  	                if (external == removable) {
  	                	 //add 2016.9.1,加强一下，(有安卓模拟器获取到外置sd卡路径不可写)
	                	 File mountRoot = new File(path);
	                     if (!mountRoot.exists() || !mountRoot.isDirectory() || !mountRoot.canWrite()) {
	                        Log.e(TAG, "exists:" + mountRoot.exists() + ", isDirectory=" + mountRoot.isDirectory() + " ,canWrite=" + mountRoot.canWrite());
	                    	 return null;
	                     }
  	                    return path;
  	                }
  	            }
  	        } catch (ClassNotFoundException e) {
  	            e.printStackTrace();
  	        } catch (InvocationTargetException e) {
  	            e.printStackTrace();
  	        } catch (NoSuchMethodException e) {
  	            e.printStackTrace();
  	        } catch (IllegalAccessException e) {
  	            e.printStackTrace();
  	        }
  	        return null;
  	}
  	
  	
  	/**
  	 *  获取sd卡路径
  	 *  1.Environment.getExternalStorageDirectory() 在部分手机中获取到的是内存存储如三星手机等
  	 *  2.如果系统设置了优先存储在内置存储中，那么Environment.getExternalStorageDirectory()获取到的也是内部存储路径
  	 *  */
  	 private static String getExtSDCardPaths() {
            String extFileStatus = Environment.getExternalStorageState();
            File extFile = Environment.getExternalStorageDirectory();
            if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                    && extFile.exists() && extFile.isDirectory()
                    && extFile.canWrite()) {
                return extFile.getAbsolutePath();
            }
            return null;
    }
  	 
  	  /**
  	   * 获取目录的可用存储空间
  	   * 	     * @return the number of bytes available on the filesystem rooted at the
	   *         given File
	   */
	    public static long getDirAvailableBytes(File root) {
			StatFs stat = new StatFs(root.getPath());
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				// put a bit of margin (in case creating the file grows the system by a
				// few blocks)
				long availableBlocks = (long) stat.getAvailableBlocks() - 4;
				return stat.getBlockSize() * availableBlocks;
	    	}else{
	    		return stat.getAvailableBytes();
	    	}
	    } 
	    
		  /**
	  	   * 获取目录的Total存储空间
	  	   * 	     * @return the number of bytes total on the filesystem rooted at the
		   *         given File
		   */
		    public static long getDirTotalBytes(File root) {
				StatFs stat = new StatFs(root.getPath());
				if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
					// put a bit of margin (in case creating the file grows the system by a
					// few blocks)
					long blockSize = stat.getBlockSize();
		            long totalBlocks = stat.getBlockCount();
		            return totalBlocks * blockSize;
		    	}else{
		    		return stat.getTotalBytes();
		    	}
		    } 
 }
