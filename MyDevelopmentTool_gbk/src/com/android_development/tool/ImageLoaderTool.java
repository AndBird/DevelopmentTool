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

/**ImageLoader���ؿ��*/
public class ImageLoaderTool {
	/**����image������˸*/
	/*public static void showImage(final String icoUrl, final ImageView imageView, DisplayImageOptions options){
	    if(icoUrl != null && icoUrl.equals(imageView.getTag())){
        	//��ַ��ͬʱ���ټ���,������˸, add 2016.7.8
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
	 * Universal-Image-Loader��RoundedBitmapDisplayer����ǿ�棬�����Զ���ͼƬ4�����е�ָ����ΪԲ��
	 
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
	     * ���췽��˵�����趨Բ�����ش�С�����нǶ�ΪԲ��
	     * @param cornerRadiusPixels Բ�����ش�С
	     *//*
	 
	    public FlexibleRoundedBitmapDisplayer(int cornerRadiusPixels){
	        this.cornerRadius = cornerRadiusPixels;
	        this.corners = CORNER_ALL;
	    }
	    *//**
	     * ���췽��˵�����趨Բ�����ش�С��ָ����ΪԲ��
	     * @param cornerRadiusPixels Բ�����ش�С
	     * @param corners �Զ���Բ��
	 
	     * CORNER_NONE����Բ��
	 
	     * CORNER_ALL ȫΪԲ��
	 
	     * CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT��ָ��Բ�ǣ�ѡ�����������  �� 
	 
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
	            //�Ȼ�һ��Բ�Ǿ��ν�ͼƬ��ʾΪԲ��
	            canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);
	            int notRoundedCorners = corners ^ CORNER_ALL;
	            //�ĸ��ǲ���Բ�����ٰ����þ��λ�����
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
                     .threadPoolSize(3)//�����̳߳��ڼ�������
             		 .threadPriority(Thread.NORM_PRIORITY - 2)
                     .denyCacheImageMultipleSizesInMemory()
                     .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                     //.discCacheFileNameGenerator(new Md5FileNameGenerator())
                     .memoryCacheSize(2 * 1024 * 1024) // �ڴ滺������ֵ  
                     .tasksProcessingOrder(QueueProcessingType.LIFO)
                     .build();
             ImageLoader.getInstance().init(config);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
     }*/
}
