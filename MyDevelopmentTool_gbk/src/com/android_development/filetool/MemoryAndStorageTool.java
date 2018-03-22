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
 * ����: �ڴ�ʹ洢�ռ�
 */
public class MemoryAndStorageTool {
    private static  final String TAG = MemoryAndStorageTool.class.getName();
    public static final int ERROR = -1;

    /**
     * SDCARD�Ƿ��
     * ע��:�����ֻ��ϲ�����(������)�������ֻ�Environment.getExternalStorageDirectory()Ĭ��ָ���ڲ��洢
     */
    @Deprecated
    private static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    

    /**
     * SDCARD�Ƿ����
     * ��ǿ�棬δ�ܴ�������
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
     * ��ȡ�ֻ��ڲ�ʣ��洢�ռ�
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
     * ��ȡ�ֻ��ڲ��ܵĴ洢�ռ�
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
     * ��ȡSDCARDʣ��洢�ռ�
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
     * ��ȡSDCARD�ܵĴ洢�ռ�
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
     * ���ܣ� ��ȡ�ֻ������ڴ�
     * @return long[�����ڴ棬 ����ڴ�] or long[0, 0]
     * */
    public static long[] getRamMemoryInfo(Context context){
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mActivityManager.getMemoryInfo(mi);
            long availMem = mi.availMem;   //ϵͳ�����ڴ�
            long maxMem = 0;
            long threshold = mi.threshold; //ϵͳ�ڴ治��ķ�ֵ
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
     *����: �ͷ��ڴ棬��ҪȨ��
     *<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
     *<uses-permission android:name="android.permission.GET_TASKS" />
     * */
    //�ͷ��ڴ�
    public static void releaseMemory(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PackageManager pm = context.getPackageManager();
                    ApplicationInfo app = null;
                    String packName = context.getPackageName();
                    ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    // ͨ������ActivityManager��getRunningAppProcesses()�������ϵͳ�������������еĽ���
                    List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
                        // ����ID��
                        int pid = appProcessInfo.pid;
                        // �û�ID ������Linux��Ȩ�޲�ͬ��IDҲ�Ͳ�ͬ ���� root��
                        int uid = appProcessInfo.uid;
                        // ��������Ĭ���ǰ�������������android��process=""ָ��
                        String processName = appProcessInfo.processName;
                        // ���ÿ�����������е�Ӧ�ó���(��),��ÿ��Ӧ�ó���İ���
                        String[] packageList = appProcessInfo.pkgList;
                        for (String pkg : packageList) {
                            app = pm.getApplicationInfo(pkg, 0);
                            if(app != null){
                                if((app.flags & ApplicationInfo.FLAG_SYSTEM) > 0){//ϵͳ���̲�ɱ
                                    continue;
                                }else{
                                    if(!pkg.equals(packName)){
                                        //System.out.println("ɱ����  ����:" + pkg + ",pid=" + pid);
                                        mActivityManager.killBackgroundProcesses(pkg);

                                        //forceStopPackage(����),ҪϵͳȨ�ޣ�<uses-permission android:name="android.permission.FORCE_STOP_PACKAGES"/>
					            		//Method forceStopPackage = mActivityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
					                    //forceStopPackage.setAccessible(true);
					                    //forceStopPackage.invoke(mActivityManager, pkg);
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
      *  ��ȡ�ڲ��洢�ռ�·��(���޸�,�����ڻ�ȡ���еĴ洢�ռ�)
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
  	 * ��ȡ�洢·��
  	 * external true Ϊ����sd��
  	 * 			false Ϊ�ڴ�洢��
  	 * ע��: ��ȡʧ��ʱ����null,�ʵ�������Ҫ�жϲ�ʹ��Environment.getExternalStorageDirectory()
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
  	                	 //add 2016.9.1,��ǿһ�£�(�а�׿ģ������ȡ������sd��·������д)
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
  	 *  ��ȡsd��·��
  	 *  1.Environment.getExternalStorageDirectory() �ڲ����ֻ��л�ȡ�������ڴ�洢�������ֻ���
  	 *  2.���ϵͳ���������ȴ洢�����ô洢�У���ôEnvironment.getExternalStorageDirectory()��ȡ����Ҳ���ڲ��洢·��
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
  	   * ��ȡĿ¼�Ŀ��ô洢�ռ�
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
	  	   * ��ȡĿ¼��Total�洢�ռ�
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
