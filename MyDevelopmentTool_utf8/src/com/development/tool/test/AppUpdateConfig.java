package com.development.tool.test;

import java.io.File;

import android.content.Context;
import android.os.Environment;


public class AppUpdateConfig {
    //更新
    public static String APP_CUR_VERSION = "1.9"; /**当前版本号*/
    public static String APP_VERSION_CHECK_URL = "http://www.baidu.com";  //更新地址
    //测试下载地址
    //public static String downloadUrl = "http://gdown.baidu.com/data/wisegame/714a4ed6bd6ca122/baiduditu_609.apk";//百度地图
    public static String downloadUrl = "http://gdown.baidu.com/data/wisegame/a3f987af08889edb/zhongzixiguan_31.apk";//种子习惯

    //文件保存根目录
    public static String ROOT = "appUpdateDemo"; /**包名，作文件根目录*/
    public static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()  + File.separator + ROOT + File.separator;

}
