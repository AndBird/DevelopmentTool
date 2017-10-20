package com.android_development.nettool;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android_development.filetool.CacheTool;
import com.android_development.tool.MD5Tool;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/12.
 *功能: 网络数据请求类
 *使用: 实现RequestDataListener接口回调，RequestDataListener接口中的方法在线程中，不可直接更新界面
 */


public class NetDataRequest {
    private final static String TAG = NetDataRequest.class.getName();
    private Context mContext = null;
    private String url = null;

    private boolean useCacheDefaultValue = true; //使用缓存
    private String cacheName = ""; //根据url产生
    private boolean cacheNameGenerateByMd5 = true; //true, cahceName 通过md5产生
    private boolean readCacheIsOk = false; //获取缓存成功

    private String cahceData, netData;

   /**
    * 获取数据失败原因
    * */
    public final static int FAIL_ERROR_URL_EMPTY = 1; //url is empty
    public final static int FAIL_ERROR_NET_DATA_EMPTY = 2; //get data from net fail
    public final static int FAIL_ERROR_CACHE_AND_NET_DATA_BOTH_EMPTY = 3; //get data from net and cache both fail
    public final static int FAIL_ERROR_POST_PARAMS = 4; //post request 参数为null or empty or exception


    private RequestDataListener requestDataListener = null;

    public interface RequestDataListener{
        public void onRequestDataStart();
        public void onRequestCacheDataFinish(String cacheData);
        public void onRequestNetDataFinish(String netData);
        public void onRequestDataFail(int error);
    }

    public void setOnLoadDataListener(RequestDataListener requestDataListener){
        this.requestDataListener = requestDataListener;
    }

    public boolean isReadCacheIsOk() {
        return readCacheIsOk;
    }

    public boolean isUseCacheDefault() {
        return useCacheDefaultValue;
    }

    public String getCahceData() {
        return cahceData;
    }

    public String getNetData() {
        return netData;
    }



    /**
     * @param url  is netUrl ,the cache is useless
     * */
    public NetDataRequest(String url) {
        this.url = url;
        this.useCacheDefaultValue = false;
    }

    /**
     * @param url  is netUrl，the cache default is useful
     * */
    public NetDataRequest(Context context, String url) {
        this.url = url;
        this.mContext = context;
    }

    /**
     * @param url  is netUrl，the cache default is useful
     * @param cacheNameGenerateByMd5  default is true
     * */
    public NetDataRequest(Context context, String url, boolean cacheNameGenerateByMd5){
        this.url = url;
        this.mContext = context;
        this.cacheNameGenerateByMd5 = cacheNameGenerateByMd5;
    }

    /**
     * @param url  is netUrl，the cache default is useful
     * */
    public NetDataRequest(Context context, String url, RequestDataListener requestDataListener) {
        this.url = url;
        this.mContext = context;
        this.requestDataListener = requestDataListener;
    }

    /**
     * @param url  is netUrl，the cache default is useful
     * @param cacheNameGenerateByMd5  default is true
     * */
    public NetDataRequest(Context context, String url, boolean cacheNameGenerateByMd5, RequestDataListener requestDataListener) {
        this.url = url;
        this.mContext = context;
        this.cacheNameGenerateByMd5 = cacheNameGenerateByMd5;
        this.requestDataListener = requestDataListener;
    }

    /**
     * 功能: Get请求数据, cacheNameGenerateByMd5 is useful
     * @param useCache 启用缓存
     * */
    public void requestDataByGet(boolean useCache){
        if(TextUtils.isEmpty(url)){
            Log.e(TAG, "url is empty");
            onRequestDataFail(FAIL_ERROR_URL_EMPTY);
            return ;
        }
        onRequestDataStart();
        this.useCacheDefaultValue = this.useCacheDefaultValue && useCache;
        if(this.useCacheDefaultValue){
            if(cacheNameGenerateByMd5) {
                cacheName = MD5Tool.getMD5(url);
            }else{
                cacheName = this.url;
            }
            getCacheData();
        }
        getDataFormNetByGet();
    }


    /**
     * 功能: Get请求数据, cacheNameGenerateByMd5 is useful
     * @param cacheName 启用缓存文件(用于解决url中含有时间等随机参数)
     * */
    public void requestDataByGet(String cacheName){
        if(TextUtils.isEmpty(url)){
            Log.e(TAG, "url is empty");
            onRequestDataFail(FAIL_ERROR_URL_EMPTY);
            return ;
        }
        onRequestDataStart();
        if(this.useCacheDefaultValue){
            this.cacheName = cacheName;
            getCacheData();
        }
        getDataFormNetByGet();
    }


    /**
     * 功能: Post请求数据,cacheName Generate By Md5, cacheNameGenerateByMd5 is useless
     * @param useCache 启用缓存
     * @param encoding 编码
     *
     * */
    public void requestDataByPost(boolean useCache, Map<String, String> params, String encoding){
        if(TextUtils.isEmpty(url)){
            Log.e(TAG, "url is empty");
            onRequestDataFail(FAIL_ERROR_URL_EMPTY);
            return ;
        }
        if(params == null || params.isEmpty()){
            Log.e(TAG, "requestDataByPost params is null or empty");
            onRequestDataFail(FAIL_ERROR_POST_PARAMS);
            return ;
        }

        StringBuffer sb = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (null != encoding) {
                    //System.out.println("Tool 上传utf8前:"+entry.getKey()+","+entry.getValue());
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encoding));
                    //	System.out.println("Tool 上传utf8后:"+entry.getKey()+","+URLEncoder.encode(entry.getValue(),encoding));
                } else {
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                }
                sb.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "requestDataByPost params Exception");
            onRequestDataFail(FAIL_ERROR_POST_PARAMS);
            return ;
        }

        String data = sb.deleteCharAt(sb.length() - 1).toString();
        onRequestDataStart();
        this.useCacheDefaultValue = this.useCacheDefaultValue && useCache;
        if(this.useCacheDefaultValue){
            //if(cacheNameGenerateByMd5) {
                cacheName = MD5Tool.getMD5(url + data);
            //}else{
            //    cacheName = this.url;
            //}
            getCacheData();
        }
        getDataFormNetByPost(data);
    }


    /**
    * 读取缓存数据
    * */
    private void getCacheData(){
        if(CacheTool.isExistDataCache(mContext,cacheName)){
            String data = (String)CacheTool.readObject(mContext, cacheName);
            if(data != null){
                onRequestCacheDataFinish(data);
            }else{
                readCacheIsOk = false;
            }
        }
    }

    /**
     * 从网络上获取数据  Get
     * */
    private void getDataFormNetByGet(){
        new Thread() {
            public void run() {
                try {
                    InputStream inputStream = NetTool.getInputStreamByGet(url);
                    String dataStr = NetTool.InputStreamToString(inputStream);
                    //Log.e(mContext, "数据:" + dataStr);
                    if(dataStr != null){
                        if(useCacheDefaultValue) {//缓存数据
                            CacheTool.saveObject(mContext, dataStr, cacheName);
                        }
                        onRequestNetDataFinish(dataStr);
                    }else{
                        requestDataFailFromNet();
                    }
                } catch (Exception e) {
                    requestDataFailFromNet();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从网络上获取数据 Post
     *
     * */
    private void getDataFormNetByPost(final String data){
        new Thread() {
            public void run() {
                try {
                    InputStream inputStream = NetTool.getInputStreamByPost(url, data);
                    String dataStr = NetTool.InputStreamToString(inputStream);
                    //Log.e(mContext, "数据:" + dataStr);
                    if(dataStr != null){
                        if(useCacheDefaultValue) {//缓存数据
                            CacheTool.saveObject(mContext, dataStr, cacheName);
                        }
                        onRequestNetDataFinish(dataStr);
                    }else{
                        requestDataFailFromNet();
                    }
                } catch (Exception e) {
                    requestDataFailFromNet();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void requestDataFailFromNet(){
        if(readCacheIsOk){
            onRequestDataFail(FAIL_ERROR_NET_DATA_EMPTY);
        }else {
            onRequestDataFail(FAIL_ERROR_CACHE_AND_NET_DATA_BOTH_EMPTY);
        }
    }

    private void onRequestDataStart(){
        if(requestDataListener != null){
            requestDataListener.onRequestDataStart();
        }

    }

    private void onRequestCacheDataFinish(String cacheData){
        readCacheIsOk = true;
        this.cahceData = cacheData;
        if(requestDataListener != null){
            requestDataListener.onRequestCacheDataFinish(cacheData);
        }
    }

    private void onRequestNetDataFinish(String netData){
        this.netData = netData;
        if(requestDataListener != null){
            requestDataListener.onRequestNetDataFinish(netData);
        }

    }

    private void onRequestDataFail(int error){
        if(requestDataListener != null){
            requestDataListener.onRequestDataFail(error);
        }
    }
}
