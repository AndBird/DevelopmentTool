package com.android_development.filetool;

import java.io.*;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;


/**
 * @author Administrator
 * @date ����ʱ�䣺2015-10-29 ����11:49:04
 * ����: Asset���ļ��ĸ������
 * ʹ��: AssetFileManager.copyDir(getAssets(), @�ļ�����, @����·��);
 *       asset�µ��ļ����ļ�Ŀ¼�ǿ��Զ��εģ����Ƕ���asset�µĵ�һ��Ϊ�����������ļ���(����׺)���ļ�������������
 */
public class AssetFileTool {
	private final static String TAG = AssetFileTool.class.getName();
	private static String destDir = ""; //���Ʊ���·��


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
			if (assets.length == 0) {//�ļ�
				Log.e(TAG + "/copyFileOrDir", "copyFile" + path);
				copyFile(assetManager, path);
			} else {//�ļ���
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
	 * ����: ��ȡasset�ļ�
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
