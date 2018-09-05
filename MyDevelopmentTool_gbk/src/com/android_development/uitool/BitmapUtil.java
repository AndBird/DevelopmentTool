package com.android_development.uitool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
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
    
    /**
     * Drawable תbitmap
     */
    public static Bitmap createIconBitmap(Drawable icon, Context context, int width, int height) {
            if (icon instanceof PaintDrawable) {
                PaintDrawable painter = (PaintDrawable) icon;
                painter.setIntrinsicWidth(width);
                painter.setIntrinsicHeight(height);
            } else if (icon instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) icon;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap.getDensity() == Bitmap.DENSITY_NONE) {
                    bitmapDrawable.setTargetDensity(context.getResources().getDisplayMetrics());
                }
            }
            int sourceWidth = icon.getIntrinsicWidth();
            int sourceHeight = icon.getIntrinsicHeight();
            if (sourceWidth > 0 && sourceHeight > 0) {
                // There are intrinsic sizes.
                if (width < sourceWidth || height < sourceHeight) {
                    // It's too big, scale it down.
                    final float ratio = (float) sourceWidth / sourceHeight;
                    if (sourceWidth > sourceHeight) {
                        height = (int) (width / ratio);
                    } else if (sourceHeight > sourceWidth) {
                        width = (int) (height * ratio);
                    }
                } else if (sourceWidth < width && sourceHeight < height) {
                    // Don't scale up the icon
                    width = sourceWidth;
                    height = sourceHeight;
                }
            }

            final Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);

            final int left = 0;
            final int top = 0;

            //sOldBounds��¼��drawable��bounds
            Rect sOldBounds = new Rect();
            sOldBounds.set(icon.getBounds());
            icon.setBounds(left, top, left+width, top+height);
            icon.draw(canvas);
            icon.setBounds(sOldBounds);
            canvas.setBitmap(null);
            return bitmap;
    }

	/**��Drawableת���ַ���
	 * (����setBounds�����drawable�Ѿ���ʾ����ô�÷�����Ҫ��ui�в���)
	 * */
	public static String drawableToString(Drawable drawable) {  
	    if(drawable != null) {  
	        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),  
	                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  : Bitmap.Config.RGB_565);  
	        Canvas canvas = new Canvas(bitmap);  
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
	        drawable.draw(canvas);  
	        int size = bitmap.getWidth() * bitmap.getHeight() * 4;  
	      
	        // ����һ���ֽ����������,���Ĵ�СΪsize  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);  
	        // ����λͼ��ѹ����ʽ������Ϊ100%���������ֽ������������  
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
	        // ���ֽ����������ת��Ϊ�ֽ�����byte[]  
	        byte[] imagedata = baos.toByteArray();  
	        
	        String icon= Base64.encodeToString(imagedata, Base64.DEFAULT);
	        return icon;  
	    }  
	    return null;  
	}
	
	/**
     * �Ŵ���СͼƬ
     *
     * @param bitmap ԴBitmap
     * @param w ��
     * @param h ��
     * @return Ŀ��Bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

}
