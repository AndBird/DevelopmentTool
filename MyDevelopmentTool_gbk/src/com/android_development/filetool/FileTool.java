package com.android_development.filetool;

import android.content.Context;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.*;

/**
 * Created by Administrator on 2016/4/12.
 * ����: file�ĸ��ƣ���д����С����
 */
public class FileTool {
    private final static String TAG = FileTool.class.getName();
    

    /**
     * �����ļ��л��ļ���С
     * */
    public static long getDirSize(File file) {
        //�ж��ļ��Ƿ����
        if (file != null && file.exists()) {
            //�����Ŀ¼��ݹ���������ݵ��ܴ�С
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//������ļ���ֱ�ӷ������С
                return file.length();
            }
        } else {
        	printLog(TAG, "file is not exists");
            return 0;
        }
    }

    /**
     * ��ȡ�ļ���С
     * @return  B, KB, MB, GB
     * */
    public static String getFileSize(Context context, long size){
        if(size > 0){
            return Formatter.formatFileSize(context, size);
        }
        return "0B";
    }

    /**
     * ����: ͨ��·����ȡ�ļ���
     * @param filePath : �ļ�����·��
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
     * ����: ͨ��·����ȡ�ļ���·��
     * @param filePath : �ļ�����·��
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
     * ����: ��ȡ�ļ���·��
     * @param file : �ļ�
     * */
    public static String getFileParentPath(File file){
        if(file != null && file.exists()){
            File pFile = file.getParentFile();
            return  pFile.getAbsolutePath();
        }
        return null;
    }

    /**
     * ����: ��ȡ�ļ������ļ���
     * @param file : �ļ�
     * */
    public static String getFileParentName(File file){
        if(file != null && file.exists()){
           return file.getParent();
        }
        return null;
    }



    /**
     * @param filePath : Դ�ļ�·��
     * @param  destDir : Ŀ��·���������ļ���(Ĭ��ΪԴ�ļ���)
     * */
    public static void copyFile(String filePath, String destDir){
    	printLog(TAG, "start copy");
        OutputStream out;
        InputStream in;
        try {
        	printLog(TAG, "copy:" + filePath);
            File f = new File(filePath);
            if(!f.exists()){//cache�е��ļ��Ƿ����
            	printLog(TAG, f.getAbsolutePath() + " is not fond!");
                return ;
            }
            if (TextUtils.isEmpty(destDir)) {
            	printLog(TAG, destDir + " is empty!");
                return ;
            }
            File dir = new File(destDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            int start = filePath.lastIndexOf("/");
            String fileName = filePath.substring(start + 1);//��ȡԴ�ļ���
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
     * @param filePath : Դ�ļ�·��
     * @param  destDir : Ŀ��·���������ļ���
     * @param  fileName : ������ļ���
     * */
    public static void copyFile(String filePath, String destDir, String fileName){
    	printLog(TAG, "start copy");
        OutputStream out;
        InputStream in;
        try {
        	printLog(TAG, "copy:" + filePath);
            File f = new File(filePath);
            if(!f.exists()){//cache�е��ļ��Ƿ����
            	printLog(TAG, f.getAbsolutePath() + " is not fond!");
                return ;
            }
            if (TextUtils.isEmpty(destDir)) {
            	printLog(TAG, destDir + " is empty!");
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

    // ���ַ��ļ��е��ַ�����������
    public static String readFileByLine(String filePath){
        BufferedReader bufr = null;
        try {
            bufr = new BufferedReader(new FileReader(filePath));
            StringBuffer sb = new StringBuffer();
            String line = null;// ��תվ
            while ((line = bufr.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("��дʧ��");
        } finally {
            try {
                if (bufr != null)
                    bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("��ȡ�ر�ʧ��");
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
    
   
    
  //�����Ϸ����
    public static void deleteDir(File file){
        if (file != null && file.exists()) {
            if (file.isDirectory()){
                File[] children = file.listFiles();
                for (File f : children) {
                    deleteDir(f);
                }
                file.delete();
            } else {
               deleFile(file);
            }
        } else {
        	printLog(TAG, "file is not exists");
        }
    }

    //ɾ���ļ�
    public static void deleFile(File file){
        if (file != null && file.exists()) {
            file.delete();
        } else {
        	printLog(TAG, "file is not exists");
        }
    }

    /**
     * ����:�����ļ��л��ļ�
     * @param srcFile :�������ļ�
     * @dest ����Ŀ¼
     * */
    public static void copyFileOrDir(File srcFile, String dest) {
		try{
				if (srcFile.isFile()) {//�ļ�
					printLog(TAG + "/copyFileOrDir", "copyFile:" + srcFile.getAbsolutePath());
					copyFile(srcFile.getAbsolutePath(), dest, srcFile.getName());
				} else if(srcFile.isDirectory()){//�ļ���
					File dir = new File(dest + "/" + srcFile.getName());
					if (!dir.exists()){//���������ļ���
						printLog(TAG + "/copyFileOrDir", "makeDir:" + dest + "/" + srcFile.getName());
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
    
    private static void printLog(String TAG, String msg){
    	Log.e(TAG, msg);
    }
}
