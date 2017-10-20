package com.android_development.filetool;

import android.content.Context;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.android_development.timetool.TimeTool;
import com.android_development.tool.DebugUtils;

import java.io.*;

/**
 * Created by Administrator on 2016/4/12.
 * 功能: file的复制，读写，大小计算
 */
public class FileTool {
    private final static String TAG = FileTool.class.getName();

    /**
     * 计算文件夹或文件大小
     * */
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
            Log.e(TAG, "file is not exists");
            return 0;
        }
    }

    /**
     * 获取文件大小
     * @return  B, KB, MB, GB
     * */
    public static String getFileSize(Context context, long size){
        if(size > 0){
            return Formatter.formatFileSize(context, size);
        }
        return "0B";
    }

    /**
     * 功能: 通过路径获取文件名
     * @param filePath : 文件完整路径
     * */
    public  static String getFileName(String filePath){
            if (filePath == null)
                return "";
            int index = filePath.lastIndexOf('/');
            if (index> -1)
                return filePath.substring(index+1);
            else
                return filePath;
    }

    /**
     * 功能: 通过路径获取文件父路径
     * @param filePath : 文件完整路径
     * */
    public static String getFileParentPath(String filePath){
        if (TextUtils.equals("/", filePath))
            return filePath;
        String parentPath = filePath;
        if (parentPath.endsWith("/"))
            parentPath = parentPath.substring(0, parentPath.length()-1);
        int index = parentPath.lastIndexOf('/');
        if (index > 0){
            parentPath = parentPath.substring(0, index);
        } else if (index == 0)
            parentPath = "/";
        return parentPath;
    }

    /**
     * 功能: 获取文件父路径
     * @param file : 文件
     * */
    public static String getFileParentPath(File file){
        if(file != null && file.exists()){
            File pFile = file.getParentFile();
            return  pFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * 功能: 获取文件所属文件夹
     * @param file : 文件
     * */
    public static String getFileParentName(File file){
        if(file != null && file.exists()){
           return file.getParent();
        }
        return null;
    }



    /**
     * @param filePath : 源文件路径
     * @param  destDir : 目标路径，不含文件名(默认为源文件名)
     * */
    public static void copyFile(String filePath, String destDir){
        Log.e(TAG, "start copy");
        OutputStream out;
        InputStream in;
        try {
            Log.e(TAG, "copy:" + filePath);
            File f = new File(filePath);
            if(!f.exists()){//cache中的文件是否存在
                Log.e(TAG, f.getAbsolutePath() + " is not fond!");
                return ;
            }
            if (TextUtils.isEmpty(destDir)) {
                Log.e(TAG, destDir + " is empty!");
                return ;
            }
            File dir = new File(destDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            int start = filePath.lastIndexOf("/");
            String fileName = filePath.substring(start + 1);//获取源文件名
            in = new FileInputStream(filePath);
            out = new FileOutputStream(new File(dir, fileName));
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

    /**
     * @param filePath : 源文件路径
     * @param  destDir : 目标路径，不含文件名
     * @param  fileName : 保存的文件名
     * */
    public static void copyFile(String filePath, String destDir, String fileName){
        Log.e(TAG, "start copy");
        OutputStream out;
        InputStream in;
        try {
            Log.e(TAG, "copy:" + filePath);
            File f = new File(filePath);
            if(!f.exists()){//cache中的文件是否存在
                Log.e(TAG, f.getAbsolutePath() + " is not fond!");
                return ;
            }
            if (TextUtils.isEmpty(destDir)) {
                Log.e(TAG, destDir + " is empty!");
                return ;
            }
            File dir = new File(destDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            in = new FileInputStream(filePath);
            out = new FileOutputStream(new File(dir, fileName));
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

    // 把字符文件中的字符读出并返回
    public static String readFileByLine(String filePath){
        BufferedReader bufr = null;
        try {
            bufr = new BufferedReader(new FileReader(filePath));
            StringBuffer sb = new StringBuffer();
            String line = null;// 中转站
            while ((line = bufr.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读写失败");
        } finally {
            try {
                if (bufr != null)
                    bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("读取关闭失败");
            }
        }
    }
    
    /**
     * @return the number of bytes available on the filesystem rooted at the
     *         given File
     */
    public static long getAvailableBytes(File root) {
		StatFs stat = new StatFs(root.getPath());
		// put a bit of margin (in case creating the file grows the system by a
		// few blocks)
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
    }
    
   
    
  //清除游戏缓存
    public static void deleteDir(File file){
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
        	Log.e(TAG, "file is not exists");
        }
    }

    //删除文件
    public static void deleFile(File file){
        if (file != null && file.exists()) {
            file.delete();
        } else {
        	Log.e(TAG, "file is not exists");
        }
    }

    /**
     * 功能:复制文件夹或文件
     * @param srcFile :待复制文件
     * @dest 保存目录
     * */
    public static void copyFileOrDir(File srcFile, String dest) {
		try{
				if (srcFile.isFile()) {//文件
					Log.e(TAG + "/copyFileOrDir", "copyFile:" + srcFile.getAbsolutePath());
					copyFile(srcFile.getAbsolutePath(), dest, srcFile.getName());
				} else if(srcFile.isDirectory()){//文件夹
					File dir = new File(dest + "/" + srcFile.getName());
					if (!dir.exists()){//创建保存文件夹
						Log.e(TAG + "/copyFileOrDir", "makeDir:" + dest + "/" + srcFile.getName());
						dir.mkdirs();
					}
					File[] fList = srcFile.listFiles();
					if(fList != null && fList.length > 0){
						for(File f : fList){
							copyFileOrDir(f, dir.getAbsolutePath());
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
