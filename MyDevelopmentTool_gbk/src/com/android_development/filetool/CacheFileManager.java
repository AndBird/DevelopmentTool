package com.android_development.filetool;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.android_development.tool.DebugUtils;

/**
 * 
 * 缓存清理
 */
public class CacheFileManager {
    private static Map<String, Long> fileMap = new HashMap<String, Long>();
    private final static String TAG = CacheFileManager.class.getName();
    private final static  boolean printDetail = false; //是否打印详细信息
    //复制到sd卡的保存目录
    private static final String SDPath = Environment.getExternalStorageDirectory().toString() + "/";

   
    private static final String[] needCleanDir = new String[]{"cache", "files", "app_webview"};//需要清理的目录，
    private static final String[] needSaveDir = new String[]{"shared_prefs", "databases", "lib"};//需要保留的目录
    
    //需要保留的文件(完整路径)
    private static final String[] exception = new String[]{
    };
    
    //获取Cache的信息
    public static void getCacheFileInfo(Context context){
        File appCacheDir = null;
//        if(appCacheDir == null) {
//            appCacheDir = context.getCacheDir();
//            Log.e(TAG, "getCacheFileInfo()  1111");
//        }

        if(appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName();
//            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
//            String cacheDirPath = "/data/data/" + context.getPackageName() + "/app_xwalkcore/";
            DebugUtils.debug(TAG, "getCacheFileInfo()  22222");
            appCacheDir = new File(cacheDirPath);
        }

        DebugUtils.debug(TAG, "cache totalSize=" + Formatter.formatFileSize(context, getDirSize(appCacheDir)));
        printCacheFileInfo(context, appCacheDir);
    }

    //计算cache文件大小
    public static long getDirSize(File file) {
        //判断文件是否存在
        if (file != null && file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小
                return file.length();
            }
        } else {
        	DebugUtils.debug(TAG, "file is not exists");
            return 0;
        }
    }


    //打印cache文件
    private static void  printCacheFileInfo(Context context, File file){
        if (file != null && file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                if(printDetail) {
                    Log.e(TAG, "====================================");
                    Log.e(TAG, file.getName() + " 【dir】 totalSize=" + Formatter.formatFileSize(context, getDirSize(file)));
                }
                printFileChangeInfo(context, file);
                File[] children = file.listFiles();
                for (File f : children) {
                    printCacheFileInfo(context, f);
                }
            } else {//如果是文件则直接返回其大小
                if(printDetail) {
                    Log.e(TAG, "cache File Name=" + file.getName() + " , cache File path=" + file.getAbsolutePath() + " , size=" + Formatter.formatFileSize(context, getDirSize(file)));
                }
                printFileChangeInfo(context, file);
            }
        } else {
            Log.e(TAG, "file is not exists");
        }
    }

    //打印cache文件变化信息
    private static void printFileChangeInfo(Context context, File file){
        String path = file.getAbsolutePath();
        long curSize = getDirSize(file);
        if(fileMap.containsKey(path)){
            long oldSize = fileMap.get(path);
            if(curSize != oldSize) {
                if(printDetail) {
                    Log.e(TAG, "change ---------------------------");
                    if (file.isDirectory()) {
                        Log.e(TAG, path + " 【dir】 oldSize=" + Formatter.formatFileSize(context, oldSize) + ", curSize=" + Formatter.formatFileSize(context, curSize) + " , change=" + (curSize - oldSize));
                    } else {
                        Log.e(TAG, path + " 【file】 oldSize=" + Formatter.formatFileSize(context, oldSize) + ", curSize=" + Formatter.formatFileSize(context, curSize) + " , change=" + (curSize - oldSize));
                    }
                    Log.e(TAG, "change --------------------------- end");
                }else{
                    if (file.isDirectory()) {
                        Log.e(TAG, path + " 【dir】 oldSize=" + Formatter.formatFileSize(context, oldSize) + ", curSize=" + Formatter.formatFileSize(context, curSize) + " , change=" + (curSize - oldSize));
                    } else {
                        Log.e(TAG, path + " 【file】 oldSize=" + Formatter.formatFileSize(context, oldSize) + ", curSize=" + Formatter.formatFileSize(context, curSize) + " , change=" + (curSize - oldSize));
                    }
                }
            }else{
                if (file.isDirectory()) {
                    Log.e(TAG, path + " 【dir】 size nochange");
                } else {
                    Log.e(TAG, path + " 【file】 size nochange");
                }
            }
        }else{
            Log.e(TAG, "new ---------------------------");
            if(file.isDirectory()) {
                Log.e(TAG, path + " 【dir】 is new, curSize=" + Formatter.formatFileSize(context, curSize));
            }else{
                Log.e(TAG, path + " 【file】 is new, curSize=" + Formatter.formatFileSize(context, curSize));
            }
            Log.e(TAG, "new --------------------------- end");
        }
        fileMap.put(path, curSize);
    }

   
    
    //清除cache
    public static void deleteCacheFile(Context context){
        String cacheDirPath = "/data/data/" + context.getPackageName() + "/app_xwalkcore/";
        DebugUtils.debug(TAG, "deleCacheFile");
        File appCacheDir = new File(cacheDirPath);
        //Log.e(TAG, "pp=" + appCacheDir.getParent());
        DebugUtils.debug(TAG, "before delete ,the total size=" + Formatter.formatFileSize(context, getDirSize(appCacheDir)));
        deleteDir(appCacheDir);
        DebugUtils.debug(TAG, "after delete ,the total size=" + Formatter.formatFileSize(context, getDirSize(appCacheDir)));
    }
    
    //清除cache
    public static void deleteCacheFileFilter(Context context){
    	try {
    		    String cacheDirPath = "/data/data/" + context.getPackageName() + "/";
    		    DebugUtils.debug(TAG, "deleCachePath:" + cacheDirPath);
    	        for(String s : needCleanDir){
    	        	if(s != null){
    	        		File appCacheDir = new File(cacheDirPath + s);
    	        		DebugUtils.debug(TAG, "deleCacheDir:" + (cacheDirPath + s));
//    	        		DebugUtils.printInfo(TAG, "before delete ,the total size=" + Formatter.formatFileSize(context, getDirSize(appCacheDir)));
    	      	        deleteDir(appCacheDir);
//    	      	        DebugUtils.printInfo(TAG, "after delete ,the total size=" + Formatter.formatFileSize(context, getDirSize(appCacheDir)));
    	        	}
    	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


    //清除游戏缓存
    private static void deleteDir(File file){
        if (file != null && file.exists()) {
            if (file.isDirectory()){
                File[] children = file.listFiles();
                for (File f : children) {
                    deleteDir(f);
                }
            } else {
               deleFile(file);
            }
        } else {
        	DebugUtils.debug(TAG, "file is not exists");
        }
    }

    //删除文件
    private static void deleFile(File file){
        if (file != null && file.exists()) {
            //保留关卡信息
            if(isNeedToSaveFile(file.getAbsolutePath())){// || isNeedToSaveDir(file.getParent()) || file.getAbsolutePath().startsWith(TimeStamp)){
                return ;
            }
            file.delete();
            if(printDetail) {
                Log.e(TAG, "delete=" + file.getAbsolutePath());
                Log.e(TAG, "delete result=" + !file.exists());
            }
        } else {
        	DebugUtils.debug(TAG, "file is not exists");
        }
    }

    //是否保留path本文件
    private static boolean isNeedToSaveFile(String path){
        for (String s : exception){
            if(s != null && s.equals(path)){
                if(printDetail) {
                    Log.e(TAG, "save1:" + path);
                }
                return true;
            }
        }
        return false;
    }

    //判断文件的上一层文件夹
    private static boolean isNeedToSaveDir(String path){
        for (String s : needSaveDir){
            if(s.equals(path)){
            	DebugUtils.debug(TAG, "save2:" + path);
                return true;
            }
        }
        return false;
    }

    //copy 缓存到sd卡
    public static void copyCacheFileToSD(){
        File dir = new File(SDPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        for(String s : exception){
            int start = s.lastIndexOf("/");
            String name = s.substring(start + 1);
            copyCacheFileToSD(s, name);
        }
    }

    private static void copyCacheFileToSD(String filePath, String fileName){
    		DebugUtils.debug(TAG, "start copy");
	    	OutputStream out;
            InputStream in;
	    	try {
	    		DebugUtils.debug(TAG, "copy:" + filePath);
                File f = new File(filePath);
                if(!f.exists()){//cache中的文件是否存在
                	DebugUtils.debug(TAG, f.getAbsolutePath() + " is not fond!");
                    return ;
                }
                in = new FileInputStream(filePath);
	    		String newFileName = SDPath + fileName;
	    		out = new FileOutputStream(newFileName);
		    	byte[] buffer = new byte[1024];
		    	int read;
		    	while ((read = in.read(buffer)) != -1) {
		    		out.write(buffer, 0, read);
		    	}
		    	in.close();
		    	in = null;
		    	out.flush();
		    	out.close();
		    	out = null;
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}

    //将备份的缓存copy到cache
    public static void copyCacheToCache(){
        File dir = new File(SDPath);
        if(!dir.exists()){//缓存文件夹不存在
        	DebugUtils.debug(TAG, "sd cache not exists");
            return;
        }
        for(String s : exception){
            int start = s.lastIndexOf("/");
            String name = s.substring(start + 1);
            String path = s.substring(0, start);
            copyCacheFileToCache(name, path);
        }
    }

    private static void copyCacheFileToCache(String fileName, String savePath){
    	DebugUtils.debug(TAG, "start copy to cache");
        OutputStream out;
        InputStream in;
        try {
            File f = new File(SDPath + fileName);
            if(!f.exists()){//sd卡被复制文件不存在
            	DebugUtils.debug(TAG, "sd 卡cache file:" + f.getAbsolutePath() + " is not fond!");
                return ;
            }
            File dir = new File(savePath);
            if(!dir.exists()){//创建复制保存目录
            	DebugUtils.debug(TAG, "cache dir not exists, mkdirs");
                dir.mkdirs();
            }
            //Log.e(TAG, "start copy:" + fileName + ", path=" + savePath);
            //String newFileName = SDPath + fileName;
            File newFile = new File(savePath, fileName);
            if(newFile.exists()){
                //如果文件存在，那么就不覆盖
            	DebugUtils.debug(TAG, "game cache exists, copy cancel");
                return;
            }
            out = new FileOutputStream(newFile);
            in = new FileInputStream(SDPath + fileName);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取缓存总大小
    public static long getCacheTotalSize(Context context){
    	long totalSize = 0;
    	for(String s : needCleanDir){
    		String cacheDirPath = "/data/data/" + context.getPackageName() + "/" + s;
    		File appCacheDir = new File(cacheDirPath);
    		totalSize = totalSize +  getDirSize(appCacheDir);
    	}
    	return totalSize;
    }
}
