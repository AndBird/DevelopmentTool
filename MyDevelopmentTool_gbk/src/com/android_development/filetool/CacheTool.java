package com.android_development.filetool;

import android.content.Context;
import android.util.Log;

import java.io.*;

/**
 * Created by Administrator on 2016/4/12.
 * ����: android Ӧ�õ�cache��д������
 * ˵��: �����������ͺ�ArrayList��ֱ�ӻ���
 */
public class CacheTool {
    /**
     * �жϻ��������Ƿ�ɶ�
     * @param cachefileName  is fileName
     * @return
     */
    public static boolean isReadDataCache(Context context,String cachefileName)
    {
        return readObject(context, cachefileName) != null;
    }

    /**
     * ��ȡ����
     * @param cachefileName
     * @return
     * @throws IOException
     */
    public static Serializable readObject(Context context, String cachefileName){
        if(!isExistDataCache(context, cachefileName)){
            Log.d("readObject", "�����ļ�������");
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = context.openFileInput(cachefileName);
            ois = new ObjectInputStream(fis);
            return (Serializable)ois.readObject();
        }catch(FileNotFoundException e){
        }catch(Exception e){
            e.printStackTrace();
            //�����л�ʧ�� - ɾ�������ļ�
            if(e instanceof InvalidClassException){
                File data = context.getFileStreamPath(cachefileName);
                data.delete();
            }
        }finally{
            try {
                ois.close();
            } catch (Exception e) {}
            try {
                fis.close();
            } catch (Exception e) {}
        }
        return null;
    }

    /**
     * �жϻ����Ƿ����
     * @param cachefileName
     * @return
     */
    public static boolean isExistDataCache(Context context, String cachefileName)
    {
        boolean exist = false;
        File data = context.getFileStreamPath(cachefileName);
        if(data.exists())
            exist = true;
        return exist;
    }

    /**
     * �������
     * @param ser
     * @param cachefileName
     * @throws IOException
     */
    public static boolean saveObject(Context context, Serializable ser, String cachefileName) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            fos = context.openFileOutput(cachefileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            try {
                oos.close();
            } catch (Exception e) {}
            try {
                fos.close();
            } catch (Exception e) {}
        }
    }
}
