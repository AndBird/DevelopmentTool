package com.android_development.uitool;

import java.io.File;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


/**
 * 消息工具类 3.0以前构建Notification对象用的是Notification.Builder
 * 3.0之后构建Notification对象用的是NotificationCompat.Builder
 * 
 * 支持:需要android-support-v4.jar
 * 
 */
public class NotificationUtil {
		/**
		 * 构建标准Notification
		 * 
		 * @param context
		 * @param title
		 *            消息的标题
		 * @param content
		 *            消息的内容
		 * @param ticker
		 *            第一次出现在屏幕上方的通知提示
		 * @param num
		 *            消息数量
		 * @param sIcon
		 *            消息的小图标
		 * @param bIcon
		 *            消息的大图标
		 * @param cls
		 *            启动的界面
		 */
		public static void setNormalNotification(Context context, String title,
				String content, String ticker, int num, int sIcon, Bitmap bIcon,
				Class<?> cls) {
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
			mBuilder.setContentTitle(title);
			mBuilder.setContentText(content);
			mBuilder.setNumber(num);
			mBuilder.setSmallIcon(sIcon);
			mBuilder.setLargeIcon(bIcon);
			// 第一次提示消息的时候显示在通知栏上
			mBuilder.setTicker(ticker);
			// 自己维护通知的消失
			mBuilder.setAutoCancel(true);
	
			Intent intent = new Intent(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mBuilder.setContentIntent(pendingIntent);
			manager.notify(0, mBuilder.build());
		}
	
		/**
		 * 构建大视图Notification
		 * 
		 * @param context
		 * @param title
		 *            消息的标题
		 * @param content
		 *            消息的内容
		 * @param lines
		 *            大视图显示的行文本
		 * @param ticker
		 *            第一次出现在屏幕上方的通知提示
		 * @param num
		 *            消息数量
		 * @param sIcon
		 *            消息的小图标
		 * @param bIcon
		 *            消息的大图标
		 * @param cls
		 *            启动的界面
		 */
		public static void setBigNotification(Context context, String title,
				String content, String[] lines, String ticker, int num, int sIcon,
				Bitmap bIcon, Class<?> cls) {
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
			mBuilder.setNumber(num);
			mBuilder.setSmallIcon(sIcon);
			mBuilder.setLargeIcon(bIcon);
			// 第一次提示消息的时候显示在通知栏上
			mBuilder.setTicker(ticker);
			// 自己维护通知的消失
			mBuilder.setAutoCancel(true);
	
			/**
			 * 安卓16以上才支持大视图Notification
			 * NotificationCompat.BigPictureStyle, 在细节部分显示一个256dp高度的位图。
			 * NotificationCompat.BigTextStyle，在细节部分显示一个大的文本块。
			 * NotificationCompat.InboxStyle，在细节部分显示一段行文本。
			 * 
			 * 如果仅仅显示一个图片，使用BigPictureStyle是最方便的； 如果需要显示一个富文本信息，则可以使用BigTextStyle；
			 * 如果仅仅用于显示一个文本的信息，那么使用InboxStyle即可。
			 */
			NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
			for (String line : lines) {
				style.addLine(line);
			}
			style.setBigContentTitle(title);
			style.setSummaryText(content);
			mBuilder.setStyle(style);
			
//			if(Build.VERSION.SDK_INT >= 16){
//				//大图片
//				NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
//				style.setBigContentTitle(msg.text).bigPicture(bitmap);
//				builder.setStyle(style);
//			}
	
			Intent intent = new Intent(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mBuilder.setContentIntent(pendingIntent);
			manager.notify(0, mBuilder.build());
		}
	
		/**
		 * 构建自定义通知视图，不能用
		 * 
		 * @param context
		 * @param layoutId
		 *            自定义视图ID
		 * @param viewId
		 *            自定义视图控件ID
		 * @param title
		 * @param content
		 * @param ticker
		 * @param sIcon
		 * @param cls
		 */
		public static void setViewNotification(Context context, int layoutId,
				int viewId, String title, String content, String ticker, int sIcon,
				Class<?> cls) {
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
			RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);
			views.setImageViewResource(viewId, sIcon);
			views.setTextViewText(viewId, title);
			views.setTextViewText(viewId, content);
	
			mBuilder.setContent(views);
			// 第一次提示消息的时候显示在通知栏上
			mBuilder.setTicker(ticker);
			// 自己维护通知的消失
			mBuilder.setAutoCancel(true);
	
			Intent intent = new Intent(context, cls);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mBuilder.setContentIntent(pendingIntent);
			manager.notify(0, mBuilder.build());
		}
		
		/**隐藏通知栏
		 * 
		 * @param notificationId :
		 * */
		public static void hideNotification(Context context, int notificationId){
			try {
				NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				manager.cancel(notificationId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 显示下载进度条(为了防止频繁的通知导致应用吃紧，百分比增加一定的值才通知一次)
		 * 
		 * @param title : 标题
		 * @param progress : 当前进度
		 * @param maxProgress ：最大进度
		 * @param notificationId : 
		 * 
		 * */
		public static void showNotification(Context context, String title, int logoResId, int progress, int maxProgress, int notificationId) {
			try {
				NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
				//RemoteViews views = new RemoteViews(m_Context.getPackageName(), R.layout.upgrade_notification_layout);
				//views.setImageViewResource(R.id.notification_large_icon, R.drawable.ic_launcher);
				//views.setTextViewText(R.id.notification_title, title);
				//views.setTextViewText(R.id.notification_text, content);

				mBuilder.setContentTitle(title);
				if(progress == maxProgress){
					mBuilder.setContentText("下载完成，点击安装");
				}else{
					mBuilder.setContentText("下载中");
				}
				// 第一次提示消息的时候显示在通知栏上
				mBuilder.setTicker(title);
				mBuilder.setSmallIcon(logoResId);
				// 自己维护通知的消失
				mBuilder.setAutoCancel(true);
				 //设置为不可清除模式
				mBuilder.setOngoing(true);
				//采用自定义view
				//mBuilder.setContent(views);
				//views.setProgressBar(R.id.progressBar, maxProgress, progress, false);
				mBuilder.setProgress(maxProgress, progress, false);

				if(progress == maxProgress){
					//下载完整后，点击view启动安装
					/*File apkfile = new File(filePath);
					if (apkfile.exists()) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
						PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
						mBuilder.setContentIntent(pendingIntent);
					}*/
				}
				manager.notify(notificationId, mBuilder.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
