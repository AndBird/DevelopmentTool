package com.android_development.filetool;

import java.io.*;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;


/**
 * @author Administrator
 * @date 创建时间：2015-10-29 上午11:49:04
 * 功能: Asset中文件的复制与读
 * 使用: AssetFileManager.copyDir(getAssets(), @文件夹名, @保存路径);
 *       asset下的文件或文件目录是可以多层次的，但是都以asset下的第一层为根，根层用文件名(带后缀)或文件夹名即可描述
 */
public class AssetFileTool {
	private final static String TAG = AssetFileTool.class.getName();
	private static String destDir = ""; //复制保存路径


	/**
	 * @param dir : need copy dirName or fileName
	 * @param destDir : save path
	 * */
	public static void copyDir(AssetManager as, String dir, String destDir){
		if(TextUtils.isEmpty(dir)){
			Log.e(TAG + "/copyDir", "Asset: copy path is empty");
			return;
		}
		if(TextUtils.isEmpty(destDir)){
			Log.e(TAG + "/copyDir", "Asset: copy destDir is empty");
			return;
		}
		if(as == null){
			Log.e(TAG + "/copyDir", "Asset: AssetManager is null");
			return ;
		}
		AssetFileTool.destDir = destDir;
		copyFileOrDir(as, dir);
	}

	private static void copyFileOrDir(AssetManager assetManager,  String path) {
		String assets[] = null;
		try{
			Log.e("copyFileDir", path);
			assets = assetManager.list(path);
			if (assets.length == 0) {//文件
				Log.e(TAG + "/copyFileOrDir", "copyFile" + path);
				copyFile(assetManager, path);
			} else {//文件夹
				File dir = new File(destDir + "/" + path);
				if (!dir.exists()){
					Log.e(TAG + "/copyFileOrDir", "makeDir:" + destDir + "/" + path);
					dir.mkdirs();
				}
				for (int i = 0; i < assets.length; ++i) {
					copyFileOrDir(assetManager, path + "/" + assets[i]);
				}
			}
		} catch (IOException ex) {
			Log.e(TAG + "/copyFileOrDir", "I/O Exception" + ex);
		}
	}

	private static void copyFile(AssetManager assetManager, String filePath){
		InputStream in = null;
		OutputStream out = null;
		try {
			Log.e(TAG + "/copyFile", "copy:" + filePath);
			in = assetManager.open(filePath);
			String newFileName = destDir + "/" + filePath;
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
			Log.e(TAG + "/copyFile", e.getMessage());
		}
	}

	/**
	 * 功能: 读取asset文件
	 * */
	public static String readAsset(Resources resources, String assetName, String defaultStr) {
		InputStream is = null;
		BufferedReader r = null;
		try {
			is = resources.getAssets().open(assetName);
			r = new BufferedReader(new InputStreamReader(is, "UTF8"));
			StringBuilder sb = new StringBuilder();
			String line = r.readLine();
			if(line != null) {
				sb.append(line);
				line = r.readLine();
				while(line != null) {
					sb.append('\n');
					sb.append(line);
					line = r.readLine();
				}
			}
			return sb.toString();
		} catch (IOException e) {
			return defaultStr;
		} finally {
			close(is);
			close(r);
		}
	}

	private static boolean close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		} else {
			return false;
		}
	}
}
