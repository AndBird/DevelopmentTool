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
     * 把二进制数组转化成位图
     *
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeData(byte[] data, int reqWidth, int reqHeight) {
        // 对位图进行解码的参数设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 对位图进行解码的过程中，避免申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 对位图进行一定比例的压缩处理
        options.inSampleSize = calculateInSimpleSize(options, reqWidth,
                reqHeight);
        // 真正输出位图
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 把资源文件转化成位图
     *
     * @param resources 资源文件
     * @param ResId     解码位图的ID
     * @param reqWidth  指定输出位图的宽度
     * @param reqHeight 指定输出位图的高度
     * @return
     */
    public static Bitmap decodeResources(Resources resources, int ResId,
                                         int reqWidth, int reqHeight) {
        // 对位图进行解码的参数设置
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 对位图进行解码的过程中，避免申请内存空间
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, ResId, options);
        // 对位图进行一定比例的压缩处理
        options.inSampleSize = calculateInSimpleSize(options, reqWidth,
                reqHeight);
        // 真正输出位图
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, ResId, options);
    }

    /**
     * 把文件转化成位图
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
     * 从网络中获取bitmap，文件缓存，推荐使用
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
     * 把InputStream转化成文件,返回文件路径
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
     * 计算压缩比
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSimpleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 获取图片的原始宽高
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        // 压缩比例，假如是4就是压缩到原来大小的1/4
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
     * 精确计算压缩比
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
     * 旋转图片到一定的角度
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
     * 判断图片旋转情况
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
     * Drawable 转bitmap
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

            //sOldBounds记录下drawable的bounds
            Rect sOldBounds = new Rect();
            sOldBounds.set(icon.getBounds());
            icon.setBounds(left, top, left+width, top+height);
            icon.draw(canvas);
            icon.setBounds(sOldBounds);
            canvas.setBitmap(null);
            return bitmap;
    }

	/**将Drawable转自字符串
	 * (由于setBounds，如果drawable已经显示，那么该方法需要在ui中操作)
	 * */
	public static String drawableToString(Drawable drawable) {  
	    if(drawable != null) {  
	        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),  
	                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  : Bitmap.Config.RGB_565);  
	        Canvas canvas = new Canvas(bitmap);  
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  
	        drawable.draw(canvas);  
	        int size = bitmap.getWidth() * bitmap.getHeight() * 4;  
	      
	        // 创建一个字节数组输出流,流的大小为size  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);  
	        // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中  
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
	        // 将字节数组输出流转化为字节数组byte[]  
	        byte[] imagedata = baos.toByteArray();  
	        
	        String icon= Base64.encodeToString(imagedata, Base64.DEFAULT);
	        return icon;  
	    }  
	    return null;  
	}
	
	/**
     * 放大缩小图片
     *
     * @param bitmap 源Bitmap
     * @param w 宽
     * @param h 高
     * @return 目标Bitmap
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
