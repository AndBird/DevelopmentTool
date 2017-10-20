package com.android_development.uitool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {
    /**
     * �Ѷ���������ת����λͼ
     *
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeData(byte[] data, int reqWidth, int reqHeight) {
        // ��λͼ���н���Ĳ�������
        BitmapFactory.Options options = new BitmapFactory.Options();
        // ��λͼ���н���Ĺ����У����������ڴ�ռ�
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // ��λͼ����һ��������ѹ������
        options.inSampleSize = calculateInSimpleSize(options, reqWidth,
                reqHeight);
        // �������λͼ
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * ����Դ�ļ�ת����λͼ
     *
     * @param resources ��Դ�ļ�
     * @param ResId     ����λͼ��ID
     * @param reqWidth  ָ�����λͼ�Ŀ��
     * @param reqHeight ָ�����λͼ�ĸ߶�
     * @return
     */
    public static Bitmap decodeResources(Resources resources, int ResId,
                                         int reqWidth, int reqHeight) {
        // ��λͼ���н���Ĳ�������
        BitmapFactory.Options options = new BitmapFactory.Options();
        // ��λͼ���н���Ĺ����У����������ڴ�ռ�
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, ResId, options);
        // ��λͼ����һ��������ѹ������
        options.inSampleSize = calculateInSimpleSize(options, reqWidth,
                reqHeight);
        // �������λͼ
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, ResId, options);
    }

    /**
     * ���ļ�ת����λͼ
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeFile(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSimpleSizeByMath(reqWidth, reqHeight, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * �������л�ȡbitmap���ļ����棬�Ƽ�ʹ��
     *
     * @param inputStream
     * @param cacheFilePath
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static Bitmap decodeInputStream(InputStream inputStream, String cacheFilePath, int width, int height)
            throws Exception {
        return decodeFile(streamToFile(inputStream, cacheFilePath), width, height);
    }

    /**
     * ��InputStreamת�����ļ�,�����ļ�·��
     *
     * @param inStream
     * @param catchFile
     * @return
     * @throws Exception
     */
    public static String streamToFile(InputStream inStream, String catchFile) throws Exception {

        File tempFile = new File(catchFile);
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        inStream.close();
        fileOutputStream.close();
        return catchFile;
    }

    /**
     * ����ѹ����
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSimpleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // ��ȡͼƬ��ԭʼ���
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        // ѹ��������������4����ѹ����ԭ����С��1/4
        int inSimpleSize = 1;
        if (imageWidth > reqWidth || imageHeight > reqHeight) {
            final int widthRatio = Math.round((float) imageWidth
                    / (float) reqWidth);
            final int heightRatio = Math.round((float) imageHeight
                    / (float) reqHeight);
            inSimpleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        return inSimpleSize;
    }

    /**
     * ��ȷ����ѹ����
     *
     * @param reqWidth
     * @param reqHeight
     * @param options
     * @return
     */
    public static int calculateInSimpleSizeByMath(int reqWidth, int reqHeight, BitmapFactory.Options options) {
        int inSampleSize = 1;
        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
            int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * ��תͼƬ��һ���ĽǶ�
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }
    
    /**
     * �ж�ͼƬ��ת���
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
