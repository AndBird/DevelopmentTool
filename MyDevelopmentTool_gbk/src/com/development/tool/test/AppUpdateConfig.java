package com.development.tool.test;

import java.io.File;

import android.content.Context;
import android.os.Environment;


public class AppUpdateConfig {
    //����
    public static String APP_CUR_VERSION = "1.9"; /**��ǰ�汾��*/
    public static String APP_VERSION_CHECK_URL = "http://www.baidu.com";  //���µ�ַ
    //�������ص�ַ
    //public static String downloadUrl = "http://gdown.baidu.com/data/wisegame/714a4ed6bd6ca122/baiduditu_609.apk";//�ٶȵ�ͼ
    public static String downloadUrl = "http://gdown.baidu.com/data/wisegame/a3f987af08889edb/zhongzixiguan_31.apk";//����ϰ��

    //�ļ������Ŀ¼
    public static String ROOT = "appUpdateDemo"; /**���������ļ���Ŀ¼*/
    public static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + ROOT + File.separator;

}
