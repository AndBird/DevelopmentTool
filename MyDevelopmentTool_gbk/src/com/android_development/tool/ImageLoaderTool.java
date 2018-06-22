package com.android_development.tool;

/*import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;*/
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**ImageLoader加载框架*/
public class ImageLoaderTool {
	/**加载image避免闪烁*/
	/*public static void showImage(final String icoUrl, final ImageView imageView, DisplayImageOptions options){
	    if(icoUrl != null && icoUrl.equals(imageView.getTag())){
        	//地址相同时不再加载,避免闪烁, add 2016.7.8
        }else{
        	imageView.setTag("");
        	ImageLoader.getInstance().displayImage(icoUrl, imageView, options, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					imageView.setTag(icoUrl);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
        }
	}*/
	
	
	
	/**
	 * Universal-Image-Loader中RoundedBitmapDisplayer的增强版，可以自定义图片4个角中的指定角为圆角
	 
	 */
	/*public static class FlexibleRoundedBitmapDisplayer implements BitmapDisplayer {
	    protected int cornerRadius;
	    protected int corners;
	 
	    public static final int CORNER_TOP_LEFT = 1;
	    public static final int CORNER_TOP_RIGHT = 1 << 1;
	    public static final int CORNER_BOTTOM_LEFT = 1 << 2;
	    public static final int CORNER_BOTTOM_RIGHT = 1 << 3;
	    public static final int CORNER_ALL = CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;
	    *//**
	     * 构造方法说明：设定圆角像素大小，所有角都为圆角
	     * @param cornerRadiusPixels 圆角像素大小
	     *//*
	 
	    public FlexibleRoundedBitmapDisplayer(int cornerRadiusPixels){
	        this.cornerRadius = cornerRadiusPixels;
	        this.corners = CORNER_ALL;
	    }
	    *//**
	     * 构造方法说明：设定圆角像素大小，指定角为圆角
	     * @param cornerRadiusPixels 圆角像素大小
	     * @param corners 自定义圆角
	 
	     * CORNER_NONE　无圆角
	 
	     * CORNER_ALL 全为圆角
	 
	     * CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT　指定圆角（选其中若干组合  ） 
	 
	     *//*
	    public FlexibleRoundedBitmapDisplayer(int cornerRadiusPixels, int corners){
	        this.cornerRadius = cornerRadiusPixels;
	        this.corners = corners;
	    }
	 
	    @Override
	    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
	        if (!(imageAware instanceof ImageViewAware)) {
	            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
	        }
	        imageAware.setImageDrawable(new FlexibleRoundedDrawable(bitmap,cornerRadius,corners));
	    }
	    
	    public static class FlexibleRoundedDrawable extends Drawable {
	        protected final float cornerRadius;
	 
	        protected final RectF mRect = new RectF(), mBitmapRect;
	        protected final BitmapShader bitmapShader;
	        protected final Paint paint;
	        private int corners;
	 
	        public FlexibleRoundedDrawable(Bitmap bitmap, int cornerRadius, int corners) {
	            this.cornerRadius = cornerRadius;
	            this.corners = corners;
	 
	            bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
	            mBitmapRect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
	 
	            paint = new Paint();
	            paint.setAntiAlias(true);
	            paint.setShader(bitmapShader);
	        }
	 
	        @Override
	        protected void onBoundsChange(Rect bounds) {
	            super.onBoundsChange(bounds);
	            mRect.set(0, 0, bounds.width(), bounds.height());
	            Matrix shaderMatrix = new Matrix();
	            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
	            bitmapShader.setLocalMatrix(shaderMatrix);
	 
	        }
	 
	        @Override
	        public void draw(Canvas canvas) {
	            //先画一个圆角矩形将图片显示为圆角
	            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
	            int notRoundedCorners = corners ^ CORNER_ALL;
	            //哪个角不是圆角我再把你用矩形画出来
	            if ((notRoundedCorners & CORNER_TOP_LEFT) != 0) {
	                canvas.drawRect(0, 0, cornerRadius, cornerRadius, paint);
	            }
	            if ((notRoundedCorners & CORNER_TOP_RIGHT) != 0) {
	                canvas.drawRect(mRect.right - cornerRadius, 0, mRect.right, cornerRadius, paint);
	            }
	            if ((notRoundedCorners & CORNER_BOTTOM_LEFT) != 0) {
	                canvas.drawRect(0, mRect.bottom - cornerRadius, cornerRadius, mRect.bottom, paint);
	            }
	            if ((notRoundedCorners & CORNER_BOTTOM_RIGHT) != 0) {
	                canvas.drawRect(mRect.right - cornerRadius, mRect.bottom - cornerRadius, mRect.right, mRect.bottom, paint);
	            }
	        }
	 
	        @Override
	        public int getOpacity() {
	            return PixelFormat.TRANSLUCENT;
	        }
	 
	        @Override
	        public void setAlpha(int alpha) {
	            paint.setAlpha(alpha);
	        }
	 
	        @Override
	        public void setColorFilter(ColorFilter cf) {
	            paint.setColorFilter(cf);
	        }
	    }
	}*/
	
	
	 /*public static void initImageLoader(Context context){
     	try {
     		if(ImageLoader.getInstance().isInited()){
         		ImageLoader.getInstance().destroy();
         	}
             ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                     .threadPoolSize(3)//设置线程池内加载数量
             		 .threadPriority(Thread.NORM_PRIORITY - 2)
                     .denyCacheImageMultipleSizesInMemory()
                     .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                     //.discCacheFileNameGenerator(new Md5FileNameGenerator())
                     .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值  
                     .tasksProcessingOrder(QueueProcessingType.LIFO)
                     .build();
             ImageLoader.getInstance().init(config);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
     }*/
}
