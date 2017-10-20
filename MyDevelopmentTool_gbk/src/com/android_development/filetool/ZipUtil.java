package com.android_development.filetool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Zip��ѹ��
 * 
 * */
public class ZipUtil {
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte
    private static boolean stopZipFlag;

    public static boolean isStopZipFlag() {
		return stopZipFlag;
	}

	public static void setStopZipFlag(boolean stopZipFlag) {
		ZipUtil.stopZipFlag = stopZipFlag;
	}

	/**
     * ����ѹ���ļ����У�
     *
     * @param resFileList Ҫѹ�����ļ����У��б�
     * @param zipFile ���ɵ�ѹ���ļ�
     * @param zipListener     zipListener
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile,ZipListener zipListener)  {
        ZipOutputStream zipout = null;
        try {
            zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), BUFF_SIZE));
            for (File resFile : resFileList) {
                if(stopZipFlag){
                    break;
                }
                zipFile(resFile, zipout, "",zipListener);
            }
            zipout.close();
            zipout = null;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
            	if(zipout != null){
            		zipout.close();
            		zipout = null;
            	}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }
    }

    /**
     * ����ѹ���ļ����У�
     *
     * @param resFileList Ҫѹ�����ļ����У��б�
     * @param zipFile ���ɵ�ѹ���ļ�
     * @param comment ѹ���ļ���ע��
     * @param zipListener    zipListener
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment,ZipListener zipListener)
           {
               ZipOutputStream zipout = null;
               try {
                   zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), BUFF_SIZE));
                   for (File resFile : resFileList) {
                       zipFile(resFile, zipout, "",zipListener);
                   }
                   zipout.setComment(comment);
                   zipout.close();
                   zipout = null;
               } catch (Exception e) {
                   e.printStackTrace();
               }finally{
               	try {
                	if(zipout != null){
                		zipout.close();
                		zipout = null;
                	}
    			} catch (Exception e2) {
    				e2.printStackTrace();
    			}
            }
    }

    /**
     * ��ѹ��һ���ļ�
     *
     * @param zipFile ѹ���ļ�
     * @param folderPath ��ѹ����Ŀ��Ŀ¼
     */
    public static void upZipFile(File zipFile, String folderPath) {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            zf = new ZipFile(zipFile);
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
                ZipEntry entry = ((ZipEntry)entries.nextElement());
                String str = folderPath + File.separator + entry.getName();
                //str = new String(str.getBytes("8859_1"), "GB2312");//
                File desFile = new File(str);
                if (!desFile.exists()) {
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                }
                in = zf.getInputStream(entry);
                out = new FileOutputStream(desFile);
                byte buffer[] = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = in.read(buffer)) > 0) {
                    out.write(buffer, 0, realLength);
                }
                if(out != null){
            		out.close();
					out = null;
            	}
            	if(in != null){
            		in.close();
					in = null;
            	}
            }
			if(zf != null){
				zf.close();
				zf = null;
			}
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
        		if(out != null){
            		out.close();
					out = null;
            	}
            	if(in != null){
            		in.close();
					in = null;
            	}
				if(zf != null){
					zf.close();
					zf = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }

    }

    /**
     * ��ѹ�ļ��������������ֵ��ļ�
     *
     * @param zipFile ѹ���ļ�
     * @param folderPath Ŀ���ļ���
     * @param nameContains ������ļ�ƥ����
     * @return   ���صļ���
     */
    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath, String nameContains) {
        ArrayList<File> fileList = new ArrayList<File>();
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdir();
        }

        ZipFile zf = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            zf = new ZipFile(zipFile);
            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
                ZipEntry entry = ((ZipEntry)entries.nextElement());
                if (entry.getName().contains(nameContains)) {
                   
                    String str = folderPath + File.separator + entry.getName();
                    str = new String(str.getBytes("8859_1"), "GB2312");
                    // str.getBytes("GB2312"),"8859_1" ���
                    // str.getBytes("8859_1"),"GB2312" ����
                    File desFile = new File(str);
                    if (!desFile.exists()) {
                        File fileParentDir = desFile.getParentFile();
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                        desFile.createNewFile();
                    }
                    in = zf.getInputStream(entry);
                    out = new FileOutputStream(desFile);
                    byte buffer[] = new byte[BUFF_SIZE];
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }
                    in.close();
                    in = null;
                    out.close();
                    out = null;
                    fileList.add(desFile);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	try {
        		if(out != null){
            		out.close();
            	}
            	if(in != null){
            		in.close();
            	}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }
        return null;
    }

    /**
     * ���ѹ���ļ����ļ��б�
     *
     * @param zipFile ѹ���ļ�
     * @return ѹ���ļ����ļ�����
     */
    public static ArrayList<String> getEntriesNames(File zipFile) {
        ArrayList<String> entryNames = new ArrayList<String>();
        Enumeration<?> entries = null;
        try {
            entries = getEntriesEnumeration(zipFile);
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry)entries.nextElement());
                entryNames.add(new String(getEntryName(entry).getBytes("GB2312"), "8859_1"));
            }
            return entryNames;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ���ѹ���ļ���ѹ���ļ�������ȡ��������
     *
     * @param zipFile ѹ���ļ�
     * @return ����һ��ѹ���ļ��б�
     */
    public static Enumeration<?> getEntriesEnumeration(File zipFile) {
        ZipFile zf = null;
        try {
            zf = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zf.entries();

    }

    /**
     * ȡ��ѹ���ļ������ע��
     *
     * @param entry ѹ���ļ�����
     * @return ѹ���ļ������ע��
     */
    public static String getEntryComment(ZipEntry entry)  {
        try {
            return new String(entry.getComment().getBytes("GB2312"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ȡ��ѹ���ļ����������
     *
     * @param entry ѹ���ļ�����
     * @return ѹ���ļ����������
     */
    public static String getEntryName(ZipEntry entry)  {
        try {
            return new String(entry.getName().getBytes("GB2312"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ѹ���ļ�
     *
     * @param resFile ��Ҫѹ�����ļ����У�
     * @param zipout ѹ����Ŀ���ļ�
     * @param rootpath ѹ�����ļ�·��
     */
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath,ZipListener zipListener)
          {
        try {
            rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                    + resFile.getName();
            rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
            if (resFile.isDirectory()) {
                File[] fileList = resFile.listFiles();
                int length=fileList.length;
                // Log.e("zipprogress", (int)((1 / (float) (length+1))*100)+"%");
                zipListener.zipProgress((int)((1 / (float) (length+1))*100));
                for (int i=0;i<length;i++) {
                    if(stopZipFlag){
                        break;
                    }
                    File file=fileList[i];
                    zipFile(file, zipout, rootpath,zipListener);
                    // Log.e("zipprogress", (int)(((i+2) / (float) (length+1))*100)+"%");
                    zipListener.zipProgress((int)(((i+2) / (float) (length+1))*100));
                }
            } else {
                byte buffer[] = new byte[BUFF_SIZE];
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile), BUFF_SIZE);
                zipout.putNextEntry(new ZipEntry(rootpath));
                int realLength;
                while ((realLength = in.read(buffer)) != -1) {
                    if(stopZipFlag){
                        break;
                    }
                    zipout.write(buffer, 0, realLength);
                }
                in.close();
                zipout.flush();
                zipout.closeEntry();
            }
        }catch (Exception e){
        	e.printStackTrace();
        }

    }
    public interface ZipListener{
    	void zipProgress(int zipProgress);
    }
}